package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;


/**
 * Manage powerups in the game.
 */
public class PowerupManager {


  public World world;
  public TiledMap map;

  public static ArrayList<Powerup> powerups = new ArrayList<>();
  public static ArrayList<float[]> spawnPositions = new ArrayList<>();


  /**
   * Instantiates a new powerups Manager.

   * @param world The game world
   * @param map The tiled map
   */
  public PowerupManager(World world, TiledMap map) {
    this.world = world;
    this.map = map;
    generatePowerups();
  }

  /**
   * Create powerups in box2D world.
   */
  public void generatePowerups() {
    MapLayer powerupSpawn = map.getLayers().get("powerupSpawns");
    for (MapObject object : powerupSpawn.getObjects()) {
      Rectangle point = ((RectangleMapObject) object).getRectangle();
      float [] position = new float[]{point.x, point.y};
      powerups.add(new Powerup(world, position[0], position[1], nameToEnum(object.getName())));
    }
  }

  /**
   * Converts the string value of a powerup to an enum type.
   *
   * @param name Name in form of a string
   * @return Powerup type
   */
  public Powerup.Type nameToEnum(String name) {
    switch (name) {
      case "speed":
        return Powerup.Type.SPEED;
      case "vision":
        return Powerup.Type.VISION;
      case "repair":
        return Powerup.Type.REPAIR;
      case "heal":
        return Powerup.Type.HEAL;
      case "arrest":
        return Powerup.Type.ARREST;
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Render the powerup, should be called in render loop.

   * @param batch The SpriteBatch to draw the powerup to.
   */
  public void renderPowerup(SpriteBatch batch) {
    for (Powerup powerup : powerups) {
      powerup.draw(batch);
    }
  }

  /**
   * Update powerups.

   * @param delta The time in seconds since the last update
   */
  public void updatePowerups(float delta) {

    for (Powerup powerup : powerups) {
      powerup.update(delta);
    }

  }

}
