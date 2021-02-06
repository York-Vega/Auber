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
import com.team3.game.sprites.StationSystem;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manage enemies in the game.
 */
public class EnemyManager implements Serializable {

  public World world;
  public TiledMap map;
  public static ArrayList<Enemy> enemies = new ArrayList<>();
  public static ArrayList<float[]> spawnPositions = new ArrayList<>();
  public static ArrayList<StationSystem> systems = new ArrayList<>();
  public static HashMap<StationSystem, Enemy> information;

  /**
   * EnemyManager to manage enemies behavior.

   *
   * @param world Box2D world
   *
   * @param map Tiled map
   *
   * @param systems Arraylist Systems objects
   */
  public EnemyManager(World world, TiledMap map, ArrayList<StationSystem> systems) {
    this.world = world;
    this.map = map;
    EnemyManager.systems = systems;
    information = new HashMap<>();
  }

  /**
   * Initialise random enemies with random targets.
   **/
  public void initialiseRandomEnemies() {
    generateSpawnPositions(map);
    generateEnemies(world);
    initialiseSabotageTargets(systems);
  }

  /**
   * Generate random start positions for enemies.
   *
   * @param map TiledMap object
   */
  public void generateSpawnPositions(TiledMap map) {
    MapLayer enemySpawn = map.getLayers().get("npcSpawns");

    while (spawnPositions.size() < 8) {
      for (MapObject object : enemySpawn.getObjects()) {
        Rectangle point = ((RectangleMapObject) object).getRectangle();
        float[] position = new float[]{point.x, point.y};
        double randomPro = Math.random();
        if (randomPro > 0.5 && !spawnPositions.contains(position)) {
          spawnPositions.add(position);
        }
      }
    }
  }

  /**
   * Create Enemy instance and store in Arraylist enemy.
   *
   * @param world World object
   */
  public void generateEnemies(World world) {
    for (int i = 0; i < 8; i++) {
      float[] position = spawnPositions.get(i);
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
  public void initialiseSabotageTargets(ArrayList<StationSystem> systems) {
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
      StationSystem sys = systems.get(index);

      Enemy enemy = enemies.get(i);
      // Set the target.
      enemy.setTargetSystem(sys);
      // Set the destination.
      enemy.setDest(
          sys.getPosition()[0],
          sys.getPosition()[1]);
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
  public void renderEnemy(SpriteBatch batch) {
    for (Enemy enemy : enemies) {
      enemy.draw(batch);
    }
  }

  /**
   * Arrest a random enemy, used for the arrest powerup. Returns true if arrest is performed.
   *
   * @param player The player to act upon.
   * @return true
   */
  public boolean arrestRandomEnemy(Player player) {
    for (Enemy enemy : enemies) {
      if (!enemy.isArrested()) {
        enemy.setArrestedMode();
        enemy.setDest(player.b2body.getPosition().x, player.b2body.getPosition().y);
        enemy.moveToDest();
        player.setNearbyEnemy(enemy);
        return true;
      }
    }
    return false;
  }

  /**
   * Update the enemy, should be called in gameplay update.
   *
   * @param delta The time in seconds since the last update
   */
  public void updateEnemy(float delta) {
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
        if (enemy.getTargetSystem() != null) {
          // Remove it from information for other enemies to target that system.
          if (enemy.getTargetSystem().isNotSabotaged() 
              && information.containsKey(enemy.getTargetSystem())) {
            information.remove(enemy.getTargetSystem());
            enemy.targetSystem = null;
            enemy.update(delta);
            continue;
          }
          enemy.update(delta);
          continue;
        }
      } else {
        // Get targeted system object.
        StationSystem sys = enemy.getTargetSystem();
        // If no system left to sabotage, should start attacking auber.
        if (sys == null) {
          // Still have systems not sabotaged, should keep generating next target.
          if (information.size() < 17) {
            generateNextTarget(enemy);
            enemy.update(delta);
            if (enemy.getTargetSystem() == null) {
              continue;
            }
          }
          continue;
        }
        if (enemy.isAttackingMode()) {
          enemy.sabotage(sys);
        }
        // Generate next target if system sabotaged.
        if (sys.isSabotaged()) {
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
    for (StationSystem system : systems) {
      if (!information.containsKey(system)) {
        enemy.setDest(
            system.getPosition()[0],
            system.getPosition()[1]
        );
        enemy.setTargetSystem(system);
        information.put(system, enemy);
        enemy.moveToDest();
        // Set enemy back to standBy mode before it contacts with the next target system,
        // otherwise the system will lose HP before contact.
        enemy.setStandbyMode();
        return;
      }
    }
    // If there is no systems left for sabotaging,
    // set enemy to standby mode and remove the target system.
    enemy.setStandbyMode();
    enemy.targetSystem = null;
  }

  @Override
  public void write(Json json) {
    json.writeValue("enemies", enemies);
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
  }
}
