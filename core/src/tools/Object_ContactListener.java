package tools;


import com.badlogic.gdx.physics.box2d.*;
import sprites.Systems;

import java.util.regex.Pattern;

public class Object_ContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String pattern = ".*teleporter.*";
    private boolean isTeleport;

    private final String pattern2 = ".*system.*";
    private boolean isSystem;

    private final String pattern3 = ".*healingPod.*";
    private boolean isHealingPod;

    private final String pattern4 = ".*NPC.*";


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
        isTeleport = Pattern.matches(pattern, (CharSequence) fixB.getBody().getUserData());
        // use reg to check whether the object contacted is a healpod
        isHealingPod = Pattern.matches(pattern3,(String) fixB.getBody().getUserData());

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

        // TEST
//        if (is_NPC(fixA) && is_NPC(fixB)){
//            fixA.setSensor(true);
//            fixB.setSensor(true);
//        }

        // Sbotage contact
        if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {
            // if contact happened between NPC and a system
            if (is_Infiltrators(fixA) && is_System(fixB)){
                // only when NPC contact with the target system, sabotage process will begin
                if(fixA.getUserData().equals((Systems) fixB.getUserData())){
                    Systems sys_being_sabotaging = (Systems) fixB.getUserData();
                    fixB.getBody().setUserData("system_sabotaging");
                    String searching_mode = (String) fixA.getBody().getUserData();
                    //String sabotaging_sys = sys_being_sabotaging.sys_name;
                    String attacking_mode = searching_mode +"attack";
                    fixA.getBody().setUserData(attacking_mode);
                }

            }
            else if (is_Infiltrators(fixB) && is_System(fixA)){
                // only when NPC contact with the target system, sabotage process will begin
                if(fixB.getUserData().equals((Systems) fixA.getUserData())){
                    Systems sys_being_sabotaging = (Systems) fixA.getUserData();
                    fixA.getBody().setUserData("system_sabotaging");
                    String searching_mode = (String) fixB.getBody().getUserData();
                    String attacking_mode = searching_mode + "attack";
                    fixB.getBody().setUserData(attacking_mode);
                }

            }
        }

    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object end contact is a teleporter or not
        isTeleport = Pattern.matches(pattern, (String) fixB.getBody().getUserData());
        // use reg to check whether the object end contact is a healpod
        isHealingPod = Pattern.matches(pattern3,(String) fixB.getBody().getUserData());

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
                Systems sys = (Systems) fixB.getUserData();
                float sys_hp = sys.hp;

                String mode = (String) fixA.getBody().getUserData();
                mode = mode.replace("attack","");
                fixA.getBody().setUserData(mode);
                if(sys_hp <= 0){
                    sys.sabotaged();
                }
            }
            else if (is_Infiltrators(fixB) && is_System(fixA)){
                Systems sys = (Systems) fixA.getUserData();
                float sys_hp = sys.hp;

                String mode = (String) fixB.getBody().getUserData();
                mode = mode.replace("attack","");
                fixB.getBody().setUserData(mode);
                if(sys_hp <= 0){
                    sys.sabotaged();
                }
            }
        }

    }

    public boolean is_Infiltrators(Fixture fixture){
        return Pattern.matches(pattern4,(String) fixture.getBody().getUserData());
    }

    public boolean is_System(Fixture fixture){
        return Pattern.matches(pattern2,(String) fixture.getBody().getUserData());
    }




    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
