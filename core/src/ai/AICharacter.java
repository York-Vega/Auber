package ai;

import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import map.Distance;
import map.Map;
import map.Path;
import map.Node;
import screen.Gameplay;

import com.badlogic.gdx.math.Vector2;

/**
 * AI Character object for the game.
 */
public class AICharacter extends Sprite {
    public World world;
    public Body b2body;

    private float speed; // in pixels per unit time 
    private PathFinder<Node> pathFinder;
    private Path path;
    private int pathIndex;
    
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
        this.speed = 25.0f;        
        createBody();
        AICharacter.numberOfHostiles++;

        // TEST
        this.pathFinder = new IndexedAStarPathFinder<Node>(Map.graph);
        
        int endx = (int)Gameplay.p1.b2body.getPosition().x;
        int endy = (int)Gameplay.p1.b2body.getPosition().y;
        this.goTo(endx, endy);
    }
        
    /**
    * Creates the physics bodies for the character Sprite.
    */
    public void createBody()  {
        BodyDef bdef = new BodyDef();
        bdef.position.set(this.getX() + getWidth() + 4, this.getY() + getHeight() + 4);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2 );

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
        Vector2 vel = direction.scl(dt * this.speed);
        this.b2body.applyLinearImpulse(vel, this.b2body.getWorldCenter(), true);

        // position sprite properly within the box
        this.setPosition(b2body.getPosition().x - getWidth() / 2,
                         b2body.getPosition().y - getHeight() / 2); 
    }

    /**
     * Decides the direction to be made by the AI
     * 
     * returns a unit vector representing direction
     * moves in the direction of the next node in the path
     */
    private Vector2 decideDirection() {
        if (this.isMoving()) {
            Node target = path.get(pathIndex);
            int targetY = (target.getIndex() / Map.mapTileWidth) * Map.tilePixelHeight + 16;
            int targetX = (target.getIndex() % Map.mapTileWidth) * Map.tilePixelWidth + 16;

            float x = this.b2body.getPosition().x;
            float y = this.b2body.getPosition().y;

            float xComp = 0;
            float yComp = 0;

            // if the difference in x values between character and node is above 1
            // move in x directio n towards node
            if (Math.abs(x - targetX) > 1) {
                xComp = targetX - x;
            } 
            // if the difference in y values between character and node is above 1
            // move in y directio n towards node
            if (Math.abs(y - targetY) > 1) {
                yComp = targetY - y;
            } 

            // if the character is in the bounds of the node 
            // target the next node            
            if (Math.abs(y - targetY) < 4 && Math.abs(x - targetX) < 4) {
                this.pathIndex++;
                return new Vector2(0, 0);
            }

            float abs = (float)Math.sqrt(Math.pow(xComp, 2) + Math.pow(yComp, 2)); 
            return new Vector2(xComp/abs, yComp/abs);
        } 
        return new Vector2(0, 0);
    } 
    
    /**
     * 
     * @param x x coordinate of destination in pixels
     * @param y y coordinate of destination in pixels
     * @return true if there is a path between character and destination, false otherwise
     */
    public boolean goTo(float x, float y) {
        // resets the path
        this.path = new Path();
        pathIndex = 0;

        Vector2 position = this.b2body.getPosition();

        Node startNode = Map.graph.getNodeByXY((int)position.x, (int)position.y);
        Node endNode = Map.graph.getNodeByXY((int)x, (int)y);

        // A* search between character and destination
        pathFinder.searchNodePath(startNode, endNode, new Distance(), path);

        // if the path is empty 
        if (path.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return whether the character is currently following a path
     */
    public boolean isMoving() {
        return this.pathIndex < this.path.getCount();
    }

    /**
     * stops the character from following its path
     */
    public void stop() {
        this.pathIndex = this.path.getCount();
    }
}