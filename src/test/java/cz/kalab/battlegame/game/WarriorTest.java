package cz.kalab.battlegame.game;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WarriorTest {

    private final Dice dice = new FixedDice(1);

    @Test
    void attackPowerIsDamagePlusDiceRoll() {
        Warrior warrior = new Warrior("Zalgoren", 100, 40, 10, dice);

        assertThat(warrior.attack()).isEqualTo(new Attack(41, false));
    }

    @Test
    void defendSubtractsDefenseAndDiceRollFromAttack() {
        Warrior warrior = new Warrior("Zalgoren", 100, 40, 10, dice);

        int injury = warrior.defend(30);

        assertThat(injury).isEqualTo(19);
        assertThat(warrior.getHealth()).isEqualTo(81);
    }

    @Test
    void weakAttackIsFendedOff() {
        Warrior warrior = new Warrior("Zalgoren", 100, 40, 10, dice);

        assertThat(warrior.defend(5)).isZero();
        assertThat(warrior.getHealth()).isEqualTo(100);
    }

    @Test
    void warriorWithZeroHealthIsDead() {
        Warrior warrior = new Warrior("Zalgoren", 10, 40, 0, dice);

        warrior.defend(500);

        assertThat(warrior.getHealth()).isZero();
        assertThat(warrior.isAlive()).isFalse();
    }

    @Test
    void healthMustBePositive() {
        assertThatThrownBy(() -> new Warrior("Ghost", 0, 10, 10, dice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
