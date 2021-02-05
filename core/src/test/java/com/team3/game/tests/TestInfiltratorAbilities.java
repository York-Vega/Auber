package com.team3.game.tests;

import static org.junit.Assert.assertEquals;

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
public class TestInfiltratorAbilities { 
  
  @Test
  public void testSlowDownPlayer() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    Enemy enemy = new Enemy(world, 0, 0);
    Float speed = player.speed;
    enemy.ability.slowDownPlayer(player);

    assertEquals("Error, player not slowed", speed * 0.5f, player.speed, 0.0f);
  }

  @Test
  public void testSpeeding() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Enemy enemy = new Enemy(world, 0, 0);
    Float speed = enemy.speed;
    enemy.ability.speeding(enemy);

    assertEquals("Error, enemy not sped up", speed * 3f, enemy.speed, 0.0f);
  }

  @Test
  public void testAttackPlayer() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    Enemy enemy = new Enemy(world, 0, 0);
    Float health = player.health;
    enemy.ability.attackPlayer(player);

    assertEquals("Error, enemy did not attack player", health - 10f, player.health, 0.0f);
  }

  @Test
  public void testRemoveAbility() throws Exception {
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    Enemy enemy = new Enemy(world, 0, 0);
    enemy.ability.target = player;
    enemy.ability.slowDownPlayer(player);
    enemy.ability.speeding(enemy);
    enemy.ability.removeAbility(enemy);

    assertEquals("Error, player speed not restored", 60f, player.speed, 0.0f);
    assertEquals("Error, enemy speed not restored", 1000f, enemy.speed, 0.0f);
  }
}