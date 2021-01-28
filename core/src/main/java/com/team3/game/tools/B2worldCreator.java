package com.team3.game.tools;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.characters.Player;
import com.team3.game.screen.Gameplay;
import com.team3.game.sprites.Door;
import com.team3.game.sprites.Jail;
import com.team3.game.sprites.StationSystem;
import com.team3.game.sprites.Teleport;


/**
 * Responsible for creating all the map objects from the map file.
 */
public class B2worldCreator {

  /**
   * Creates all the interactive objects and hooks them into the world physics.
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

    // Get all layers of map.
    MapLayers layers = map.getLayers();

    // Create the walls.
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

    // Creates the player at the spawn point on the spawn layer of the map.
    Rectangle point = ((RectangleMapObject) layers.get("spawn").getObjects().get(0)).getRectangle();
    Gameplay.player = new Player(world, point.x, point.y);

    // Create teleport <- this is interactive tiled map object.
    for (MapObject object : layers.get("teleports").getObjects()) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      // Create a new instantiated Teleport object.
      new Teleport(world, map, rect, object.getName());
    }

    // Create systems <- this is interactive tiled map object.
    for (MapObject object : layers.get("systems").getObjects()) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      // Create a new instantiated System object.
      // Store system object in the systems Arraylist.
      Gameplay.systems.add(new StationSystem(world, map, rect, object.getName()));
    }

    // Create doors <- this is interactive tiled map object.
    for (MapObject object : layers.get("doors").getObjects()) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      // Create a new instantiated door object.
      // Adds door object to the Doors Arraylist.
      Gameplay.doors.add(new Door(world, map, rect, object.getName().equals("jailDoor")));
    }

    // Create jails
    int jailNumber = 0;
    for (MapObject object : layers.get("jail").getObjects()) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Jail(world, map, rect, jailNumber);
      jailNumber++;
    }
  }
}
