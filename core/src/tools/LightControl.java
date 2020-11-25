package tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import sprites.Systems;


public class LightControl {

    public RayHandler rayHandler;

    /**
     * Creates a new instantiated light_control.

     * @param world game world
     */
    public LightControl(World world) {
        rayHandler = new RayHandler(world);
    }

    /**
     * update the light.

     * @param systems the list of all systems to check for sabotaged lights
     */
    public void light_update(ArrayList<Systems> systems) {

        if (isLightsabotaged(systems)) {
            rayHandler.setAmbientLight(.3f);
        } else {
            rayHandler.setAmbientLight(.9f);
        }
        rayHandler.update();
    }

    /**
     * if the light is sabotaged or not.
     *
     * @param systems Arraylist store all systems object
     * @return true if light system is sabotaged
     */
    public boolean isLightsabotaged(ArrayList<Systems> systems) {

        for (Systems sys : systems) {
            if (sys.sysName.equals("lights")) {
                return sys.is_sabotaged();
            }
        }
        return false;
    }

}
