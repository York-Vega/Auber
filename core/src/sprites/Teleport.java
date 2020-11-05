package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Teleporter that can be used in the map to teleport Auber to different rooms.
 */
public class Teleport extends InteractiveTileObject {
    /**
     * Creates a new instantiated Teleporter object.

     * @param world Physics world the teleporter should query
     * @param map Tiled map teleporter will be placed in
     * @param bounds The bounds of where the teleporter will sense Auber
     */
    public Teleport(World world, TiledMap map, Rectangle bounds)  {
        super(world, map, bounds);
        this.fixture.setUserData("teleport"); // for contact_listener

        // set teleport as sensor so the collison will not happen between 
        // auber and teleport, but contact still be sensed
        this.fixture.setSensor(true); 
    }
}
