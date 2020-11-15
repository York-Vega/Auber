package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Systems extends InteractiveTileObject{

    /**
     * Creates a new instantiated System object.
     *
     * @param world  Physics world the teleporter should query
     * @param map    Tiled map onject will be placed in
     * @param bounds The bounds of where the object will interact with entities
     */
    public Systems(World world, TiledMap map, Rectangle bounds,String name) {
        super(world, map, bounds);
        // use the fixture.userdata to store the name of the system. used for contact listener
        this.fixture.setUserData("system_"+name);
        // use the body.userdata to store the saboage status. used for sabotage process
        this.fixture.getBody().setUserData("not sabotaged");
    }
}
