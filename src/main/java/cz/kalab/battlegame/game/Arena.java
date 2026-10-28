package cz.kalab.battlegame.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates a battle between two warriors.
 * The arena doesn't print anything - it returns a {@link BattleReport},
 * so the same logic can be used from a REST API, a console or a test.
 */
public class Arena {
    /**
     * Protection against endless battles (e.g. when nobody can break the rival's defense)
     */
    public static final int MAX_ROUNDS = 100;

    private final Warrior warriorA;
    private final Warrior warriorB;
    private final Dice dice;

    public Arena(Warrior warriorA, Warrior warriorB, Dice dice) {
        this.warriorA = warriorA;
        this.warriorB = warriorB;
        this.dice = dice;
    }

    public BattleReport battle() {
        List<BattleEvent> events = new ArrayList<>();

        // the dice decides who starts
        boolean warriorBStarts = dice.roll() <= dice.getNumberOfFaces() / 2;
        Warrior first = warriorBStarts ? warriorB : warriorA;
        Warrior second = warriorBStarts ? warriorA : warriorB;

        log(events, 0, "%s VS %s".formatted(warriorA, warriorB));
        log(events, 0, "%s starts first".formatted(first));

        int round = 0;
        while (first.isAlive() && second.isAlive() && round < MAX_ROUNDS) {
            round++;
            turn(first, second, round, events);
            if (second.isAlive()) {
                turn(second, first, round, events);
            }
        }

        String winner = resolveWinner();
        log(events, round, winner == null
                ? "The battle ended in a draw after %d rounds".formatted(round)
                : "%s wins!".formatted(winner));
        return new BattleReport(winner, round, List.copyOf(events));
    }

    private void turn(Warrior attacker, Warrior defender, int round, List<BattleEvent> events) {
        Attack attack = attacker.attack();
        log(events, round, attack.magic()
                ? "%s uses magic attack with power %d".formatted(attacker, attack.power())
                : "%s attacks with power %d".formatted(attacker, attack.power()));

        int injury = defender.defend(attack.power());
        String defenseMessage;
        if (injury == 0) {
            defenseMessage = "%s fended off the attack".formatted(defender);
        } else if (defender.isAlive()) {
            defenseMessage = "%s takes %d damage".formatted(defender, injury);
        } else {
            defenseMessage = "%s takes %d damage and dies".formatted(defender, injury);
        }
        log(events, round, defenseMessage);
    }

    private String resolveWinner() {
        if (warriorA.isAlive() && !warriorB.isAlive()) {
            return warriorA.getName();
        }
        if (warriorB.isAlive() && !warriorA.isAlive()) {
            return warriorB.getName();
        }
        return null;
    }

    private void log(List<BattleEvent> events, int round, String message) {
        events.add(new BattleEvent(round, message, List.of(warriorA.status(), warriorB.status())));
    }
}
