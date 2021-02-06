package com.team3.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.team3.game.characters.Player;
import com.team3.game.characters.ai.Ability;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.characters.ai.Powerup;
import com.team3.game.sprites.Door;
import com.team3.game.sprites.StationSystem;
import java.util.regex.Pattern;

/**
 * Determines outcomes when different objects come in contact.
 */
public class ObjectContactListener implements ContactListener {

  // Regex to determine whether contact object is a teleport or not.
  private final String teleportPattern = ".*teleporter.*";
  private boolean isTeleport;

  private final String powerupPattern = ".*POWERUP.*";
  private final String systemPattern = ".*system.*";
  private final String infiltratorsPattern = ".*Infiltrators.*";

  // Regex to determine whether contact object is a healing pod or not.
  private final String healingPattern = ".*healingPod.*";
  private boolean isHealingPod;

  /**
   * If auber has contact with the teleport, the auber's userData to ready_to_teleport, 
   * update auber's position in player.update().
   * If enemy has contact with the systems, start sabotage process.
   * If auber has contact with healing pod, start healing process.
   * If auber has contact with enemy body and auber is not arresting another enemy and 
   * KEY A is pressed, auber to arrest enemy.
   * If auber has contact with enemy ability area and enemy is not in cool down,
   * enemy will use ability.

   * @param contact The contact that is being sensed
   */
  @Override
  public void beginContact(Contact contact) {

    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    // Use reg to check whether the object contacted is a teleporter or not.
    isTeleport = Pattern.matches(teleportPattern, fixB.getBody().getUserData().toString());
    // Use reg to check whether the object contacted is a healpod.
    isHealingPod = Pattern.matches(healingPattern, fixB.getBody().getUserData().toString());

    // Only auber contact with teleport will be listened.
    if (isTeleport && fixA.getBody().getUserData() == "auber")  {

      // Set the player.UserData to ready_to_teleport for teleport process.
      fixA.getBody().setUserData("ready_to_teleport");
    }

    // If auber contact with healing pod and healing pod is not sabotaged.
    if (isHealingPod && ((String) fixA.getBody().getUserData()).equals("auber")) {
      // Set the player.UserData to ready_to_heal for healing process.
      if (fixB.getBody().getUserData() == "healingPod_not_sabotaged") {
        fixA.getBody().setUserData("ready_to_heal");
      }
    }

    // Infiltrators contact.
    if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {
      if (is_Infiltrators(fixA)) {
        // If fixture's userdata is Ability object, then is sensoring area.
        if (fixA.getUserData() != null 
            && Ability.class.isAssignableFrom(fixA.getUserData().getClass())) {
          // Enemy sensors auber.
          if (is_Auber(fixB)) {
            Ability ability = (Ability) fixA.getUserData();
            Player auber = (Player) fixB.getUserData();
            ability.setTarget(auber);
            ability.provokeAbility();
          }
        } else {
          // If contact happened between infiltrator and a system.
          if (is_System(fixB)) {
            // Only when NPC contact with the target system, sabotage process will begin.
            Enemy enemy = (Enemy) fixA.getUserData();
            StationSystem targetSystem = enemy.getTargetSystem();
            StationSystem contactSystem = (StationSystem) fixB.getUserData();
            if (targetSystem == contactSystem) {
              enemy.ability.setDisable(true);
              enemy.currentContactSystem = contactSystem;
              enemy.setAttackingSystemMode();
              targetSystem.set_sabotaging();
            }
          }
        }
      } else if (is_Infiltrators(fixB)) {
        // If fixture's userdata is Ability object, then is sensoring area.
        if (fixB.getUserData() != null 
            && Ability.class.isAssignableFrom(fixB.getUserData().getClass())) {
          if (is_Auber(fixA)) {
            Ability ability = (Ability) fixB.getUserData();
            Player auber = (Player) fixA.getUserData();
            ability.setTarget(auber);
            ability.provokeAbility();
          }
        } else {
          if (is_System(fixA)) {
            Enemy enemy = (Enemy) fixB.getUserData();
            StationSystem targetSystem = enemy.getTargetSystem();
            StationSystem contactSystem = (StationSystem) fixA.getUserData();
            if (targetSystem == contactSystem) {
              enemy.ability.setDisable(true);
              enemy.currentContactSystem = contactSystem;
              enemy.setAttackingSystemMode();
              targetSystem.set_sabotaging();
            }
          }
        }
      }
    }

    // Auber arrest contact, auber can only arrest enemy if contact with its main body.
    if (is_Auber(fixA) || is_Auber(fixB)) {
      // If contact happened between auber and infiltrators' body but not sensor area.
      if (is_Auber(fixA) && is_Infiltrators(fixB) 
          && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {
        Player auber = (Player) fixA.getUserData();
        Enemy enemy = (Enemy) fixB.getUserData();
        // If auber is not arresting other infiltrators, 
        // contacted infiltrators will be arrested.
        if (!auber.is_arresting() && auber.not_arrested(enemy)) {
          auber.setNearbyEnemy(enemy);
          enemy.ability.setDisable(true);
        }
      } else if (is_Auber(fixB) && is_Infiltrators(fixA) 
          && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
        Player auber = (Player) fixB.getUserData();
        Enemy enemy = (Enemy) fixA.getUserData();
        if (!auber.is_arresting() && auber.not_arrested(enemy)) {
          auber.setNearbyEnemy(enemy);
          enemy.ability.setDisable(true);
        }
      }
    }

    // Powerup contact
    if (is_Auber(fixA) || is_Auber(fixB)) {
      // If contact happened between auber and powerup but not sensor area.
      if (is_Auber(fixA) && is_Powerup(fixB)
              && Powerup.class.isAssignableFrom(fixB.getUserData().getClass())) {
        Player auber = (Player) fixA.getUserData();
        Powerup prp = (Powerup) fixB.getUserData();
        if (!prp.hidden && auber.powerup == null) {
          prp.pickup();
          auber.setPowerup(prp.type);
        }
      } else if (is_Auber(fixB) && is_Powerup(fixA)
              && Powerup.class.isAssignableFrom(fixA.getUserData().getClass())) {
        Player auber = (Player) fixB.getUserData();
        Powerup prp = (Powerup) fixA.getUserData();
        if (!prp.hidden && auber.powerup == null) {
          prp.pickup();
          auber.setPowerup(prp.type);
        }
      }
    }
  }


  /**
   * If auber end contact with enemy and KEY A is not pressed, arrest process will fail
   * if enemy end contact with system system's hp should be checked.

   * @param contact The contact that is ending
   */
  @Override
  public void endContact(Contact contact) {

    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    // Use reg to check whether the object end contact is a teleporter or not.
    isTeleport = Pattern.matches(teleportPattern,  fixB.getBody().getUserData().toString());
    // Use reg to check whether the object end contact is a healpod.
    isHealingPod = Pattern.matches(healingPattern, fixB.getBody().getUserData().toString());

    // Only auber contact with teleport will be listened.
    if (isTeleport && (fixA.getBody().getUserData()).toString().equals("ready_to_teleport")) {
      // Set the player.UserData to auber after the contact ended.
      fixA.getBody().setUserData("auber");
    }

    // If auber end contact with healing pod, set auber's body data back to auber.
    if (isHealingPod 
        && ((String) fixA.getBody().getUserData()).toString().equals("ready_to_heal")) {
      // Set the player.UserData to ready_to_heal for healing process.
      fixA.getBody().setUserData("auber");
    }


    // Infiltrators end contact.
    if (is_Infiltrators(fixA) || is_Infiltrators(fixB))  {

      if (is_Infiltrators(fixA) && is_System(fixB) 
          && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
        Enemy enemy = (Enemy) fixA.getUserData();
        StationSystem currentContactsystem = enemy.currentContactSystem;
        StationSystem endContactSys = (StationSystem) fixB.getUserData();
        // Contact will be listened if enemy finished sabotaging a system 
        // and have generated next target system or enemy stop sabotaging the system,
        // the end contact between enemy and system will be listened.
        if (currentContactsystem == endContactSys) {
          enemy.ability.setDisable(false);
          float sysHp = currentContactsystem.hp;
          if (sysHp > 1) {
            // If system's hp > 1, set it to not sabotaged status.
            currentContactsystem.set_not_sabotaged();
          }
        }
        // Left the current contact system, should set it back to null.
        currentContactsystem = null;

      } else if (is_Infiltrators(fixB) && is_System(fixA) 
          && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {

        Enemy enemy = (Enemy) fixB.getUserData();
        StationSystem currentContactsystem = enemy.currentContactSystem;
        StationSystem endContactSys = (StationSystem) fixA.getUserData();
        // Contact will be listened if enemy finished sabotaging a system and have
        // generated next target system or enemy stop sabotaging the system,
        // the end contact between enemy and system it left will be listened.
        if (currentContactsystem == endContactSys) {
          enemy.ability.setDisable(false);
          float sysHp = currentContactsystem.hp;
          if (sysHp > 1) {
            // If system's hp > 1, set it to not sabotaged status.
            currentContactsystem.set_not_sabotaged();
          }
        }
        // Left the current system, should set it back to null.
        currentContactsystem = null;

      }
    }

    // End auber arrest contact.
    if (is_Auber(fixA) || is_Auber(fixB)) {
      // If contact happened between auber and infiltrators' body but not sensor area.
      if (is_Auber(fixA) && is_Infiltrators(fixB)
          && Enemy.class.isAssignableFrom(fixB.getUserData().getClass())) {
        Player auber = (Player) fixA.getUserData();
        Enemy enemy = (Enemy) fixB.getUserData();
        if (!auber.arrestPressed) {
          auber.setNearbyEnemy(null);
          enemy.ability.setDisable(false);
        }
      } else if (is_Auber(fixB) && is_Infiltrators(fixA)
          && Enemy.class.isAssignableFrom(fixA.getUserData().getClass())) {
        Player auber = (Player) fixB.getUserData();
        Enemy enemy = (Enemy) fixA.getUserData();
        if (!auber.arrestPressed) {
          auber.setNearbyEnemy(null);
          enemy.ability.setDisable(false);
        }
      }
    }
  }

  /**
   * If the given fixture is a powerup.

   * @param fixture Contact fixture
   * @return True if fixture is an Powerup object
   */
  public boolean is_Powerup(Fixture fixture) {
    return Pattern.matches(powerupPattern, fixture.getBody().getUserData().toString());
  }

  /**
   * If the given fixture is an infiltrator.

   * @param fixture Contact fixture
   * @return True if fixture is an Enemy object
   */
  public boolean is_Infiltrators(Fixture fixture) {
    return Pattern.matches(infiltratorsPattern, fixture.getBody().getUserData().toString());
  }

  /**
   * If the given fixture is a system.

   * @param fixture Contact fixture
   * @return True if fixture is a System object
   */
  public boolean is_System(Fixture fixture) {
    return Pattern.matches(systemPattern,  fixture.getBody().getUserData().toString())
      || Pattern.matches(healingPattern, fixture.getBody().getUserData().toString());
  }

  /**
   * If the given fixture is a Door.

   * @param fixture Contact fixture
   * @return True if fixture is a door
   */
  public boolean is_Doors(Fixture fixture) {
    return  Pattern.matches("door_.*", fixture.getUserData().toString());
  }


  /**
   * If the given fixture is a Auber.

   * @param fixture Contact fixture
   * @return True if fixture is a Player object
   */
  public boolean is_Auber(Fixture fixture) {
    return fixture.getBody().getUserData().equals("auber");
  }


  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    // If the character is about to come into contact with a door.
    if (is_Doors(fixB) 
        && ((fixA.getBody().getUserData() == "auber") || is_Infiltrators(fixA))) {
      // gets the door
      Object data = fixB.getBody().getUserData();
      if (data instanceof Door) {
        // If the door is locked, it is collidable,
        // else it is not.
        contact.setEnabled(((Door) data).isLocked());
      }
    }
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) { }
}
