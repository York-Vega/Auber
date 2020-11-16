package tools;

import auber.Player;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import screen.actors.Teleport_Menu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Teleport_process
 */
public class Teleport_process {

    public Teleport_Menu selected_room;
    public Player auber;
    public HashMap<String, ArrayList<Float>> teleporter_position;


    /**
     * Create a new instantiated teleport_process
     * @param selected_room the selectBox object from the stage
     * @param auber the player object
     * @param map tiled map used to get positions of teleports
     */
    public Teleport_process(Teleport_Menu selected_room, Player auber,TiledMap map){
        this.selected_room = selected_room;
        this.auber = auber;
        MapLayers layers = map.getLayers();
        teleporter_position = new HashMap<>();
        generate_position(layers);
    }

    /**
     * store the positions of the teleports in Hashmap
     *
     * @param layers layers of the tiled map
     */
    public void generate_position(MapLayers layers){
        for (MapObject object: layers.get("teleports").getObjects()){
            Rectangle tele = ((RectangleMapObject) object).getRectangle();
            ArrayList<Float> teleport = new ArrayList<>();
            teleport.add(tele.x);
            teleport.add(tele.y);
            teleporter_position.put(object.getName(),teleport);
        }
    }

    /**
     * validate the auber.body.Userdata
     */
    public void validate(){

        // if auber not in contact with a teleporter, the menu(selected box) should be disabled
        if((String) auber.b2body.getUserData() =="auber"){
            selected_room.setDisabled(true);
        }
        // if auber's contact with teleporter detected, enable the selectBox
        if((String) auber.b2body.getUserData() == "ready_to_teleport" && selected_room.isDisabled()){
            selected_room.setDisabled(false);
        }else if(((String) selected_room.getSelected() != "Teleport") && ((String) auber.b2body.getUserData() == "ready_to_teleport")){
            transform();
        }
    }
    /**
     * transfrom auber
     */
    public void transform(){
        // get the room name to be teleported form the selectBox
        String room = (String) selected_room.getSelected();
        // get the X cord from Data.teleporter_Position(HashMap<String,ArrayList>)
        float room_X = teleporter_position.get(room).get(0);
        // get the Y cord from Data.teleporter_Position(HashMap<String,ArrayList>)
        float room_Y = teleporter_position.get(room).get(1);
        // transform auber to the chosen room
        auber.b2body.setTransform(room_X,room_Y,0);
        // set the selectBox back to Teleport and disable the selectedBox
        selected_room.setSelected("Teleport");
        selected_room.setDisabled(true);

    }

}
