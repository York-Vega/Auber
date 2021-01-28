package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.team3.game.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestAssetsAvailable {

  // Testing only the assets the are required within the game itself.
  static final String[] assets = { "skin/hudskin/comic-ui.json", "background/background.png",
    "sprites/dinoSprites.atlas", "Map/Map.tmx", "skin/hudskin/comic-ui.atlas" };

  @Test
  public void allAssetsAvailable() {
    for (String asset : assets) {
      assertTrue("Error, could not find asset " + asset,
          Gdx.files.internal("assets/" + asset).exists());
    }
  }
}
