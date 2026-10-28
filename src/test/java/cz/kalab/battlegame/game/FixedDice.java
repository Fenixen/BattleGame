package cz.kalab.battlegame.game;

import java.util.Random;

/**
 * Dice that always rolls the same value - makes battles fully predictable in tests.
 */
class FixedDice extends Dice {
    private final int value;

    FixedDice(int value) {
        super(6, new Random());
        this.value = value;
    }

    @Override
    public int roll() {
        return value;
    }
}
