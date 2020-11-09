package auber;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Main player object for the game.
 */
public class Player extends Sprite {
    public World world;
    public Body b2body;

    /**
     * creates an semi-initalised player the physics body is still uninitiated.

     * @param world The game world
     * 
     * @param name The name of the sprite
     * 
     * @param x The inital x location of the player
     * 
     * @param y The inital y location of the player
     */
    public Player(World world, String name, float x, float y)  {
        super(new Texture(name));
        this.world = world;
        setPosition(x, y);
        createBody();
    }
        
    /**
    * Creates the physics bodies for the player Sprite.
    */
    public void createBody()  {
        BodyDef bdef = new BodyDef();
        bdef.position.set(this.getX() + getWidth() + 4, this.getY() + getHeight() + 4);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData("auber"); // for contact listener

        shape.dispose();

    }
    

    /**
     * Updates the player, should be called every update cycle.

     * @param dt The time in secconds since the last update
     */
    public void updatePlayer(float dt)  {

        // position sprite properly within the box
        this.setPosition(b2body.getPosition().x - getWidth() / 2,
                         b2body.getPosition().y - getHeight() / 2); 

    }


}
