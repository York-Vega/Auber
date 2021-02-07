package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.GdxTestRunner;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.map.Map;
import com.team3.game.sprites.StationSystem;
import com.team3.game.tools.CharacterRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestSabotage { 
  
  @Test
  public void testDealDamage() throws Exception {

    //Instantiating necessary values to test
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Enemy enemy = new Enemy(world, 0, 0);
    StationSystem system = new StationSystem(world, map, new Rectangle(0, 0, 0, 0), "healingPod");

    //action of sabotage, should result in damage being dealt once
    enemy.sabotage(system);

    //test to see if damage was dealt
    assertTrue("No damage was dealt to the system", system.hp < 100);
  }

  @Test
  public void testCompleteDestruction() throws Exception {
    
    //Instantiating necessary values to test
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Enemy enemy = new Enemy(world, 0, 0);
    StationSystem system = new StationSystem(world, map, new Rectangle(0, 0, 0, 0), "healingPod");

    //action of sabotage, should result in the system being sabotaged
    while (system.hp > 0) {
      enemy.sabotage(system);
    }

    //test to see if the system is sabotaged
    assertTrue("System is not sabotaged", system.isSabotaged());

  }
}
