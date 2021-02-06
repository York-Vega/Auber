package com.team3.game.tests;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.GdxTestRunner;
import com.team3.game.characters.ai.EnemyManager;
import com.team3.game.map.Map;
import com.team3.game.sprites.StationSystem;
import com.team3.game.tools.CharacterRenderer;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class TestGenerateEnemies {
    
  @Test
  public void testGenerateEnemies() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    ArrayList<StationSystem> systems = new ArrayList<>();
    EnemyManager enemyManager = new EnemyManager(world, map, systems);
    enemyManager.generateSpawnPositions(map);
    enemyManager.generateEnemies(world);

    assertEquals("Error, did not generate 8 enemies", 8, EnemyManager.enemies.size());
  }
}
