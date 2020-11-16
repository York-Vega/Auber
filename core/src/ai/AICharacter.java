package ai;

import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.box2d.*;

import map.Distance;
import map.Map;
import map.Path;
import screen.Gameplay;

import com.badlogic.gdx.math.Vector2;

/**
 * AI Character object for the game.
 */
public class AICharacter extends Sprite {
    public World world;
    public Body b2body;

    private float speed; // in pixels per unit time 
    private PathFinder<map.Node> pathFinder;
    private Path path;
    
    private static int numberOfHostiles; 

    /**
     * creates an semi-initalised AI character the physics body is still uninitiated.

     * @param world The game world
     * 
     * @param name The name of the sprite
     * 
     * @param x The inital x location of the character
     * 
     * @param y The inital y location of the character
     */
    public AICharacter(World world, String name, float x, float y)  {
        super(new Texture(name));
        this.world = world;
        setPosition(x, y);
        this.speed = 0.0f;        
        createBody();
        AICharacter.numberOfHostiles++;

        // TEST
        this.pathFinder = new IndexedAStarPathFinder<map.Node>(Map.graph);
        this.path = new Path();

        map.Node snode = Map.graph.getNodeByXY((int)x, (int)y);

        int endx = (int)Gameplay.p1.b2body.getPosition().x;
        int endy = (int)Gameplay.p1.b2body.getPosition().y;
        map.Node enode = Map.graph.getNodeByXY(endx, endy);

        pathFinder.searchNodePath(snode, enode, new Distance(), path);
    }
        
    /**
    * Creates the physics bodies for the character Sprite.
    */
    public void createBody()  {
        BodyDef bdef = new BodyDef();
        bdef.position.set(this.getX() + getWidth() / 2, this.getY() + getHeight() / 2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2 - 20);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData("NPC" + AICharacter.numberOfHostiles); // for contact listener

        shape.dispose();

    }
    

    /**
     * Updates the character, should be called every update cycle.

     * @param dt The time in secconds since the last update
     */
    public void update(float dt)  {

        Vector2 direction = this.decideDirection();
        this.move(dt, direction);

    }

    /**
     * applies the move to the character
     */
    public void move(float dt, Vector2 direction) {

        // applies a velocity of direction * time delta * speed 
        this.b2body.applyLinearImpulse(direction.scl(dt * this.speed), this.b2body.getWorldCenter(), true);
        
        // position sprite properly within the box
        this.setPosition(b2body.getPosition().x - getWidth() / 2,
                         b2body.getPosition().y - getHeight() / 2); 
    }

    // todo - implement this
    /**
     * Decides the direction to be made by the AI
     * 
     * returns a unit vector representing direction
     */
    public Vector2 decideDirection() {
        return new Vector2();
    }

    
}