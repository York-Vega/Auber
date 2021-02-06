package com.team3.game.tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.sprites.StationSystem;
import java.util.ArrayList;

/**
 * Handles the lighting of the game.
 */
public class LightControl {

  public RayHandler rayHandler;

  /**
   * Creates a new instantiated light_control.

   * @param world Game world
   */
  public LightControl(World world) {
    rayHandler = new RayHandler(world);
  }

  /**
   * Update the light.

   * @param systems The list of all systems to check for sabotaged lights
   */
  public void light_update(ArrayList<StationSystem> systems) {

    if (isLightsabotaged(systems)) {
      rayHandler.setAmbientLight(.3f);
    } else {
      rayHandler.setAmbientLight(.9f);
    }
    rayHandler.update();
  }

  /**
   * If the light is sabotaged or not.
   *
   * @param systems Arraylist store all systems object
   * @return True if light system is sabotaged
   */
  public boolean isLightsabotaged(ArrayList<StationSystem> systems) {

    for (StationSystem sys : systems) {
      if (sys.sysName.equals("lights")) {
        return sys.isSabotaged();
      }
    }
    return false;
  }

}
