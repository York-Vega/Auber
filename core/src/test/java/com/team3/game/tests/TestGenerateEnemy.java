package com.team3.game.tests;

import com.team3.game.GdxTestRunner;
import com.team3.game.characters.ai.EnemyManager;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import com.team3.game.tools.CharacterRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.map.Map;
import java.util.ArrayList;
import com.team3.game.sprites.StationSystem;


@RunWith(GdxTestRunner.class)
public class TestGenerateEnemy {
    
  @Test
  public void testGenerateEnemy() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    ArrayList<StationSystem> systems = new ArrayList<>();
    EnemyManager enemyManager = new EnemyManager(world, map, systems);
    enemyManager.generate_spawn_position(map);
    enemyManager.generate_enemy(world);

    assertEquals("Error, did not generate 8 enemies", 8, EnemyManager.enemies.size());
  }
}