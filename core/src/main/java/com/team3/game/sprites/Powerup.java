package com.team3.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


/**
 *  Powerup that is placed onto the world, which can be picked up by the player.
 */
public class Powerup extends InteractiveTileObject {

    /**
     * Creates a new instantiated Powerup object.

     * @param world Physics world the powerup should query
     * @param map Tiled map powerup will be placed in
     * @param bounds The bounds of which the player can pickup the powerup
     * @param name The type of the powerup
     */
    public Powerup(World world, TiledMap map, Rectangle bounds, String name) {
        super(world, map, bounds);
        this.fixture.setUserData("powerup_" + name);
        // For contact listener
        this.fixture.getBody().setUserData("powerup_" + name);
        // Set powerup as sensor so the collision will not happen between
        // auber and powerup, but contact still be sensed.
        this.fixture.setSensor(true);
    }

    public void pickup() {
        System.out.println("picked up");
        this.fixture.setSensor(false);
    }

}
