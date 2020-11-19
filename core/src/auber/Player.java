package auber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import tools.CharacterRenderer;

/**
 * Main player object for the game.
 */
public class Player {
    public World world;
    public Body b2body;
    public float health;
    public boolean ishealing;
    private float playerSpeed = 60f;
    private CharacterRenderer renderer;
    private Vector2 position;
    private Vector2 size;

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
    public Player(World world, float x, float y)  {
        this.world = world;
        this.health = 10f;
        this.ishealing = false;
        position = new Vector2(x, y);
        size = new Vector2(24, 24);
        createBody();

        renderer = new CharacterRenderer(CharacterRenderer.Sprite.AUBER);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
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

        b2body.createFixture(fdef).setUserData("auber"); // for contact listener
        b2body.setUserData("auber");
        shape.dispose();

    }
    
    /**
     * Updates the player, should be called every update cycle.

     * @param delta The time in secconds since the last update
     */
    public void updatePlayer(float delta)  {
        Vector2 input = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            input.add(-playerSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            input.add(playerSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            input.add(0, playerSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            input.add(0, -playerSpeed);
        }
        b2body.applyLinearImpulse(input, b2body.getWorldCenter(), true);

        // position sprite properly within the box
        this.setPosition(b2body.getPosition().x - size.x / 1,
                         b2body.getPosition().y - size.y / 1 + 4);

        // should be called each loop of rendering
        healing(delta);

        renderer.update(delta, input);
    }

    /**
     *
     * @param isheal set ishealing to true or false
     */
    public void setHealing(boolean isheal) {
        ishealing = isheal;
    }

    /**
     * Healing auber
     */
    public void healing(float delta) {
        // healing should end or not start if auber left healing pod or not contact with healing pod
        if (b2body.getUserData() == "auber"){
            setHealing(false);
            return;
        }
        // healing should start if auber in healing pod and not in full health
        if(b2body.getUserData() == "ready_to_heal" && health < 100f){
            setHealing(true);
        }
        // healing shouln't start if auber is in full health and healing should end if auber being healed to full health
        else if (b2body.getUserData() == "ready_to_heal" && health == 100f){
            setHealing(false);
            // test purpose, delet when deploy
            System.out.println("Auber is in full health, no need for healing ");
        }
        // healing process
        if(ishealing){
            // adjust healing amount accrodingly
            health += 20f * delta;
            if (health > 100f) {
                health = 100f;
            }
            // test prupose, delet when depoly
            System.out.println("Auber is healing, Auber current health: " + health);
        }

    }

    public void draw(SpriteBatch batch) {
        renderer.render(position, batch);
    }

}
