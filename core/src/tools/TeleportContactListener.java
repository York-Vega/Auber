package tools;


import com.badlogic.gdx.physics.box2d.*;
import java.util.regex.Pattern;

public class TeleportContactListener implements ContactListener {

    // regex to determine whether contact object is a teleport or not
    private final String pattern = ".*teleporter.*";
    private boolean isTeleport;

    /**
     * If auber has contact with the teleporter, the auber's userData --> ready_to_teleport, update auber's position in player.update()
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        // use reg to check whether the object contacted is a teleporter or not
        isTeleport = Pattern.matches(pattern, (String) fixB.getUserData());
        if (isTeleport && (String) fixA.getBody().getUserData() == "auber")  {
            // set the player.UserData to ready_to_teleport for teleport_process
            fixA.getBody().setUserData("ready_to_teleport");
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        // use reg to check whether the object contacted is a teleporter or not
        isTeleport = Pattern.matches(pattern, (String) fixB.getUserData());
        if (isTeleport && (String) fixA.getBody().getUserData() == "ready_to_teleport") {
            // set the player.UserData to auber after the contact ended
            fixA.getBody().setUserData("auber");
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
