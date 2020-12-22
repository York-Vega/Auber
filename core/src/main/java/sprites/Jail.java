package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Jail extends InteractiveTileObject {

    /**
     * Generates a Jail.

     * @param world Physics world to 
     * @param map The tiled map object will be placed in
     * @param bounds The position of the jail
     * @param number The jail id
     */
    public Jail(World world, TiledMap map, Rectangle bounds, int number) {
        super(world, map, bounds);
        this.fixture.setUserData("jail" + number);
        this.fixture.getBody().setUserData("jail" + number);
        this.fixture.setSensor(true);
    }
}
