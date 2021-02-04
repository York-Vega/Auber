package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.GdxTestRunner;
import com.team3.game.characters.Player;
import com.team3.game.map.Map;
import com.team3.game.screen.actors.TeleportMenu;
import com.team3.game.tools.CharacterRenderer;
import com.team3.game.tools.TeleportProcess;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestTeleport {
    
  @Test
  public void testWontTeleport() throws Exception {

    //necessary object instantiations to carry out test
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    TeleportMenu teleportMenu = new TeleportMenu();
    TeleportProcess teleportProcess = new TeleportProcess(teleportMenu, player, map);

    //runs the teleporters validate process whilst the player is unable to teleport
    player.b2body.setUserData("auber");
    teleportProcess.validate();

    //test is passed if the player is unable to teleport
    assertTrue("Player was able to teleport despite not being ready", teleportMenu.isDisabled());

  }

  @Test
  public void testWillTeleport() throws Exception {

    //necessary object instantiations to carry out test
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    TeleportMenu teleportMenu = new TeleportMenu();
    TeleportProcess teleportProcess = new TeleportProcess(teleportMenu, player, map);

    //runs the teleporters validate process whilst the player is able to teleport
    player.b2body.setUserData("ready_to_teleport");
    teleportProcess.validate();

    //test is passed if the player is able to teleport
    assertTrue("Player was unable to teleport despite being ready", !teleportMenu.isDisabled());

  }

  @Test
  public void testTeleportToLocation() throws Exception {

    //necessary object instantiations to carry out test
    CharacterRenderer.loadTextures();
    TmxMapLoader maploader = new TmxMapLoader();
    TiledMap map = maploader.load("Map/Map.tmx");
    Map.create(map);
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    TeleportMenu teleportMenu = new TeleportMenu();
    TeleportProcess teleportProcess = new TeleportProcess(teleportMenu, player, map);

    /*runs the teleporters validate process whilst the player is able to teleport and the selected 
    location is the reactor*/
    player.b2body.setUserData("ready_to_teleport");
    teleportMenu.setSelected("reactor");
    
    teleportProcess.validate();
    teleportProcess.validate();
    player.update(1);

    //test if the transform function has been executed
    assertTrue("Player was unable to teleport", (teleportMenu.getSelected() == "Teleport") 
              && teleportMenu.isDisabled());

  }

}
