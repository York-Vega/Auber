package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Door class representing the activating region and physical body
 */
public class Door extends InteractiveTileObject{
    private boolean locked;
    // total number of doors in the world
    private static int noDoors = 0;
    
    /**
     * 
     * @param world the game world the door inhabits
     * @param map 
     * @param bounds bounds of the activating region
     *               this is the two neighboring tiles of the door and the door itself
     */
    public Door(World world, TiledMap map, Rectangle bounds, boolean locked) {
        super(world, map, bounds);
        
        this.bounds = bounds;
        this.locked = locked;

        this.createBody(world);
        this.fixture.setUserData("door_interact_" + Door.noDoors); // for contact_listener
        this.fixture.getBody().setUserData(this);
        Door.noDoors++;

        this.fixture.setSensor(true);
    }

    public boolean isLocked(){
        return this.locked;
    }

    public void lock() {
        this.locked = true;        
    }

    public void unlock() {
        this.locked = false;
    }

    //TODO: use with animations
    /**
     * 
     * @return true if the door has been opened, false otherwise
     */
    public boolean open() {
        if (!this.locked) {
            // open the door    
            return true;
        } else {
            return false;
        }
    }

    
    private void createBody(World world)  {
        // ensure only the door blocks and not the surrounding interactive tiles 

        BodyDef bdef = new BodyDef();

        // adjusts shape depending on what direction the door faces
        // up-down vs left-right
        int xDiv = 2;
        int yDiv = 2;
        if (this.bounds.width < this.bounds.height) {
            yDiv = 6;
        } else {
            xDiv = 6;
        }

        bdef.position.set(this.bounds.x + this.bounds.width / 2, this.bounds.y + this.bounds.height / 2);
        // physical door cannot be moved by physics 
        bdef.type = BodyDef.BodyType.StaticBody;
        this.body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        shape.setAsBox(this.bounds.width / xDiv, this.bounds.height / yDiv);

        fdef.shape = shape;

             
        this.body.createFixture(fdef).setUserData("door_body_" + Door.noDoors);// for contact listener
        this.body.setUserData(this);
        shape.dispose();
    } 
    
    public String toString() {
        return "door";
    }

}
