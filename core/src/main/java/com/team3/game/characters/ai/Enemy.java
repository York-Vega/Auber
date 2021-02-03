package com.team3.game.characters.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.team3.game.screen.Gameplay;
import com.team3.game.sprites.StationSystem;

/**
 * Creates enemy and sets them to sabotage systems.
 */
public class Enemy extends AiCharacter {

  public StationSystem targetSystem;
  public StationSystem currentContactSystem; // Used for contact listener.
  public String mode;
  public Ability ability;
  public static int numberofInfiltrators;
  public boolean usingAbility; 
  /**
   * Enemy.

   * @param world The game world
   * @param x Position x
   * @param y Position y
   */
  public Enemy(World world, float x, float y) {
    super(world, x, y);
    this.destX = x;
    this.destY = y;
    numberofInfiltrators++;
    this.b2body.setUserData("Infiltrators" + numberofInfiltrators);
    ability = new Ability();
    createEdgeShape(ability);
    mode = "";
    usingAbility = false;
  }

  /**
   * Create an EdgeShape for enemy to sense auber for special ability.

   * @param ability Ability to be triggered
   */
  public void createEdgeShape(Ability ability) {

    EdgeShape sensoringArea = new EdgeShape();
    sensoringArea.set(new Vector2(64, 32), new Vector2(64, -32));
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = sensoringArea;
    fixtureDef.isSensor = true;
    // Store ability in sensor userdata to retrieve it in contactListener.
    b2body.createFixture(fixtureDef).setUserData(ability);

  }

  @Override
  public void update(float delta) {
    super.update(delta);

    ability.update(delta, this);
    if (!ability.inUse) {
      usingAbility = false;
    }
  }

  /**
   * Set sabotage system target.

   * @param system The system to target
   */
  public void set_target_system(StationSystem system) {
    targetSystem = system;
  }

  /**
   * Get the targetted system object.

   * @return The targeted system
   */
  public StationSystem get_target_system() {
    return targetSystem;
  }

  /**
   * Ability to sabotage the system.

   * @param system System object
   */
  public void sabotage(StationSystem system) {
    system.hp -= 0.05;
    if (system.hp < 0) {
      system.hp = 0;
      system.set_sabotaged();
    }
  }

  /**
   * Set enemy to attacking mode.
   */
  public void set_attackSystemMode() {
    mode = "attacking_system";
  }

  /**
   * Set enemy to standby mode.
   */
  public void set_standByMode() {
    mode = "";
  }

  /**
   * Check enemy is attacking a system or not.
   *
   * @return True if it is in attacking mode
   */
  public boolean is_attcking_mode() {
    return mode.equals("attacking_system");
  }

  /**
   * Check enemy is standby or not.
   *
   * @return True if it is in standby mode
   */
  public boolean is_standBy_mode() {
    return mode.equals("");
  }

  /**
   * Set enemy to arrested.
   */
  public void set_ArrestedMode() {
    mode = "arrested";
    speed = 3000f;
  }

  /**
   * Getter for arrested.

   * @return Bool if the enemy is arrested
   */
  public boolean isArrested() {
    return mode.equals("arrested");
  }

  @Override
  public void write(Json json) {
    super.write(json);
    json.writeValue("target_system", targetSystem.getSystemName());
    json.writeValue("mode", mode);
  }

  @Override
  public void read(Json json, JsonValue jsonData) { }
}
