package cz.kalab.battlegame.game;

/**
 * Result of one attack attempt.
 * @param power total attack power (before the rival's defense)
 * @param magic whether a magic attack was used
 */
public record Attack(int power, boolean magic) {
}
