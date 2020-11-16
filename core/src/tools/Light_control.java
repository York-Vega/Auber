package tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;

public class Light_control {

    public RayHandler rayHandler;
    public boolean lightsabotaged;

    /**
     * Creates a new instatntiated light_control.

     * @param world game world
     */
    public Light_control(World world){
        rayHandler = new RayHandler(world);
        lightsabotaged = false;
    }
    /**
     * update the light depends on the lightsabotaged
     */
    public void light_update(){

        if(lightsabotaged == true){
            rayHandler.setAmbientLight(.3f);
        }else {
            rayHandler.setAmbientLight(.9f);
        }
        rayHandler.update();
    }

    public void setLightsabotaged(boolean sabotage){
        lightsabotaged = sabotage;
    }

    public boolean isLightsabotaged(){
        return lightsabotaged;
    }


}
