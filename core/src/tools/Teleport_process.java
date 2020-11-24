package tools;

import characters.Player;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import screen.actors.Teleport_Menu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Teleport_process
 */
public class Teleport_process {

    public Teleport_Menu selectedRoom;
    public Player auber;
    public HashMap<String, ArrayList<Float>> teleporter_position;
    public HashMap<Integer,ArrayList<Float>> jail_position;


    /**
     * Create a new instantiated teleport_process.
     *
     * @param selected_room the selectBox object from the stage
     * @param auber the player object
     * @param map tiled map used to get positions of teleports
     */
    public Teleport_process(Teleport_Menu selected_room, Player auber, TiledMap map) {
        this.selectedRoom = selected_room;
        this.auber = auber;
        MapLayers layers = map.getLayers();
        teleporter_position = new HashMap<>();
        jail_position = new HashMap<>();
        generate_position(layers);
    }

    /**
     * store the positions of the teleports in Hashmap
     *
     * @param layers layers of the tiled map
     */
    public void generate_position(MapLayers layers) {
        for (MapObject object : layers.get("teleports").getObjects()) {
            Rectangle tele = ((RectangleMapObject) object).getRectangle();
            ArrayList<Float> teleport = new ArrayList<>();
            teleport.add(tele.x);
            teleport.add(tele.y);
            teleporter_position.put(object.getName(), teleport);
        }
        // Test , should change with proper jail name for position
        int jailCount = 0;
        for(MapObject object : layers.get("jail").getObjects()) {
            Rectangle jail = ((RectangleMapObject) object).getRectangle();
            ArrayList<Float> jails = new ArrayList<>();
            jails.add(jail.x);
            jails.add(jail.y);
            jail_position.put(jailCount, jails);
            jailCount++;
            System.out.println(jail_position);
        }

    }

    /**
     * validate the teleport process.
     */
    public void validate() {

        // if auber not in contact with a teleporter, the menu(selected box) should be disabled
        if (((String) auber.b2body.getUserData()).equals("auber")) {
            selectedRoom.setDisabled(true);
        }
        // if auber's contact with teleporter detected, enable the selectBox
        if (((String) auber.b2body.getUserData()).equals("ready_to_teleport") && selectedRoom.isDisabled()) {
            selectedRoom.setDisabled(false);
        } else if ((!(selectedRoom.getSelected()).equals("Teleport") && !((String) selectedRoom.getSelected()).equals("Jail"))
                && ((auber.b2body.getUserData()).equals("ready_to_teleport"))) {
            transform();
        } else if ((selectedRoom.getSelected()).equals("Jail") && auber.is_arresting()) {
            jail_transform();
        } else if ((selectedRoom.getSelected()).equals("Jail") && !auber.is_arresting()) {
            selectedRoom.setSelected("Teleport");
        }
    }

    /**
     * transfrom auber.
     */
    public void transform() {
        // get the room name to be teleported form the selectBox
        String room = selectedRoom.getSelected();
        // get the X cord from Data.teleporter_Position(HashMap<String,ArrayList>)
        float roomX = teleporter_position.get(room).get(0);
        // get the Y cord from Data.teleporter_Position(HashMap<String,ArrayList>)
        float roomY = teleporter_position.get(room).get(1);
        // transform auber to the chosen room
        auber.b2body.setTransform(roomX, roomY, 0);
        // set the selectBox back to Teleport and disable the selectedBox
        selectedRoom.setSelected("Teleport");
        selectedRoom.setDisabled(true);
    }




    static int jail_index = 0;

    /**
     * transform auber and arrested infiltrator to jail.
     */
    public void jail_transform() {

        float jailX = jail_position.get(jail_index).get(0);
        float jailY = jail_position.get(jail_index).get(1);
        jail_index++;
        //auber.b2body.setTransform(jail_X,jail_Y,0);
        auber.nearbyEnemy.b2body.setTransform(jailX, jailY, 0);
        auber.nearbyEnemy.stop();
        // add the enemy to arrested list, shouldn't be arrested again
        auber.arrestedEnemy.add(auber.nearbyEnemy);
        auber.arrestedCount++;
        // remove enemy's target system if it has one
        auber.nearbyEnemy.targetSystem = null;
        auber.nearbyEnemy = null;
        auber.arrest_pressed = false;
        selectedRoom.setSelected("Teleport");
        selectedRoom.setDisabled(true);
    }

}
