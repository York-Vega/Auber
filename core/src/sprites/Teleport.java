package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;



public class Teleport extends InteractiveTileObject {
    public Teleport(World world, TiledMap map, Rectangle bounds){
        super(world,map,bounds);
        this.fixture.setUserData("teleport"); // for contact_listener
        this.fixture.setSensor(true); // set teleport as sensor so the collison will not happen between auber and teleport, but contact still be sensored
    }
}
