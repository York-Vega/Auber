package tools;


import com.badlogic.gdx.physics.box2d.*;

import screen.Gameplay;
import sprites.Door;

import java.util.regex.Pattern;

public class Object_ContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String pattern = ".*teleporter.*";
    private boolean isTeleport;

    private final String pattern2 = ".*system.*";
    private boolean isSystem;

    private final String pattern3 = ".*healingPod.*";
    private boolean isHealingPod;

    private final String pattern4 = "door_.*";
    private boolean isDoor;

    private final String pattern5 = "NPC_.*";
    private boolean isNPC;

    /**
     * If auber has contact with the teleporter, the auber's userData --> ready_to_teleport, update auber's position in player.update()
     * if enemy has contact with the systems. start sabotage process
     * if auber has contact with healing pod. start healing process
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object contacted is a teleporter or not
        isTeleport = Pattern.matches(pattern, fixB.getUserData().toString());
        // use reg to check whether the object contacted is a system or not
        isSystem = Pattern.matches(pattern2,fixB.getUserData().toString());
        // use reg to check whether the object contacted is a healpod
        isHealingPod = Pattern.matches(pattern3, fixB.getUserData().toString());
        // use reg to check whether the object contacted is a door
        isDoor = Pattern.matches(pattern4, fixB.getUserData().toString());


        if (isTeleport && fixA.getBody().getUserData() == "auber")  {
            // set the player.UserData to ready_to_teleport for teleport_process
            fixA.getBody().setUserData("ready_to_teleport");
        }
        // change the fixA to enemy after enemy entity built, this is for test purpose
        if (isSystem && fixA.getBody().getUserData() == "auber")  {

            System.out.println("start contact with " + fixB.getUserData());
            System.out.println(fixB.getUserData() + " sabotaged : " + fixB.getBody().getUserData().toString());

            // test for system status menu
            //fixB.getBody().setUserData("sabotaged");
            fixB.getBody().setUserData("sabotaging");

            /** To Do
             *  if the contact begin between enemy and systems, sabotage process should begin
             *  set enemy.userdata to sabotaging.
             *  ...
             */
        }
        // if auber contact with healing pod and healing pod is not sabotaged
        if (isHealingPod && fixA.getBody().getUserData() == "auber"){
            // set the player.UserData to ready_to_heal for healing process
            if(fixB.getBody().getUserData() == "not sabotaged"){
                fixA.getBody().setUserData("ready_to_heal");
            }
        }

        // if the auber is in contact with a door
        if(isDoor && fixA.getBody().getUserData() == "auber") {
            System.out.println("start contact with " + fixB.getUserData().toString());
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object end contact is a teleporter or not
        isTeleport = Pattern.matches(pattern, fixB.getUserData().toString());
        // use reg to check whether the object end contact is a system or not
        isSystem = Pattern.matches(pattern2, fixB.getUserData().toString());
        // use reg to check whether the object end contact is a healpod
        isHealingPod = Pattern.matches(pattern3, fixB.getUserData().toString());
        // use reg to check whether the object end contact is a door           
        isDoor = Pattern.matches(pattern4, fixB.getUserData().toString());


        if (isTeleport && fixA.getBody().getUserData() == "ready_to_teleport") {
            // set the player.UserData to auber after the contact ended
            fixA.getBody().setUserData("auber");
        }
        // change the fixA to enemy after enemy entity built, this is for test purpose
        if (isSystem && fixA.getBody().getUserData() == "auber")  {
            System.out.println("end contact with " + fixB.getUserData());
            System.out.println(fixB.getUserData()+ " sabotaged : " + fixB.getBody().getUserData());

            //test for system_status_menu
            fixB.getBody().setUserData("not sabotaged");


            /** To Do
             *  if the contact end between enemy and systems, sabotage process should stop
             *  set enemy.userdata to not_sabotage.(if contact ends, either auber is near or is being arrested
             *  if player is nearby, should start attacking auber or run away from auber)
             *  ...
             */

        }
        // if auber end contact with the healing pod
        if (isHealingPod && fixA.getBody().getUserData() == "ready_to_heal"){
            // set the player.UserData back to auber to end healing process
            fixA.getBody().setUserData("auber");
        }

        // if auber end contact with the door
        if (isDoor && fixA.getBody().getUserData() == "auber")  {
            System.out.println("end contact with " + fixB.getUserData().toString());
        }    
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // if the character is about to come into contact with a door
        isDoor = Pattern.matches(pattern4,fixB.getUserData().toString());
        if (isDoor && ((fixA.getBody().getUserData() == "auber") | Pattern.matches(pattern5,(String)fixA.getUserData())))  {
            // gets the door 
            Object data = fixB.getBody().getUserData();
            if (data instanceof Door.DoorData) {                
                Door door = Gameplay.doors.get(((Door.DoorData)data).index);
                
                // if the door is locked, it is collidable,
                // else it is not 
                contact.setEnabled(door.isLocked());
            }
        }        
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}
