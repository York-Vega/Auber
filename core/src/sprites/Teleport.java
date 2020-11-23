package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Teleport that can be used in the map to teleport Auber to different rooms.
 */
public class Teleport extends InteractiveTileObject {

    /**
     * Creates a new instantiated Teleport object.

     * @param world Physics world the teleport should query
     * @param map Tiled map teleport will be placed in
     * @param bounds The bounds of where the teleport will sense Auber
     * @param name the room name of the teleport
     */
    public Teleport(World world, TiledMap map, Rectangle bounds,  String name)  {
        super(world, map, bounds);
        this.fixture.setUserData("teleporter_" + name);
        // for contact_listener
        this.fixture.getBody().setUserData("teleporter_" + name);
        // set teleport as sensor so the collision will not happen between
        // auber and teleport, but contact still be sensed
        this.fixture.setSensor(true);
    }
}
