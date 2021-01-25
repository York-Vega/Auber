package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.team3.game.characters.Player;
import com.team3.game.sprites.System;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manage enemies in the game.
 */
public class EnemyManager implements Serializable {

  public World world;
  public TiledMap map;
  public static ArrayList<Enemy> enemies = new ArrayList<>();
  public static ArrayList<float[]> spawn_position = new ArrayList<>();
  public static ArrayList<float[]> target_position = new ArrayList<>();
  public static ArrayList<System> systems = new ArrayList<>();
  public static HashMap<System, Enemy> information;

  /**
   * EnemyManager to manage enemies behavior.

   *
   * @param world Box2D world
   *
   * @param map Tiled map
   *
   * @param systems Arraylist Systems objects
   */
  public EnemyManager(World world, TiledMap map, ArrayList<System> systems) {
    this.world = world;
    this.map = map;
    EnemyManager.systems = systems;
    information = new HashMap<>();
    generate_spawn_position(map);
    generate_enemy(world);
    initial_sabotageTarget(systems);
  }

  /**
   * Generate random start position for enemies.
   *
   * @param map TiledMap object
   */
  public void generate_spawn_position(TiledMap map) {

    MapLayer enemySpawn = map.getLayers().get("npcSpawns");

    while (spawn_position.size() < 8) {
      for (MapObject object : enemySpawn.getObjects()) {
        Rectangle point = ((RectangleMapObject) object).getRectangle();
        float[] position = new float[]{point.x, point.y};
        double randomPro = Math.random();
        if (randomPro > 0.5 && !spawn_position.contains(position)) {
          spawn_position.add(position);
        }
      }
    }

  }

  /**
   * Create Enemy instance and store in Arraylist enemy.
   *
   * @param world World object
   */
  public void generate_enemy(World world) {

    for (int i = 0; i < 8; i++) {
      float[] position = spawn_position.get(i);
      // Pic needs to be changed with enemy pic.
      Enemy enemy = new Enemy(world, position[0], position[1]);
      enemies.add(enemy);

    }

  }

  /**
   * Generate 8 initial targets for enemies.
   *
   * @param systems Arraylist stores system objects
   */
  public void initial_sabotageTarget(ArrayList<System> systems) {

    ArrayList<Integer> randomIndex = new ArrayList<>();
    // Generate random target positions.
    for (int i = 0; i < 8; i++) {
      // Generate a double [0,1].
      double randomD = Math.random();
      // Generate a index [0,15].
      int index = (int) (randomD * 15);
      // Take away healing pod for initial target, for difficulty.
      while (randomIndex.contains(index) 
          && !systems.get(index).sysName.equals("headlingPod")) {
        randomD = Math.random();
        index = (int) (randomD * 15);
      }
      randomIndex.add(index);
    }

    // Set targets.
    for (int i = 0; i < randomIndex.size(); i++) {
      int index = randomIndex.get(i);
      System sys = systems.get(index);

      float endX = sys.getposition()[0];
      float endY = sys.getposition()[1];

      Enemy enemy = enemies.get(i);
      // Set the target.
      enemy.set_target_system(sys);
      // Set the destination.
      enemy.setDest(endX, endY);
      enemy.moveToDest();
      // Update the information hash table, avoid enemy targeting the same system.
      information.put(sys, enemy);

    }

  }

  /**
   * Render the enemy, should be called in gameplay render loop.
   *
   * @param batch SpriteBatch used in game
   */
  public void render_ememy(SpriteBatch batch) {

    for (Enemy enemy : enemies) {
      enemy.draw(batch);
    }

  }

  /**
   * Update the enemy, should be called in gameplay update.
   *
   * @param delta The time in seconds since the last update
   */
  public void update_enemy(float delta) {

    for (Enemy enemy : enemies) {
      if (enemy.ability.inUse && !enemy.usingAbility) {    
        Player target = enemy.ability.target;
        enemy.ability.useAbility(enemy, target);
        enemy.update(delta);
        enemy.usingAbility = true;
        continue;
      }
      if (enemy.isArrested()) {
        // If enemy have a target system.
        if (enemy.get_target_system() != null) {
          // Remove it from information for other enemies to target that system.
          if (enemy.get_target_system().is_not_sabotaged() 
              && information.containsKey(enemy.get_target_system())) {
            information.remove(enemy.get_target_system());
            enemy.targetSystem = null;
            enemy.update(delta);
            continue;
          }
          enemy.update(delta);
          continue;
        }
      } else {
        // Get targeted system object.
        System sys = enemy.get_target_system();
        // If no system left to sabotage, should start attacking auber.
        if (sys == null) {
          // Still have systems not sabotaged, should keep generating next target.
          if (information.size() < 17) {
            generateNextTarget(enemy);
            enemy.update(delta);
            if (enemy.get_target_system() == null) {
              continue;
            }
          }
          continue;
        }
        if (enemy.is_attcking_mode()) {
          enemy.sabotage(sys);
        }
        // Generate next target if system sabotaged.
        if (sys.is_sabotaged()) {
          generateNextTarget(enemy);
        }
      }
      enemy.update(delta);
    }
  }

  /**
    * If enemy successfully sabotage one target, generate next target for it.
    *
    * @param enemy Enemy object
    */
  public void generateNextTarget(Enemy enemy) {
    for (System system : systems) {
      if (!information.containsKey(system)) {
        float endx = system.getposition()[0];
        float endy = system.getposition()[1];
        enemy.setDest(endx, endy);
        enemy.set_target_system(system);
        information.put(system, enemy);
        enemy.moveToDest();
        // Set enemy back to standBy mode before it contacts with the next target system,
        // otherwise the system will lose HP before contact.
        enemy.set_standByMode();
        return;
      }
    }
    // If there is no systems left for sabotaging,
    // set enemy to standby mode and remove the target system.
    enemy.set_standByMode();
    enemy.targetSystem = null;
  }

  @Override
  public void write(Json json) {
    json.writeValue("enemies", enemies);
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    // TODO Auto-generated method stub
    
  }
}
