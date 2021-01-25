package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.team3.game.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestMainMenu {

  static final String[] assets = { "skin/hudskin/comic-ui.atlas", "skin/hudskin/comic-ui.json" };

  @Test
  public void allAssetsAvailable() {
    for (String asset : assets) {
      assertTrue("Error, could not find asset " + asset,
          Gdx.files.internal("assets/" + asset).exists());
    }
  }
}
