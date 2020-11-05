package tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import sprites.Teleport;

import java.util.ArrayList;


public class B2worldCreator {

    public B2worldCreator(World world, TiledMap map){

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        // create the walls
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){ // 2: is the layer id, can be seen in tield eidtor
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth()/2 , rect.getY() + rect
                    .getHeight()/2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 , rect.getHeight()/2);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("walls");
        }

        // create o2 tank
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth()/2 , rect.getY() + rect
                    .getHeight()/2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 , rect.getHeight()/2);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("O2_tank");
        }
        //create jail


        //create teleport <- this should be interactive tiled map object
        ArrayList<String> teleports = new ArrayList<>();
        teleports.add("control_room");
        teleports.add("infirmary");
        teleports.add("mess");
        teleports.add("hangar");
        teleports.add("reactor");
        teleports.add("bathroom");
        int count = 0;
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Teleport(world,map,rect,teleports.get(count)); // pass the name of the teleport to the teleport creator
            count += 1;
        }


        //create helaport <- this should be interactiable

        //...
    }
}
