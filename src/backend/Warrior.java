package backend;

public class Warrior {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int defense;
    protected Dice dice;
    protected String message;

    /**
     * New Instance of Warrior
     * @param name Warrior's name
     * @param health Warrior's Health
     * @param damage Warrior's damage to other Warriors
     * @param defense Warrior's defense
     * @param dice Rolling dice to play the game
     */
    public Warrior(String name, int health, int damage, int defense, Dice dice){
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.defense = defense;
        this.dice = dice;
    }

    /**
     * Fighting method
     * @param rival
     */
    public void fight(Warrior rival){
        int attack = damage + dice.rollDice();
        setMessage(String.format(name + " fighting with " + attack + " damage"));
        rival.fightBack(attack);
    }

    /**
     * Defense method
     * @param attack
     */
    public void fightBack(int attack){
        int injury = attack - (defense + dice.rollDice());
        if (injury > 0){
            health -= injury;
            message = String.format(name + " take " + injury + " damage");
            if(health <= 0){
                health = 0;
                message += " and die";
            }
        } else {
            message = String.format(name + " fended off the attack ");
        }
        setMessage(message);
    }

    /**
     *
     * @return if the Warrior is dead or not
     */
    public boolean isDead(){
        return (health < 0);
    }

    /**
     * It writes a message about fight and defense
     * @param message
     */
    protected void setMessage(String message){
        this.message = message;
    }

    public String getLastMessage(){
        return message;
    }

    /**
     * Graphical indicator of health
     * @param maxHealth
     * @param health
     * @return
     */
    protected String graphicalIndicator(int maxHealth, int health){
        String graphicalHealth = "";
        int total = 20;
        double numberOfPieces = Math.round(((double) health / maxHealth) * total);
        if(numberOfPieces == 0 && !isDead()){
            numberOfPieces = 1;
        }
        for (int i = 0; i < numberOfPieces; i++){
            graphicalHealth += "█";
        }
        for  (int i = 0; i < total - numberOfPieces; i++){
            graphicalHealth += " ";
        }
        return graphicalHealth;
    }

    /**
     *
     * @return graphical health of warrior
     */
    public String graphicalHealth(){
        return graphicalIndicator(health, maxHealth);
    }

    /**
     *
     * @return name of the Warrior
     */
    @Override
    public String toString(){
        return name;
    }
}
