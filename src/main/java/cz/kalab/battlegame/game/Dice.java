package cz.kalab.battlegame.game;

import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Dice with a configurable number of faces.
 * The random generator can be injected, so battles are reproducible in tests.
 */
public class Dice {
    private final RandomGenerator random;
    private final int numberOfFaces;

    /**
     * New instance of dice with 6 faces
     */
    public Dice() {
        this(6, new Random());
    }

    /**
     * New instance of dice
     * @param numberOfFaces how many faces the dice has, must be greater than 0
     * @param random source of randomness
     */
    public Dice(int numberOfFaces, RandomGenerator random) {
        if (numberOfFaces <= 0) {
            throw new IllegalArgumentException("Number of faces must be greater than 0");
        }
        this.numberOfFaces = numberOfFaces;
        this.random = random;
    }

    public int getNumberOfFaces() {
        return numberOfFaces;
    }

    /**
     * @return random number from 1 to numberOfFaces
     */
    public int roll() {
        return random.nextInt(numberOfFaces) + 1;
    }

    @Override
    public String toString() {
        return "Dice with %d faces".formatted(numberOfFaces);
    }
}
