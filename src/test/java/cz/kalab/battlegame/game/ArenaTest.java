package cz.kalab.battlegame.game;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class ArenaTest {

    @Test
    void strongerWarriorWins() {
        // roll 6 is greater than half of the faces, so warrior A starts
        Dice dice = new FixedDice(6);
        Warrior strong = new Warrior("Strong", 100, 100, 10, dice);
        Warrior weak = new Warrior("Weak", 50, 10, 0, dice);

        BattleReport report = new Arena(strong, weak, dice).battle();

        assertThat(report.winner()).isEqualTo("Strong");
        assertThat(report.rounds()).isEqualTo(1);
        assertThat(weak.isAlive()).isFalse();
        assertThat(report.events().getLast().message()).isEqualTo("Strong wins!");
    }

    @Test
    void endsInDrawWhenNobodyCanBreakTheDefense() {
        Dice dice = new FixedDice(1);
        Warrior a = new Warrior("Turtle A", 100, 1, 100, dice);
        Warrior b = new Warrior("Turtle B", 100, 1, 100, dice);

        BattleReport report = new Arena(a, b, dice).battle();

        assertThat(report.winner()).isNull();
        assertThat(report.rounds()).isEqualTo(Arena.MAX_ROUNDS);
    }

    @Test
    void everyEventContainsStatusOfBothFighters() {
        Dice dice = new FixedDice(6);
        Warrior a = new Warrior("A", 100, 50, 10, dice);
        Mage b = new Mage("B", 100, 20, 5, dice, 20, 40);

        BattleReport report = new Arena(a, b, dice).battle();

        assertThat(report.events()).allSatisfy(event -> assertThat(event.fighters())
                .extracting(FighterStatus::name)
                .containsExactly("A", "B"));
    }

    @RepeatedTest(20)
    void randomBattleAlwaysFinishes() {
        Dice dice = new Dice(6, new Random());
        Warrior warrior = new Warrior("Zalgoren", 150, 40, 15, dice);
        Mage mage = new Mage("Gandalf", 100, 30, 10, dice, 25, 60);

        BattleReport report = new Arena(warrior, mage, dice).battle();

        assertThat(report.rounds()).isBetween(1, Arena.MAX_ROUNDS);
        assertThat(report.winner()).isIn("Zalgoren", "Gandalf", null);
    }
}
