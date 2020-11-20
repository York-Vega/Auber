import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import tools.CharacterRenderer;

/**
 * Main player object for the game.
 */
public abstract class Character {
    public World world;
    public Body b2body;
    public float speed = 60f;
    
    private CharacterRenderer renderer;
    private Vector2 position;
    private Vector2 size;

    /**
     * creates an semi-initalised player the physics body is still uninitiated.

     * @param world The game world
     * 
     * @param x The inital x location of the player
     * 
     * @param y The inital y location of the player
     */
    public Character(World world, float x, float y, CharacterRenderer.Sprite sprite)  {
        this.world = world;
        position = new Vector2(x, y);
        size = new Vector2(24, 24);
        createBody();

        renderer = new CharacterRenderer(sprite);
    }

    /**
    * Creates the physics bodies for the player Sprite.
    */
    public void createBody()  {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x + size.x, position.y + size.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        fdef.shape = shape;

        b2body.setLinearDamping(20f);

        shape.dispose();
    }
    
    /**
     * Updates the player, should be called every update cycle.

     * @param delta The time in secconds since the last update
     */
    public void updatePlayer(float delta)  {
        
        // position sprite properly within the box
        this.position.set(b2body.getPosition().x - size.x / 1,
                         b2body.getPosition().y - size.y / 1 + 4);

        renderer.update(delta, b2body.linVelLoc);
    }

    public void draw(SpriteBatch batch) {
        renderer.render(position, batch);
    }

}
