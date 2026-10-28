package backend;

public class Arena {
    /**
     * First Warrior in Arena
     */
    private Warrior warriorA;
    /**
     * Second Warrior in Arena
     */
    private Warrior warriorB;

    private Dice dice;

    /**
     * New Instance of Arena
     * @param warriorA
     * @param warriorB
     * @param dice
     */
    public Arena(Warrior warriorA, Warrior warriorB, Dice dice){
        this.warriorA = warriorA;
        this.warriorB = warriorB;
        this.dice = dice;
    }

    /**
     * Simulation of Battle
     */
    public void battle(){
        Warrior warriorA = this.warriorA;
        Warrior warriorB = this.warriorB;
        IO.println("Welcome to the Battle Game!");
        IO.println(warriorA + " VS " + warriorB);
        boolean firstWarriorB = (dice.rollDice() <= dice.getNumberOfFaces() / 2);
        if (firstWarriorB){
            warriorA =  this.warriorB;
            warriorB =  this.warriorA;
        }
        IO.println(warriorA + " starting first");

        // battle
        while (warriorA.isLive() && warriorB.isLive()){
            warriorA.fight(warriorB);
            status();
            // message of fight
            printMessage(warriorA.getLastMessage());
            // message of defense
            printMessage(warriorB.getLastMessage());
            if(warriorB.isLive()){
                warriorB.fight(warriorA);
                status();
                printMessage(warriorB.getLastMessage());
                printMessage(warriorA.getLastMessage());
            }
        }
    }

    /**
     * print Warrior to console
     * @param warrior to print
     */
    private void printWarrior(Warrior warrior){
        IO.println("Warrior: " + warrior);
        IO.println("Health");
        IO.println(warrior.graphicalHealth());
        IO.println("---------");

        if(warrior instanceof Mag){
            IO.println("Mana: ");
            IO.println(((Mag)warrior).graphicalMana());
        }
    }

    /**
     * Status screen
     */
    private void status(){
        IO.println(""
                + "   __    ____  ____  _  _    __   \n"
                + "  /__\\  (  _ \\( ___)( \\( )  /__\\  \n"
                + " /(__)\\  )   / )__)  )  (  /(__)\\ \n"
                + "(__)(__)(_)\\_)(____)(_)\\_)(__)(__)\n");
        IO.println("Warriors:");
        printWarrior(warriorA);
        printWarrior(warriorB);
    }

    private void printMessage(Object message){
        IO.println(message);
        try{
            Thread.sleep(500);
        } catch (InterruptedException ex){
            IO.println("Error: " + ex.getMessage());
        }
    }
}
