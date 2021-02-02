package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
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
  public void testArrestInfiltrator() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    Enemy enemy = new Enemy(world, 0, 0);
    player.arrest(enemy);
    
    assertTrue("Test unsuccessful, enemy was not arrested", enemy.isArrested());
  }
}
