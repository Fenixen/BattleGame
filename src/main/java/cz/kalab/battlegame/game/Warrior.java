package cz.kalab.battlegame.game;

public class Warrior {
    private final String name;
    private final int maxHealth;
    private final int damage;
    private final int defense;
    protected final Dice dice;
    private int health;

    /**
     * New instance of Warrior
     * @param name warrior's name
     * @param health warrior's health, must be greater than 0
     * @param damage warrior's damage to other warriors
     * @param defense warrior's defense
     * @param dice rolling dice to play the game
     */
    public Warrior(String name, int health, int damage, int defense, Dice dice) {
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be greater than 0");
        }
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.defense = defense;
        this.dice = dice;
    }

    /**
     * Prepares an attack against a rival.
     * The rival decides how much of it gets through in {@link #defend(int)}.
     */
    public Attack attack() {
        return new Attack(damage + dice.roll(), false);
    }

    /**
     * Defends against an attack
     * @param attackPower power of the incoming attack
     * @return injury actually taken (0 if the attack was fended off)
     */
    public int defend(int attackPower) {
        int injury = Math.max(0, attackPower - (defense + dice.roll()));
        health = Math.max(0, health - injury);
        return injury;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public FighterStatus status() {
        return new FighterStatus(name, health, maxHealth, null, null);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return name;
    }
}
