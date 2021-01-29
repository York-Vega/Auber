package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.team3.game.GdxTestRunner;
import com.team3.game.characters.Player;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.map.Map;
import com.team3.game.tools.CharacterRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestArrest {
  
  
  @Test
  public void testArrest() {
    
    /*Code has error where it won't run due to being unable to load & read gdx-box2d64. 
    This is present as of the latest pull request*/
    
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    Player player = new Player(null, 0, 0);
    Enemy enemy = new Enemy(null, 0, 0);
    player.arrest(enemy);
    
    assertTrue("Test unsuccessful, enemy was not arrested", enemy.isArrested());
  }
}
