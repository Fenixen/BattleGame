package cz.kalab.battlegame.game;

import java.util.List;

/**
 * One line of the battle log together with the state of both warriors after it happened.
 */
public record BattleEvent(int round, String message, List<FighterStatus> fighters) {
}
