package com.team3.game.tools;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.team3.game.characters.Player;
import com.team3.game.screen.actors.TeleportMenu;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Teleport process.
 */
public class TeleportProcess {

  public TeleportMenu selectedRoom;
  public Player auber;
  public HashMap<String, ArrayList<Float>> teleporterPosition;
  public HashMap<Integer, ArrayList<Float>> jailPosition;


  /**
   * Create a new instantiated teleport process.
   *
   * @param selectedRoom The selectBox object from the stage
   * @param auber The player object
   * @param map Tiled map used to get positions of teleports
   */
  public TeleportProcess(TeleportMenu selectedRoom, Player auber, TiledMap map) {
    this.selectedRoom = selectedRoom;
    this.auber = auber;
    MapLayers layers = map.getLayers();
    teleporterPosition = new HashMap<>();
    jailPosition = new HashMap<>();
    generate_position(layers);
  }

  /**
   * Store the positions of the teleports in Hashmap.

   * @param layers Layers of the tiled map
   */
  public void generate_position(MapLayers layers) {
    for (MapObject object : layers.get("teleports").getObjects()) {
      Rectangle tele = ((RectangleMapObject) object).getRectangle();
      ArrayList<Float> teleport = new ArrayList<>();
      teleport.add(tele.x);
      teleport.add(tele.y);
      teleporterPosition.put(object.getName(), teleport);
    }
    // Test, should change with proper jail name for position.
    int jailCount = 0;
    for (MapObject object : layers.get("jail").getObjects()) {
      Rectangle jail = ((RectangleMapObject) object).getRectangle();
      ArrayList<Float> jails = new ArrayList<>();
      jails.add(jail.x);
      jails.add(jail.y);
      jailPosition.put(jailCount, jails);
      jailCount++;
    }

  }

  /**
   * Validate the teleport process.
   */
  public void validate() {

    // If auber not in contact with a teleporter, the menu (selected box) should be disabled.
    if (((String) auber.b2body.getUserData()).equals("auber")) {
      selectedRoom.setDisabled(true);
    }
    // If auber's contact with teleporter detected, enable the selectBox.
    if (((String) auber.b2body.getUserData()).equals("ready_to_teleport") 
        && selectedRoom.isDisabled()) {
      selectedRoom.setDisabled(false);
    } else if ((!(selectedRoom.getSelected()).equals("Teleport") 
          && !(selectedRoom.getSelected()).equals("Jail"))
        && ((auber.b2body.getUserData()).equals("ready_to_teleport"))) {
      transform();
    } else if ((selectedRoom.getSelected()).equals("Jail") && auber.is_arresting()) {
      jail_transform();
    } else if ((selectedRoom.getSelected()).equals("Jail") && !auber.is_arresting()) {
      selectedRoom.setSelected("Teleport");
    }
  }

  /**
   * Transform auber.
   */
  public void transform() {
    // Get the room name to be teleported form the selectBox.
    String room = selectedRoom.getSelected();
    // Get the X cord from Data.teleporterPosition(HashMap<String,ArrayList>).
    float roomX = teleporterPosition.get(room).get(0);
    // Get the Y cord from Data.teleporterPosition(HashMap<String,ArrayList>).
    float roomY = teleporterPosition.get(room).get(1);
    // Transform auber to the chosen room.
    auber.b2body.setTransform(roomX, roomY, 0);
    // Set the selectBox back to Teleport and disable the selectedBox.
    selectedRoom.setSelected("Teleport");
    selectedRoom.setDisabled(true);
  }




  static int jail_index = 0;

  /**
   * Transform auber and arrested infiltrator to jail.
   */
  public void jail_transform() {

    float jailX = jailPosition.get(jail_index).get(0);
    float jailY = jailPosition.get(jail_index).get(1);
    jail_index++;
  
    auber.nearbyEnemy.b2body.setTransform(jailX, jailY, 0);
    auber.nearbyEnemy.stop();
    // Add the enemy to arrested list, shouldn't be arrested again.
    auber.arrestedEnemy.add(auber.nearbyEnemy);
    auber.arrestedCount++;
    // Remove enemy's target system if it has one.
    auber.nearbyEnemy.targetSystem = null;
    auber.nearbyEnemy = null;
    auber.arrestPressed = false;
    selectedRoom.setSelected("Teleport");
    selectedRoom.setDisabled(true);
  }

}
