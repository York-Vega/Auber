package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.Random;


/**
 * Manage powerups in the game.
 */
public class PowerupManager {


    public World world;
    public TiledMap map;

    // WHY IS THIS STATIC???
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
            String name = object.getName();
            float [] position = new float[]{point.x, point.y};

            Powerup powerup = new Powerup(world, position[0], position[1], name);
            powerups.add(powerup);
        }
    }

    /**
     * Render the powerup, should be called in render loop.

     * @param batch The SpriteBatch to draw the powerup to.
     */
    public void renderPowerup(SpriteBatch batch) {
        for (Powerup powerup: powerups) {
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
