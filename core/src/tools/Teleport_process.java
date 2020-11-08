package tools;

import auber.Player;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import helper.Data;

/**
 * Teleport_process
 */
public class Teleport_process {

    public SelectBox<String> selected_room;
    public Player auber;
    public Data teleporter_position;

    /**
     * Create a new instantiated teleport_process
     * @param selected_room the selectBox object from the stage
     * @param auber the player object
     */
    public Teleport_process(SelectBox<String> selected_room, Player auber){
        this.selected_room = selected_room;
        this.auber = auber;
        teleporter_position = new Data();

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
        int room_X = teleporter_position.teleporter_Position.get(room).get(0);
        // get the Y cord from Data.teleporter_Position(HashMap<String,ArrayList>)
        int room_Y = teleporter_position.teleporter_Position.get(room).get(1);
        // transform auber to the chosen room
        auber.b2body.setTransform(room_X,room_Y,0);
        // set the selectBox back to Teleport and disable the selectedBox
        selected_room.setSelected("Teleport");
        selected_room.setDisabled(true);
    }

}
