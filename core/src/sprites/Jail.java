package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Jail extends InteractiveTileObject{

    /**
     *
     * @param world
     * @param map
     * @param bounds
     * @param number
     */
    public Jail(World world, TiledMap map, Rectangle bounds, int number){
        super(world,map,bounds);
        this.fixture.setUserData("jail" + number);
        this.fixture.getBody().setUserData("jail" + number);
        this.fixture.setSensor(true);
    }
}
