package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class Teleport_contact_listener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        //Gdx.app.log("beginContact", "between " + fixA.getUserData() + " and " + fixB.getUserData());
        if (fixA.getUserData() =="auber" && fixB.getUserData() =="teleport" ){
            Gdx.app.log("beginContact", "between " + fixA.getUserData() + " and " + fixB.getUserData());
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        //Gdx.app.log("endContact", "between " + fixA.getUserData() + " and " + fixB.getUserData());
        if (fixA.getUserData() =="auber" && fixB.getUserData() =="teleport" ) {
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
