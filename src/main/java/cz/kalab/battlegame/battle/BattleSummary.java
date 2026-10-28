package cz.kalab.battlegame.battle;

import java.time.Instant;

/**
 * Battle history entry without the battle log.
 */
public record BattleSummary(Long id, String fighterA, String fighterB, String winner, int rounds, Instant foughtAt) {

    public static BattleSummary from(BattleRecord record) {
        return new BattleSummary(record.getId(), record.getFighterA(), record.getFighterB(),
                record.getWinner(), record.getRounds(), record.getFoughtAt());
    }
}
