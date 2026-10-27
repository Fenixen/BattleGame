package backend;

import java.util.Random;

public class Dice {
    private Random rand;
    private int numberOfFaces;

    /**
     * New Instance of dice with 6 faces
     */
    public Dice(){
        rand = new Random();
        this.numberOfFaces = 6;
    }

    /**
     * New Instance of dice
     * @param numberOfFaces How many faces will have the dice
     *                      must be greater than 0
     */
    public Dice(int numberOfFaces){
        if(numberOfFaces <= 0) {
            throw new IllegalArgumentException("Number of faces must be greater than 0");
        }
        this.numberOfFaces = numberOfFaces;
        rand = new Random();
    }

    public int getNumberOfFaces(){
        return numberOfFaces;
    }

    /**
     *
     * @return random number from 1 to numberOfFaces
     */
    public int rollDice(){
        return rand.nextInt(numberOfFaces) + 1;
    }

    /**
     *
     * @return text about dice
     */
    @Override
    public String toString(){
        return String.format("Dice with %s faces.", this.numberOfFaces);
    }
}
