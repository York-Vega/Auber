package com.team3.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.team3.game.GameMain;

/**
 * New object for the demo version of the game.
 */
public class GameDemo extends Gameplay {

  public GameDemo(GameMain game) {
    super(game, new Vector2(2560, 1440), false, Difficulty.MEDIUM);
  }
}
