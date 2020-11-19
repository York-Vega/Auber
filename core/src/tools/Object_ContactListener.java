package tools;


import ai.Enemy;
import auber.Player;
import com.badlogic.gdx.physics.box2d.*;
import sprites.Systems;

import java.util.regex.Pattern;

public class Object_ContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String teleport_parten = ".*teleporter.*";
    private boolean isTeleport;

    private final String system_pattern = ".*system.*";


    private final String healing_pattern = ".*healingPod.*";
    private boolean isHealingPod;

    private final String infiltrators_pattern = ".*Infiltrators.*";



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
        isTeleport = Pattern.matches(teleport_parten, (CharSequence) fixB.getBody().getUserData());
        // use reg to check whether the object contacted is a healpod
        isHealingPod = Pattern.matches(healing_pattern,(String) fixB.getBody().getUserData());

        // only auber contact with teleport will be listened
        if (isTeleport && fixA.getBody().getUserData() == "auber")  {
            // set the player.UserData to ready_to_teleport for teleport_process
            fixA.getBody().setUserData("ready_to_teleport");
        }

        // if auber contact with healing pod and healing pod is not sabotaged
        if (isHealingPod && (String) fixA.getBody().getUserData() == "auber"){
            // set the player.UserData to ready_to_heal for healing process
            if(fixB.getBody().getUserData() == "healingPod_not_sabotaged"){
                fixA.getBody().setUserData("ready_to_heal");
            }
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

        // auber arrest contact
        if (is_Auber(fixA) || is_Auber(fixB)){

            if (is_Auber(fixA) && is_Infiltrators(fixB)){

                Player auber = (Player) fixA.getUserData();
                Enemy enemy = (Enemy) fixB.getUserData();

                if (!auber.is_arresting()){
                    auber.setNearby_enemy(enemy);
                    //auber.arrest(enemy);
                }

            }
            else if (is_Auber(fixB) && is_Infiltrators(fixA)){

                Player auber = (Player) fixB.getUserData();
                Enemy enemy = (Enemy) fixA.getUserData();

                if (!auber.is_arresting()){
                    auber.setNearby_enemy(enemy);
                    //auber.arrest(enemy);
                }

            }

        }


    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object end contact is a teleporter or not
        isTeleport = Pattern.matches(teleport_parten, (String) fixB.getBody().getUserData());
        // use reg to check whether the object end contact is a healpod
        isHealingPod = Pattern.matches(healing_pattern,(String) fixB.getBody().getUserData());

        // only auber contact with teleport will be listened
        if (isTeleport && (String) fixA.getBody().getUserData() == "ready_to_teleport") {
            // set the player.UserData to auber after the contact ended
            fixA.getBody().setUserData("auber");
        }
        // if auber end contact with healing pod, set auber's body data back to auber
        if (isHealingPod && (String) fixA.getBody().getUserData() == "ready_to_heal"){
            // set the player.UserData to ready_to_heal for healing process
            fixA.getBody().setUserData("auber");
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
        return Pattern.matches(infiltrators_pattern,(String) fixture.getBody().getUserData());
    }

    public boolean is_System(Fixture fixture){
        return Pattern.matches(system_pattern,(String) fixture.getBody().getUserData()) ||
                Pattern.matches(healing_pattern,(String) fixture.getBody().getUserData());
    }

    public boolean is_Auber(Fixture fixture){
        return fixture.getBody().getUserData().equals("auber");
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
