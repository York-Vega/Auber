package auber;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;

public class player extends Sprite {
    public World world;
    public Body b2body;

    public player( World world,String name, float x, float y){
        super(new Texture(name));
        this.world = world;
        setPosition(x,y);
        createBody();

    }

    public void createBody(){

        BodyDef bdef = new BodyDef();
        bdef.position.set(this.getX() + getWidth()/2,this.getY() + getHeight()/2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2,getHeight()/2-20);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData("auber"); // for contact listener

        shape.dispose();

    }


    public void updatePlayer(float dt){

        this.setPosition(b2body.getPosition().x - getWidth()/2,b2body.getPosition().y-getHeight()/2); // to position sprite properly within the box

    }


}
