package com.team3.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.tools.CharacterRenderer;
import com.team3.game.tools.Controller;
import java.util.ArrayList;

/**
 * Main player object for the game.
 */
public class Player extends Character {
  public Enemy nearbyEnemy;
  public float health;
  public boolean ishealing;
  public boolean arrestPressed;
  public int arrestedCount = 0;
  public ArrayList<Enemy> arrestedEnemy = new ArrayList<>();


  /**
   * Creates an semi-initialized player the physics body is still uninitiated.

   * @param world The game world
   * 
   * @param x The initial x location of the player
   * 
   * @param y The initial y location of the player
   */
  public Player(World world, float x, float y)  {
    super(world, x, y, CharacterRenderer.Sprite.AUBER);
    this.health = 100f;
    this.ishealing = false;
    arrestPressed = false;

  }

  public void setPosition(float x, float y) {
    position.set(x, y);
  }

  /**
   * Creates the physics bodies for the player Sprite.
   */
  @Override
  public void createBody()  {
    super.createBody();
    b2body.setUserData("auber"); // For contact listener.
  }

  /**
   * Updates the player, should be called every update cycle.

   * @param delta The time in seconds since the last update
   */
  @Override
  public void update(float delta)  {

    Vector2 input = new Vector2(0, 0);
    if (Controller.isLeftPressed()) {
      input.add(-speed, 0);
    }
    if (Controller.isRightPressed()) {
      input.add(speed, 0);
    }
    if (Controller.isUpPressed()) {
      input.add(0, speed);
    }
    if (Controller.isDownPressed()) {
      input.add(0, -speed);
    }
    b2body.applyLinearImpulse(input, b2body.getWorldCenter(), true);

    if (Controller.isArrestPressed()) {
      arrestPressed = true;
    }

    if (nearbyEnemy != null && arrestPressed) {
      arrest(nearbyEnemy);
    }

    // Position sprite properly within the box.
    this.setPosition(b2body.getPosition().x - size.x / 1,
        b2body.getPosition().y - size.y / 1 + 6);

    // Should be called each loop of rendering.
    healing(delta);
    //powerup(delta);

    renderer.update(delta, input);
  }

  /**
   * Sets whether or not Player is currently healing.

   * @param isheal Set isHealing to true or false
   */
  public void setHealing(boolean isheal) {
    ishealing = isheal;
  }

  /**
   * Healing auber.
   *
   * @param delta The time in seconds since the last update
   */
  public void healing(float delta) {
    // Healing should end or not start if auber left healing pod or not contact with healing pod.
    if (b2body.getUserData() == "auber") {
      setHealing(false);
      return;
    }
    // Healing should start if auber in healing pod and not in full health.
    if (b2body.getUserData() == "ready_to_heal" && health < 100f) {
      setHealing(true);
    } else if (b2body.getUserData() == "ready_to_heal" && health == 100f) {
      setHealing(false);
    }
    // Healing process.
    if (ishealing) {
      // Adjust healing amount accordingly.
      health += 20f * delta;
      if (health > 100f) {
        health = 100f;
      }
    }

  }

  public void draw(SpriteBatch batch) {
    renderer.render(position, batch);
  }

  /**
   * Arrest enemy.
   *
   * @param enemy The enemy object
   */
  public void arrest(Enemy enemy) {
    // Stop enemy's sabotaging if it does.
    enemy.set_ArrestedMode();
    // Set enemy destination to auber's left,enemy should follow auber until it is in jail.
    enemy.setDest(position.x, position.y);
    enemy.moveToDest();

  }

  /**
   * Set the nearby enemy.
   *
   * @param enemy The enemy object
   */
  public void setNearby_enemy(Enemy enemy) {
    nearbyEnemy = enemy;
  }

  /**
   * If auber is arresting an enemy.
   *
   * @return True if auber is currently arresting an enemy
   */
  public boolean is_arresting() {
    return nearbyEnemy != null;
  }

  /**
   * Avoid arresting enemy already in jail twice.
   *
   * @param enemy The enemy object
   *
   * @return True if enemy is not in arrested enemy arraylist
   */
  public boolean not_arrested(Enemy enemy) {
    return !arrestedEnemy.contains(enemy);
  }

}
