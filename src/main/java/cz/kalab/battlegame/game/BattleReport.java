package cz.kalab.battlegame.game;

import java.util.List;

/**
 * Complete outcome of a battle.
 * @param winner name of the winner, {@code null} when the battle ended in a draw
 */
public record BattleReport(String winner, int rounds, List<BattleEvent> events) {
}
