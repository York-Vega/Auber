package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import java.util.regex.Pattern;

public class Teleport_contact_listener implements ContactListener {

    private String pattern = ".*teleport.*"; // regex to determine whether contact object is a teleport or not
    private boolean isTeleport ;


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        isTeleport = Pattern.matches(pattern,(String)fixB.getUserData());
        if (isTeleport == true){
            Gdx.app.log("beginContact", "between " + fixA.getUserData() + " and " + fixB.getUserData());
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        isTeleport = Pattern.matches(pattern,(String)fixB.getUserData());
        if (isTeleport == true){
            Gdx.app.log("endContact", "between " + fixA.getUserData() + " and " + fixB.getUserData());
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
