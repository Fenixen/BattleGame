package cz.kalab.battlegame.battle;

import cz.kalab.battlegame.game.BattleEvent;

import java.util.List;

/**
 * Result of a freshly fought battle, including the full battle log.
 */
public record BattleResponse(Long id, String winner, int rounds, List<BattleEvent> events) {
}
