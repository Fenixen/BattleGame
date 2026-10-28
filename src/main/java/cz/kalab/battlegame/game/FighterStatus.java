package cz.kalab.battlegame.game;

/**
 * Snapshot of a warrior's state at a given moment of the battle.
 * Mana values are {@code null} for warriors without magic.
 */
public record FighterStatus(String name, int health, int maxHealth, Integer mana, Integer maxMana) {
}
