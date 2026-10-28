package cz.kalab.battlegame.battle;

import cz.kalab.battlegame.common.NotFoundException;
import cz.kalab.battlegame.fighter.Fighter;
import cz.kalab.battlegame.fighter.FighterService;
import cz.kalab.battlegame.game.Arena;
import cz.kalab.battlegame.game.BattleReport;
import cz.kalab.battlegame.game.Dice;
import cz.kalab.battlegame.game.Mage;
import cz.kalab.battlegame.game.Warrior;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BattleService {

    private final FighterService fighterService;
    private final BattleRecordRepository battleRepository;
    private final Dice dice;
    private final Clock clock;

    public BattleService(FighterService fighterService, BattleRecordRepository battleRepository,
                         Dice dice, Clock clock) {
        this.fighterService = fighterService;
        this.battleRepository = battleRepository;
        this.dice = dice;
        this.clock = clock;
    }

    @Transactional
    public BattleResponse fight(BattleRequest request) {
        Warrior warriorA = toWarrior(fighterService.getEntity(request.fighterAId()));
        Warrior warriorB = toWarrior(fighterService.getEntity(request.fighterBId()));

        BattleReport report = new Arena(warriorA, warriorB, dice).battle();

        BattleRecord saved = battleRepository.save(new BattleRecord(
                warriorA.getName(), warriorB.getName(), report.winner(), report.rounds(), Instant.now(clock)));
        return new BattleResponse(saved.getId(), report.winner(), report.rounds(), report.events());
    }

    public List<BattleSummary> history() {
        return battleRepository.findAllByOrderByFoughtAtDesc().stream()
                .map(BattleSummary::from)
                .toList();
    }

    public BattleSummary findById(Long id) {
        return battleRepository.findById(id)
                .map(BattleSummary::from)
                .orElseThrow(() -> new NotFoundException("Battle", id));
    }

    /**
     * Creates a fresh domain object (full health and mana) from the stored fighter.
     */
    private Warrior toWarrior(Fighter fighter) {
        return switch (fighter.getType()) {
            case WARRIOR -> new Warrior(fighter.getName(), fighter.getHealth(), fighter.getDamage(),
                    fighter.getDefense(), dice);
            case MAGE -> new Mage(fighter.getName(), fighter.getHealth(), fighter.getDamage(),
                    fighter.getDefense(), dice, fighter.getMana(), fighter.getMagicDamage());
        };
    }
}
