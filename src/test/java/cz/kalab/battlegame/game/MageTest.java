package cz.kalab.battlegame.game;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MageTest {

    private final Dice dice = new FixedDice(1);

    @Test
    void usesMagicAttackWhenManaIsFullAndThenRegenerates() {
        Mage mage = new Mage("Gandalf", 100, 30, 10, dice, 20, 60);

        assertThat(mage.attack()).isEqualTo(new Attack(61, true));
        assertThat(mage.getMana()).isZero();

        assertThat(mage.attack()).isEqualTo(new Attack(31, false));
        assertThat(mage.getMana()).isEqualTo(Mage.MANA_REGENERATION);

        mage.attack();
        assertThat(mage.attack().magic()).isTrue();
    }

    @Test
    void statusContainsMana() {
        Mage mage = new Mage("Gandalf", 100, 30, 10, dice, 20, 60);

        assertThat(mage.status()).isEqualTo(new FighterStatus("Gandalf", 100, 100, 20, 20));
    }
}
