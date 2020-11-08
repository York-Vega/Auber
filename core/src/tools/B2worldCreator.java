package tools;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayList;
import sprites.Teleport;


/**
 * Responsible for creating all the map objects from the map file.
 */
public class B2worldCreator {

    /**
     * Creates all the intractive objects and hooks them into the world physics.

     * @param world Physics world objects should look for interactions in
     * @param map Map we should look for objects in
     */
    public static void createWorld(World world, TiledMap map)  {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Get all layers of map
        MapLayers layers = map.getLayers();

        // create the walls
        for (MapObject object : layers.get(2).getObjects().getByType(RectangleMapObject.class)) {
            // 2: is the layer id, can be seen in tield eidtor
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2,
                              rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("walls");
        }

        // create o2 tank
        for (MapObject object : layers.get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2,
                              rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("O2_tank");
        }
        // create jail


        //create teleport <- this should be interactive tiled map object
        ArrayList<String> roomname = new ArrayList<>();
        roomname.add("teleporter_control_room");
        roomname.add("teleporter_infirmary");
        roomname.add("teleporter_mess");
        roomname.add("teleporter_hangar");
        roomname.add("teleporter_reactor");
        roomname.add("teleporter_bathroom");
        int count = 0;

        for (MapObject object : layers.get(5).getObjects().getByType(RectangleMapObject.class))  {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // pass the name of the teleporter to the teleporter creator
            new Teleport(world, map, rect, roomname.get(count));
            count += 1;
        }


        // ...
    }
}
