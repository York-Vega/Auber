package com.team3.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.team3.game.tools.CharacterRenderer;

/**
 * Main player object for the game.
 */
public abstract class Character implements Serializable {
  public World world;
  public Body b2body;
  public float speed = 60f;

  protected CharacterRenderer renderer;
  public Vector2 position;
  public Vector2 size;

  /**
   * Creates an semi-initialized player the physics body is still uninitiated.

   * @param world The game world
   * 
   * @param x The initial x location of the player
   * 
   * @param y The initial y location of the player
   * 
   * @param sprite The sprite the character should look like
   */
  public Character(World world, float x, float y, CharacterRenderer.Sprite sprite) {
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
    b2body.createFixture(fdef).setUserData(this);

    shape.dispose();
  }

  /**
   * Updates the player, should be called every update cycle.

   * @param delta The time in seconds since the last update
   */
  public abstract void update(float delta);

  public void draw(SpriteBatch batch) {
    renderer.render(position, batch);
  }

  @Override
  public void write(Json json) {
    json.writeValue("speed", speed);
    json.writeValue("position", position);
    json.writeValue("size", size);
  }

  @Override
  public void read(Json json, JsonValue jsonData) { }
}
