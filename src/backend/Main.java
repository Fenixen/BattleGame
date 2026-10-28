package backend;

public class Main {
    static void main(String[] args) {
        Dice dice = new Dice();

        Warrior zalgoren = new Warrior("Zalgoren", 150, 40, 15, dice);
        Mag gandalf = new Mag("Gandalf", 100, 60, 10, dice, 25, 40);

        Arena arena = new Arena(zalgoren, gandalf, dice);

        arena.battle();
    }
}
