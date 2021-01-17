package com.team3.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.team3.game.screen.MainMenu;

/**
 * Class to render a main screen.
 */
public class GameMain extends Game {
  SpriteBatch batch;

  @Override
  public void create() {
    batch = new SpriteBatch();
    setScreen(new MainMenu(batch));
  }

  @Override
  public void render() {
    super.render(); // Render multiple screen.
  }

  public SpriteBatch getBatch() {
    return this.batch;
  }
}
