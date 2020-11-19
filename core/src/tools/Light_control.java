package tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;

import java.util.ArrayList;

public class Light_control {

    public RayHandler rayHandler;

    /**
     * Creates a new instatntiated light_control.

     * @param world game world
     */
    public Light_control(World world){
        rayHandler = new RayHandler(world);
    }
    /**
     * update the light
     */
    public void light_update(ArrayList<Systems> systems){

        if(isLightsabotaged(systems)){
            rayHandler.setAmbientLight(.3f);
        }else {
            rayHandler.setAmbientLight(.9f);
        }
        rayHandler.update();
    }

    /**
     * if the light is sabotaged or not
     * @param systems
     * @return
     */
    public boolean isLightsabotaged(ArrayList<Systems> systems){
        for (Systems sys : systems){
            if (sys.sys_name.equals("lights") ){
                if (sys.is_sabotaged()){
                    return true;
                }
                return  false;
            }
        }
        return false;
    }



}
