package com.team3.game.characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.Random;


/**
 * Manage NPCs in the game.
 */
public class NpcManager implements Serializable {


  public World world;
  public TiledMap map;

  public static ArrayList<Npc> npcs = new ArrayList<>();
  public static ArrayList<float[]> spawnPositions = new ArrayList<>();


  /**
   * Instantiates a new NPC Manager.

   * @param world The game world
   * @param map The tiled map
   */
  public NpcManager(World world, TiledMap map) {
    this.world = world;
    this.map = map;
    generateInitialPositions();
  }

  /**
   * Generate 20 random spawn locations for NPCs to use.
   */
  public void generateInitialPositions() {
    MapLayer npcSpawn = map.getLayers().get("npcSpawns");

    while (spawnPositions.size() < 20) {
      for (MapObject object : npcSpawn.getObjects()) {
        Rectangle point = ((RectangleMapObject) object).getRectangle();
        float [] position = new float[]{point.x, point.y};
        double randomPo = Math.random();
        if (randomPo > 0.5 && !spawnPositions.contains(position)) {
          spawnPositions.add(position);
        }
      }
    }
  }

  /**
   * Create NPCs in box2D world and set initial destination for NPCs.
   */
  public void generateNpcs() {
    int destcount = 1;

    for (int i = 0; i < spawnPositions.size(); i++) {

      float[] position = spawnPositions.get(i);
      // Set destination for NPC.
      if (i == spawnPositions.size() - 1) {
        destcount = 0;
      }
      float[] dest = spawnPositions.get(destcount);
      destcount += 1;
      // Pic for NPC needed.
      Npc npc = new Npc(world, position[0], position[1]);
      npc.setDest(dest[0], dest[1]);
      npc.moveToDest();
      npcs.add(npc);

    }

  }

  /**
   * Render the NPC, should be called in render loop.

   * @param batch The SpriteBatch to draw the NPC to.
   */
  public void renderNpc(SpriteBatch batch) {
    for (Npc npc : npcs) {
      npc.draw(batch);
    }

  }


  /**
   * Update NPC, should be called in GamePlay update.

   * @param delta The time in seconds since the last update
   */
  public void updateNpc(float delta) {

    for (Npc npc : npcs) {
      if (!npc.isMoving()) {
        generateNextPosition(npc);
      }
      npc.update(delta);
    }

  }

  /**
   * Generates the next random position an NPC will go to.

   * @param npc The NPC to randomly pathfind
   */
  public void generateNextPosition(Npc npc) {
    int index;
    Random random = new Random();
    index = random.nextInt(20);

    float[] destination = spawnPositions.get(index);
    npc.setDest(destination[0], destination[1]);
    npc.moveToDest();
  }

  @Override
  public void write(Json json) {
    json.writeValue("npcs", npcs);
  }

  @Override
  public void read(Json json, JsonValue jsonData) { }


}
