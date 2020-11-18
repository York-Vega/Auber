package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Systems extends InteractiveTileObject{

    public String sys_name;
    public float hp;
    /**
     * Creates a new instantiated System object.
     *
     * @param world  Physics world the teleporter should query
     * @param map    Tiled map onject will be placed in
     * @param bounds The bounds of where the object will interact with entities
     */
    public Systems(World world, TiledMap map, Rectangle bounds,String name) {
        super(world, map, bounds);
        sys_name = name;
        hp = 100;
        // use the fixture.userdata to store the system object.
        this.fixture.setUserData(this);
        // use the body.userdata to store the saboage status. used for contact listener
        this.fixture.getBody().setUserData("system_not_sabotaged");
        // check whether is a healing pod or not
        isHealing_pod(name);

    }

    public void isHealing_pod(String name){
        // if system is healingPod, set the fixture to sensor
        if (name.equals("healingPod")){
            this.fixture.getBody().setUserData("healingPod_not_sabotaged");
            this.fixture.setSensor(true);
        }
    }

    public String getSystemName(){
        return sys_name;
    }

    public String getSabotage_status(){
        return (String) this.fixture.getBody().getUserData();
    }

    public float[] getposition(){
        float[] position = new float[]{this.body.getPosition().x,this.body.getPosition().y};
        return position;
    }

    public void sabotaged(){
        this.fixture.getBody().setUserData("system_sabotaged");
    }

    public void not_sabotaged(){
        this.fixture.getBody().setUserData("system_not_sabotaged");
    }
}
