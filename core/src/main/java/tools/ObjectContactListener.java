package tools;

import characters.Player;
import characters.ai.Ability;
import characters.ai.Enemy;
import com.badlogic.gdx.physics.box2d.*;
import java.util.regex.Pattern;
import sprites.Door;
import sprites.Systems;


public class ObjectContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String teleportPattern = ".*teleporter.*";
    private boolean isTeleport;
    // regex to determine whether contact object is a healing pod or not
    private final String healingPattern = ".*healingPod.*";
    private boolean isHealingPod;

    private final String systemPattern = ".*system.*";
    private final String infiltratorsPattern = ".*Infiltrators.*";



    /**
     * If auber has contact with the teleport, the auber's userData to ready_to_teleport, 
     * update auber's position in player.update()
     * if enemy has contact with the systems, start sabotage process.
     * if auber has contact with healing pod, start healing process.
     * if auber has contact with enemy body and auber is not arresting another enemy and 
     * KEY A is pressed, auber to arrest enemy.
     * if auber has contact with enemy ability area and enemy is not in cool down,
     * enemy will use ability.

     * @param contact the contact that is being sensed
     */
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object contacted is a teleporter or not
        isTeleport = Pattern.matches(teleportPattern, fixB.getBody().getUserData().toString());
        // use reg to check whether the object contacted is a healpod
        isHealingPod = Pattern.matches(healingPattern, fixB.getBody().getUserData().toString());

        // only auber contact with teleport will be listened
        if (isTeleport && fixA.getBody().getUserData() == "auber")  {
            // set the player.UserData to ready_to_teleport for teleport_process
            fixA.getBody().setUserData("ready_to_teleport");
        }

        // if auber contact with healing pod and healing pod is not sabotaged
        if (isHealingPod && ((String) fixA.getBody().getUserData()).equals("auber")) {
            // set the player.UserData to ready_to_heal for healing process
            if (fixB.getBody().getUserData() == "healingPod_not_sabotaged") {
                fixA.getBody().setUserData("ready_to_heal");
            }
        }

        // Infiltrators contact
        if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {

            if (is_Infiltrators(fixA)) {
                // if fixture's userdata is Ability object, then is sensoring area
                if (fixA.getUserData() != null 
                        && Ability.class.isAssignableFrom(fixA.getUserData().getClass())) {
                    // enemy sensors auber
                    if (is_Auber(fixB)) {
                        Ability ability = (Ability) fixA.getUserData();
                        Player auber = (Player) fixB.getUserData();
                        ability.setTarget(auber);
                        ability.provokeAbility();
                    }
                } else {
                    // if contact happened between infiltrator and a system
                    if (is_System(fixB)) {
                        // only when NPC contact with the target system, sabotage process will begin
                        Enemy enemy = (Enemy) fixA.getUserData();
                        Systems targetSystem = enemy.get_target_system();
                        Systems contactSystem = (Systems) fixB.getUserData();
                        if (targetSystem == contactSystem) {
                            enemy.ability.setDisable(true);
                            enemy.currentContactSystem = contactSystem;
                            enemy.set_attackSystemMode();
                            targetSystem.set_sabotaging();
                        }
                    }
                }
            } else if (is_Infiltrators(fixB)) {
                // if fixture's userdata is Ability object, then is sensoring area
                if (fixB.getUserData() != null 
                        && Ability.class.isAssignableFrom(fixB.getUserData().getClass())) {
                    if (is_Auber(fixA)) {
                        Ability ability = (Ability) fixB.getUserData();
                        Player auber = (Player) fixA.getUserData();
                        ability.setTarget(auber);
                        ability.provokeAbility();
                        System.out.println("auber is under attack");
                    }
                } else {
                    if (is_System(fixA)) {
                        Enemy enemy = (Enemy) fixB.getUserData();
                        Systems targetSystem = enemy.get_target_system();
                        Systems contactSystem = (Systems) fixA.getUserData();
                        if (targetSystem == contactSystem) {
                            enemy.ability.setDisable(true);
                            enemy.currentContactSystem = contactSystem;
                            enemy.set_attackSystemMode();
                            targetSystem.set_sabotaging();
                        }
                    }
                }
            }
        }

        // auber arrest contact, auber can only arrest enemy if contact with its main body
        if (is_Auber(fixA) || is_Auber(fixB)) {
            // if contact happened between auber and infiltrators' body but not sensor area
            if (is_Auber(fixA) && is_Infiltrators(fixB) 
                    && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {
                Player auber = (Player) fixA.getUserData();
                Enemy enemy = (Enemy) fixB.getUserData();
                // if auber is not arresting other infiltrators, 
                // contacted infiltrators will be arrested
                if (!auber.is_arresting() && auber.not_arrested(enemy)) {
                    auber.setNearby_enemy(enemy);
                    //enemy.set_standByMode();
                    enemy.ability.setDisable(true);
                }
            } else if (is_Auber(fixB) && is_Infiltrators(fixA) 
                    && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
                Player auber = (Player) fixB.getUserData();
                Enemy enemy = (Enemy) fixA.getUserData();
                if (!auber.is_arresting() && auber.not_arrested(enemy)) {
                    auber.setNearby_enemy(enemy);
                    //enemy.set_standByMode();
                    enemy.ability.setDisable(true);
                }
            }
        }
    }


    /**
     * if auber end contact with enemy and KEY A is not pressed, arrest process will fail
     * if enemy end contact with system system's hp should be checked.

     * @param contact The contact that is ending
     */
    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // use reg to check whether the object end contact is a teleporter or not
        isTeleport = Pattern.matches(teleportPattern,  fixB.getBody().getUserData().toString());
        // use reg to check whether the object end contact is a healpod
        isHealingPod = Pattern.matches(healingPattern, fixB.getBody().getUserData().toString());

        // only auber contact with teleport will be listened
        if (isTeleport && (fixA.getBody().getUserData()).toString().equals("ready_to_teleport")) {
            // set the player.UserData to auber after the contact ended
            fixA.getBody().setUserData("auber");
        }

        // if auber end contact with healing pod, set auber's body data back to auber
        if (isHealingPod 
                && ((String) fixA.getBody().getUserData()).toString().equals("ready_to_heal")) {
            // set the player.UserData to ready_to_heal for healing process
            fixA.getBody().setUserData("auber");
        }


        // // infiltrators end contact
        if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {

            if (is_Infiltrators(fixA) && is_System(fixB) 
                    && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
                Enemy enemy = (Enemy) fixA.getUserData();
                Systems currentContactsystem = enemy.currentContactSystem;
                Systems endContactSys = (Systems) fixB.getUserData();
                // contact will be listened if enemy finished sabotaging a system 
                // and have generated next target system or enemy stop sabotaging the system
                // the end contact between enemy and system will be listened
                if (currentContactsystem == endContactSys) {
                    enemy.ability.setDisable(false);
                    float sysHp = currentContactsystem.hp;
                    if (sysHp > 1) {
                        // if system's hp > 1, set it to not sabotaged status
                        currentContactsystem.set_not_sabotaged();
                    }
                }
                // left the current contact system, should set it back to null
                currentContactsystem = null;

            } else if (is_Infiltrators(fixB) && is_System(fixA) 
                    && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {

                Enemy enemy = (Enemy) fixB.getUserData();
                Systems currentContactsystem = enemy.currentContactSystem;
                Systems endContactSys = (Systems) fixA.getUserData();
                // contact will be listened if enemy finished sabotaging a system and have
                // generated next target system or enemy stop sabotaging the system
                // the end contact between enemy and system it left will be listened
                if (currentContactsystem == endContactSys) {
                    enemy.ability.setDisable(false);
                    float sysHp = currentContactsystem.hp;
                    if (sysHp > 1) {
                        // if system's hp > 1, set it to not sabotaged status
                        currentContactsystem.set_not_sabotaged();
                    }
                }
                // left the current system, should set it back to null
                currentContactsystem = null;

            }
        }

        // end auber arrest contact
        if (is_Auber(fixA) || is_Auber(fixB)) {
            // if contact happened between auber and infiltrators' body but not sensor area
            if (is_Auber(fixA) && is_Infiltrators(fixB) 
                    && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {
                Player auber = (Player) fixA.getUserData();
                Enemy enemy = (Enemy) fixB.getUserData();
                if (!auber.arrestPressed) {
                    auber.setNearby_enemy(null);
                    enemy.ability.setDisable(false);
                }
            } else if (is_Auber(fixB) && is_Infiltrators(fixA) 
                    && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
                Player auber = (Player) fixB.getUserData();
                Enemy enemy = (Enemy) fixA.getUserData();
                if (!auber.arrestPressed) {
                    auber.setNearby_enemy(null);
                    enemy.ability.setDisable(false);
                }
            }
        }


    }

    /**
     * if the given fixture is an infiltrator.

     * @param fixture contact fixture
     * @return true if fixture is an Enemy object
     */
    public boolean is_Infiltrators(Fixture fixture) {
        return Pattern.matches(infiltratorsPattern, fixture.getBody().getUserData().toString());
    }

    /**
     * If the given fixture is a system.

     * @param fixture contact fixture
     * @return true if fixture is a System object
     */
    public boolean is_System(Fixture fixture) {
        return Pattern.matches(systemPattern,  fixture.getBody().getUserData().toString())
                || Pattern.matches(healingPattern, fixture.getBody().getUserData().toString());
    }

    /**
     * If the given fixture is a Door.

     * @param fixture contact fixture
     * @return true if fixture is a door
     */
    public boolean is_Doors(Fixture fixture) {
        return  Pattern.matches("door_.*", fixture.getUserData().toString());
    }


    /**
     * If the given fixture is a Auber.

     * @param fixture contact fixture
     * @return true if fixture is a Player object
     */
    public boolean is_Auber(Fixture fixture) {
        return fixture.getBody().getUserData().equals("auber");
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // if the character is about to come into contact with a door
        if (is_Doors(fixB) 
            && ((fixA.getBody().getUserData() == "auber") || is_Infiltrators(fixA))) {
            // gets the door
            Object data = fixB.getBody().getUserData();
            if (data instanceof Door) {
                // if the door is locked, it is collidable,
                // else it is not
                contact.setEnabled(((Door) data).isLocked());
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}