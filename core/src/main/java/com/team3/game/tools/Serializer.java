package com.team3.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.team3.game.GameMain;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.characters.ai.EnemyManager;
import com.team3.game.characters.ai.Npc;
import com.team3.game.characters.ai.NpcManager;
import com.team3.game.screen.Gameplay;
import com.team3.game.sprites.StationSystem;

import java.util.ArrayList;


/**
 * Handle the serialization of a game state to and from JSON.
 **/
public class Serializer {
  /**
   * Dump the current state of the world to a JSON string.
   *
   * @param pretty Whether to return a spaced and indented string or not
   * @return A JSON string representing the game state
   **/
  public String dumpStr(Gameplay gameplay, boolean pretty) {
    Json json = new Json();

    if (pretty) {
      return json.prettyPrint(gameplay);
    }
    return json.toJson(gameplay);
  }

  /**
   * Write a JSON string representing the current game state to a file.
   *
   * @param fileName The name of the file to save to (excluding its json extension)
   * @param pretty Whether the file should be spaced and indented
   **/
  public void toFile(String fileName, boolean pretty, Gameplay gameplay) {
    FileHandle file = Gdx.files.local("saves/" + fileName + ".json");
    file.writeString(dumpStr(gameplay, pretty), false);
  }

  /**
   * Generate a gameplay object from a JSON save file.
   *
   * @param fileName The name of the save file (excluding its json extension)
   * @return A gameplay object representing the loaded game state
   **/
  public Gameplay fromFile(String fileName, final GameMain main) {
    Json json = new Json();
    json.setSerializer(Gameplay.class, new Json.Serializer<Gameplay>() {
      @SuppressWarnings("rawtypes")
      @Override
      public void write(Json json, Gameplay object, Class knownType) {
        // This function can be empty as this code is only called when reading from JSON
      }

      @SuppressWarnings("rawtypes")
      @Override
      public Gameplay read(Json json, JsonValue jsonData, Class type) {
        Gameplay gameplay = new Gameplay(main, true);
        // System.out.println(jsonData.getChild("npc_manager").toString());

        for (JsonValue systemData : jsonData.get("systems")) {
          Gameplay.systems.stream()
              .filter(system -> systemData.getString("name").equals(system.getSystemName()));
        }

        // TODO: Some NPCs don't have a path. Due to static vars somewhere
        for (JsonValue npcData : jsonData.getChild("npc_manager")) {
          JsonValue positionData = npcData.get("position");
          Npc npc = new Npc(gameplay.world, positionData.get("x").asFloat(), 
              positionData.get("y").asFloat());
          npc.destX = npcData.get("dest_x").asFloat();
          npc.destY = npcData.get("dest_y").asFloat();
          npc.moveToDest();
          NpcManager.npcs.add(npc);
        }

        for (JsonValue enemyData : jsonData.getChild("enemy_manager")) {
          JsonValue positionData = enemyData.get("position");
          Enemy enemy = new Enemy(gameplay.world, positionData.get("x").asFloat(), 
              positionData.get("y").asFloat());
          // Get system from name
          // Set enemy target system to system
          EnemyManager.enemies.add(enemy);
        }

        return gameplay;
      }
    });

    FileHandle file = Gdx.files.local("saves/" + fileName + ".json");
    Gameplay gameplay = json.fromJson(Gameplay.class, file.readString());
    return gameplay;
  }
}
