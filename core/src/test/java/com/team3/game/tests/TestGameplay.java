package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.team3.game.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestGameplay {

  static final String[] assets = { "Map/Map.tmx" };

  @Test
  public void allAssetsAvailable() {
    for (String asset : assets) {
      assertTrue("Error, could not find asset " + asset,
          Gdx.files.internal("assets/" + asset).exists());
    }
  }
}
