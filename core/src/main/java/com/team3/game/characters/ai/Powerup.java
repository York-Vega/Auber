package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Powerup object for the game.
 */
public class Powerup {
  private static TextureAtlas atlas = new TextureAtlas("sprites/powerupSprites.atlas");

  private String[] textureNames = { "POWERUP_speed", "POWERUP_vision", "POWERUP_repair",
                                    "POWERUP_heal", "POWERUP_arrest" };
  public Sprite sprite;

  public World world;
  public Vector2 position;
  public Vector2 size;

  /**
   * Powerup types.
   */
  public enum Type {
    SPEED,
    VISION,
    REPAIR,
    HEAL,
    ARREST
  }

  public Type type;
  public Body b2body;
  public boolean hidden;

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
    this.hidden = false;
    position = new Vector2(x, y);
    size = new Vector2(24, 24);
    createBody();

    switch (type) {
      case SPEED:
        sprite = atlas.createSprite(textureNames[0]);
        break;
      case VISION:
        sprite = atlas.createSprite(textureNames[1]);
        break;
      case REPAIR:
        sprite = atlas.createSprite(textureNames[2]);
        break;
      case HEAL:
        sprite = atlas.createSprite(textureNames[3]);
        break;
      case ARREST:
        sprite = atlas.createSprite(textureNames[4]);
        break;
      default:
        throw new IllegalArgumentException("Unexpected powerup type received");
    }
  }

  /**
   * Creates the physics bodies for the powerup sprite.
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
    hidden = true;
  }

  /**
   * Draw the powerup to the provided batch.
   *
   * @param batch The spritebatch to draw to
   **/
  public void draw(SpriteBatch batch) {
    if (!hidden) {
      sprite.draw(batch);
    }
  }

  /**
   * Updates the powerup, should be called every update cycle.

   * @param delta The time in seconds since the last update
   */
  public void update(float delta) {
    sprite.setPosition(position.x - (sprite.getWidth() / 2),
                       position.y - (sprite.getHeight() / 2));
  }
}
