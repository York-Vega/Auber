package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.tools.PowerupRenderer;

/**
 * Powerup object for the game.
 */
public class Powerup {

  private static PowerupRenderer.Sprite[] sprites = new PowerupRenderer.Sprite[] {
    PowerupRenderer.Sprite.POWERUP,
    };

  public World world;
  public PowerupRenderer renderer;
  public Vector2 position;
  public Vector2 size;

  public enum Type {
    SPEED,
    VISION,
    REPAIR,
    HEAL,
    ARREST
  }

  public Type type;
  public Body b2body;

  /**
   * Creates a powerup.

   * @param world The game world
   * @param x The initial x location of the powerup
   * @param y The initial y location of the powerup
   * @param type The type of powerup
   */
  public Powerup(World world, float x, float y, Type type) {
    this.world = world;
    this.type = type;
    position = new Vector2(x, y);
    size = new Vector2(24, 24);
    createBody();

    PowerupRenderer.Sprite toRender = sprites[0];

    renderer = new PowerupRenderer(toRender);
  }

  /**
   * Creates the physics bodies for the character Sprite.
   */
  public void createBody() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(position.x + size.x, position.y + size.y);
    bdef.type = BodyDef.BodyType.StaticBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(size.x / 2, size.y / 2);
    fdef.shape = shape;

    b2body.setLinearDamping(20f);
    b2body.createFixture(fdef).setUserData(this);
    shape.dispose();

    b2body.setUserData("POWERUP_" + this.type);
    b2body.getFixtureList().get(0).setSensor(true);
    b2body.getFixtureList().get(0).setUserData(this);
  }

  public void pickup() {
    type = "hidden";
  }

  public void draw(SpriteBatch batch) {
    renderer.render(position, batch);
  }

  /**
   * Updates the powerup, should be called every update cycle.

   * @param delta The time in seconds since the last update
   */
  public void update(float delta) {
    renderer.update(delta, type);
  }
}
