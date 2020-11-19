package tools;


import ai.Enemy;
import com.badlogic.gdx.physics.box2d.*;

import screen.Gameplay;
import sprites.Door;
import sprites.Systems;

import java.util.regex.Pattern;

public class Object_ContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String pattern = ".*teleporter.*";
    private boolean isTeleport; 

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
       
        if (isTeleport && fixA.getBody().getUserData() == "auber")  {
            // set the player.UserData to ready_to_teleport for teleport_process
            fixA.getBody().setUserData("ready_to_teleport");
        }
        // if auber contact with healing pod and healing pod is not sabotaged
        if (is_System(fixB) && fixA.getBody().getUserData() == "auber"){
            // set the player.UserData to ready_to_heal for healing process
            if(fixB.getBody().getUserData() == "healingPod_not_sabotaged"){
                fixA.getBody().setUserData("ready_to_heal");
            }
        }
        // if the auber is in contact with a door
        if(is_Doors(fixB) && fixA.getBody().getUserData() == "auber") {
            System.out.println("start contact with " + fixB.getUserData().toString());

        }

        // Sbotage contact
        if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {
            // if contact happened between NPC and a system
            if (is_Infiltrators(fixA) && is_System(fixB)){
                // only when NPC contact with the target system, sabotage process will begin
                Enemy enemy = (Enemy) fixA.getUserData();
                Systems target_system = enemy.get_target_system();
                Systems contact_system = (Systems) fixB.getUserData();
                if (target_system == contact_system){
                    enemy.current_contact_system = contact_system;
                    enemy.set_attackSystemMode();
                    target_system.set_sabotaging();
                }
            }
            else if (is_Infiltrators(fixB) && is_System(fixA)){
                // only when NPC contact with the target system, sabotage process will begin
                Enemy enemy = (Enemy) fixB.getUserData();
                Systems target_system = enemy.get_target_system();
                Systems contact_system = (Systems) fixA.getUserData();
                if (target_system == contact_system){
                    enemy.current_contact_system = contact_system;
                    enemy.set_attackSystemMode();
                    target_system.set_sabotaging();
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object end contact is a teleporter or not
        isTeleport = Pattern.matches(pattern, fixB.getUserData().toString());
        
        if (isTeleport && fixA.getBody().getUserData() == "ready_to_teleport") {
            // set the player.UserData to auber after the contact ended
            fixA.getBody().setUserData("auber");
        }
        // change the fixA to enemy after enemy entity built, this is for test purpose
        if (is_System(fixB) && fixA.getBody().getUserData() == "auber")  {
            System.out.println("end contact with " + fixB.getUserData());
            System.out.println(fixB.getUserData()+ " sabotaged : " + fixB.getBody().getUserData());
        }

        // if auber end contact with the door
        if (is_Doors(fixB) && fixA.getBody().getUserData() == "auber")  {
            System.out.println("end contact with " + fixB.getUserData().toString());
        }  
        


        // // end Sbotage contact
        if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {
            // if contact end between NPC and a system
            if (is_Infiltrators(fixA) && is_System(fixB)){
                Enemy enemy = (Enemy) fixA.getUserData();
                Systems current_contact_system = enemy.current_contact_system;
                Systems endContactSys = (Systems) fixB.getUserData();
                // contact will be listened if enemy finished sabotaging a system and have generated next target system
                // or enemy stop sabotaging the system
                // the end contact between enemy and system it left will be listened
                if (current_contact_system == endContactSys){
                    float sys_hp = current_contact_system.hp;
                    if (sys_hp > 1){
                        // if system's hp > 1, set it to not sabotaged status
                        current_contact_system.not_sabotaged();
                    }
                }
                // left the current contact system, should set it back to null
                current_contact_system = null;
            }
            else if (is_Infiltrators(fixB) && is_System(fixA)){
                Enemy enemy = (Enemy) fixB.getUserData();
                Systems current_contact_system = enemy.current_contact_system;
                Systems endContactSys = (Systems) fixA.getUserData();
                // contact will be listened if enemy finished sabotaging a system and have generated next target system
                // or enemy stop sabotaging the system
                // the end contact between enemy and system it left will be listened
                if (current_contact_system == endContactSys){
                    float sys_hp = current_contact_system.hp;
                    if (sys_hp > 1){
                        // if system's hp > 1, set it to not sabotaged status
                        current_contact_system.not_sabotaged();
                    }
                }
                // left the current system, should set it back to null
                current_contact_system = null;
            }
        }   
                
    }

    public boolean is_Infiltrators(Fixture fixture){
        return Pattern.matches(".*NPC.*", fixture.getBody().getUserData().toString());
    }

    public boolean is_System(Fixture fixture){
        return Pattern.matches( ".*system.*", fixture.getBody().getUserData().toString()) ||
                Pattern.matches(".*healingPod.*", fixture.getBody().getUserData().toString());
    }

    public boolean is_Doors(Fixture fixture) {
        return  Pattern.matches("door_.*" , fixture.getUserData().toString());
    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // if the character is about to come into contact with a door
        if (is_Doors(fixB) && ((fixA.getBody().getUserData() == "auber") || is_Infiltrators(fixA)))  {
            // gets the door 
            Object data = fixB.getBody().getUserData();
            if (data instanceof Door) {            
                                
                // if the door is locked, it is collidable,
                // else it is not 
                contact.setEnabled(((Door)data).isLocked());
            }
        }   
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}
