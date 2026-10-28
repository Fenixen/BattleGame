package cz.kalab.battlegame.game;

/**
 * Warrior who regenerates mana every turn and casts a strong magic attack once the mana is full.
 */
public class Mage extends Warrior {
    static final int MANA_REGENERATION = 10;

    private final int maxMana;
    private final int magicDamage;
    private int mana;

    public Mage(String name, int health, int damage, int defense, Dice dice, int mana, int magicDamage) {
        super(name, health, damage, defense, dice);
        if (mana <= 0) {
            throw new IllegalArgumentException("Mana must be greater than 0");
        }
        this.mana = mana;
        this.maxMana = mana;
        this.magicDamage = magicDamage;
    }

    @Override
    public Attack attack() {
        if (mana >= maxMana) {
            mana = 0;
            return new Attack(magicDamage + dice.roll(), true);
        }
        mana = Math.min(maxMana, mana + MANA_REGENERATION);
        return super.attack();
    }

    @Override
    public FighterStatus status() {
        FighterStatus base = super.status();
        return new FighterStatus(base.name(), base.health(), base.maxHealth(), mana, maxMana);
    }

    public int getMana() {
        return mana;
    }
}
