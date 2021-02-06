package com.team3.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Creates a system template.
 */
public class StationSystem extends InteractiveTileObject implements Serializable {

  public String sysName;
  public float hp;

  /**
   * Creates a new instantiated System object.
   *
   * @param world Physics world the teleport should query
   * @param map Tiled map object will be placed in
   * @param bounds The bounds of where the object will interact with entities
   * @param name They name of the system
   */
  public StationSystem(World world, TiledMap map, Rectangle bounds, String name) {
    super(world, map, bounds);
    sysName = name;
    hp = 100;
    // Use the fixture.userdata to store the system object.
    this.fixture.setUserData(this);
    // Use the body.userdata to store the sabotage status used for contact listener.
    this.fixture.getBody().setUserData("system_not_sabotaged");
    this.fixture.setSensor(true);
    // Check whether is a healing pod or not.
    isHealing_pod(name);
    isDoors(name);

  }

  /**
   * If the system is a healing pod userdata accordingly.

   * @param name The name of the system
   */
  public void isHealing_pod(String name) {
    // If system is healingPod, set the fixture to sensor.
    if (name.equals("healingPod")) {
      this.fixture.getBody().setUserData("healingPod_not_sabotaged");
      this.fixture.setSensor(true);
    }
  }

  /**
   * If the system is a door set userdata accordingly.

   * @param name The name of the system
   */
  public void isDoors(String name) {
    // If system is healingPod, set the fixture to sensor.
    if (name.equals("doors")) {
      this.fixture.getBody().setUserData("doors_not_sabotaged");
      this.fixture.setSensor(true);
    }
  }

  public String getSystemName() {
    return sysName;
  }

  /**
   * Sabotage status.
   *
   * @return Sabotage status
   */
  public String getSabotage_status() {
    return (String) this.body.getUserData();
  }

  public float[] getPosition() {
    return new float[] {this.body.getPosition().x, this.body.getPosition().y};
  }

  /**
   * Set system to sabotaged.
   */
  public void setSabotaged() {
    body.setUserData("system_sabotaged");
  }

  /**
   * Set system to not sabotaged.
   */
  public void set_not_sabotaged() {
    body.setUserData("system_not_sabotaged");
  }

  /**
   * Set system to sabotaging.
   */
  public void set_sabotaging() {
    body.setUserData("system_sabotaging");
  }

  /**
   * Check system is sabotaged or not.
   *
   * @return True if system is sabotaged
   */
  public boolean isSabotaged() {
    return body.getUserData().equals("system_sabotaged");
  }

  /**
   * Check system is under sabotaging or not.
   *
   * @return Return true if is sabotaging
   */
  public boolean is_sabotaging() {
    return body.getUserData().equals("system_sabotaging");
  }

  /**
   * Check system is not sabotaged or not.
   *
   * @return True if system is not sabotaged and not sabotaging
   */
  public boolean isNotSabotaged() {
    return body.getUserData().equals("system_not_sabotaged");
  }

  @Override
  public void write(Json json) {
    json.writeValue("name", sysName);
    json.writeValue("hp", hp);
  }

  @Override
  public void read(Json json, JsonValue jsonMap) {}
}
