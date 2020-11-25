package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Systems extends InteractiveTileObject {

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
    public Systems(World world, TiledMap map, Rectangle bounds, String name) {
        super(world, map, bounds);
        sysName = name;
        hp = 100;
        // use the fixture.userdata to store the system object.
        this.fixture.setUserData(this);
        // use the body.userdata to store the saboage status. used for contact listener
        this.fixture.getBody().setUserData("system_not_sabotaged");
        this.fixture.setSensor(true);
        // check whether is a healing pod or not
        isHealing_pod(name);
        isDoors(name);

    }

    /**
     * If the system is a healing pod userdata accordingly.

     * @param name The name of the system
     */
    public void isHealing_pod(String name) {
        // if system is healingPod, set the fixture to sensor
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
        // if system is healingPod, set the fixture to sensor
        if (name.equals("doors")) {
            this.fixture.getBody().setUserData("doors_not_sabotaged");
            this.fixture.setSensor(true);
        }
    }

    public String getSystemName() {
        return sysName;
    }

    /**
     *  sabotage status.
     *
     * @return sabotage status
     */
    public String getSabotage_status() {
        return (String) this.body.getUserData();
    }

    public float[] getposition() {
        return new float[]{this.body.getPosition().x, this.body.getPosition().y};
    }

    /**
     * set system to sabotaged.
     */
    public void set_sabotaged() {
        body.setUserData("system_sabotaged");
    }

    /**
     * set system to not sabotaged.
     */
    public void set_not_sabotaged() {
        body.setUserData("system_not_sabotaged");
    }

    /**
     * set system to sabotaging.
     */
    public void set_sabotaging() {
        body.setUserData("system_sabotaging");
    }

    /**
     * check system is sabotaged or not.
     *
     * @return true if system is sabotaged
     */
    public boolean is_sabotaged() {
        return body.getUserData().equals("system_sabotaged");
    }

    /**
     * check system is under sabotaging or not.
     *
     * @return return true if is sabotaging
     */
    public boolean is_sabotaging() {
        return body.getUserData().equals("system_sabotaging");
    }

    /**
     * check system is not sabotaged or not.
     *
     * @return true if system is not sabotaged and not sabotaging
     */
    public boolean is_not_sabotaged() {
        return body.getUserData().equals("system_not_sabotaged");
    }
}
