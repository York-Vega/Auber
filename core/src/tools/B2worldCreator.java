package tools;

import auber.Player;

import characters.Player;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import map.Map;
import ai.AICharacter;
import screen.Gameplay;
import sprites.Door;
import sprites.Jail;
import sprites.Systems;
import sprites.Teleport;


/**
 * Responsible for creating all the map objects from the map file.
 */
public class B2worldCreator {

    /**
     * Creates all the intractive objects and hooks them into the world physics.
     *
     * @param world Physics world objects should look for interactions in
     * @param map   Map we should look for objects in
     * @param game  Gameplay
     */
    public static void createWorld(World world, TiledMap map, Gameplay game) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Get all layers of map
        MapLayers layers = map.getLayers();

        // create the walls
        for (MapObject object : layers.get("walls").getObjects()) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2,
                    rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("walls");
            body.setUserData("walls");
        }

        // Creates the player at the spawn point on the spawn layer of the map
        for (MapObject object : layers.get("spawn").getObjects()) {
            Rectangle point = ((RectangleMapObject) object).getRectangle();
            game.p1 = new Player(world, point.x, point.y);
            break;

        }

        //create teleport <- this is interactive tiled map object
        for (MapObject object : layers.get("teleports").getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // create a new instantiated Teleport object
            new Teleport(world, map, rect, object.getName());
        }

        // create systems <- this is interactive tiled map object
        for (MapObject object : layers.get("systems").getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // create a new instantiated System object
            // stor system object in the systems Arraylist
            game.systems.add(new Systems(world,map,rect, object.getName()));
        }

        
        // loop through each tile
        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get(0);
        for (int x = 0; x < Map.mapTileWidth; x++) {
            for (int y = 0; y < Map.mapTileHeight; y++) {
                TiledMapTileLayer.Cell current = tiles.getCell(x, y);
                if (current != null) {

                    // if the current tile is a door tile
                    if (current.getTile().getId() == 32) {
                        TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);

                        // adds a new door to the game in the correct orientation
                        // north-south 
                        if (up != null) {
                            Rectangle r = new Rectangle(x * Map.tilePixelWidth, (y-1) * Map.tilePixelHeight, Map.tilePixelWidth, Map.tilePixelHeight * 3);
                            Gameplay.doors.add(new Door(world, map, r, false));
                        // east-west    
                        } else {
                            Rectangle r = new Rectangle(x * Map.tilePixelWidth, (y-1) * Map.tilePixelHeight, Map.tilePixelWidth*3, Map.tilePixelHeight) ;
                            Gameplay.doors.add(new Door(world, map, r, false));
                        }
                    }
                }
            }
        }
        
        // create jails
        int jail_number = 0;
        for (MapObject object : layers.get("jail").getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Jail(world,map,rect,jail_number);
            System.out.println("jail is created");
            jail_number ++;
        }



    }
}
