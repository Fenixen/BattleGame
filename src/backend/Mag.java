package backend;

public class Mag extends Warrior{
    private int mana;
    private int maxMana;
    private int magicDamage;

    public Mag(String name, int health, int damage, int defense, Dice dice, int mana, int magicDamage){
        super(name,health,damage,defense,dice);
        this.mana = mana;
        this.maxMana = mana;
        this.magicDamage = magicDamage;
    }

    @Override
    public void fight(Warrior rival){
        if (mana < maxMana){
            mana += 10;
            if (mana > maxMana){
                mana = maxMana;
            }
            rival.fight(this);
        } else {
            int attack = magicDamage + dice.rollDice();
            setMessage(String.format(name + " uses his magic attack for " + attack));
            rival.fightBack(attack);
            mana = 0;
        }
    }

    public String graphicalMana(){
        return graphicalIndicator(mana, maxMana);
    }
}
