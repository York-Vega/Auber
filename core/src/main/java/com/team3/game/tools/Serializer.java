package com.team3.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.team3.game.screen.Gameplay;


/**
 * Handle the serialization of a game state to and from JSON.
 **/
public class Serializer {
  private Gameplay gameplay;

  public Serializer(Gameplay gameplay) {
    this.gameplay = gameplay;
  }

  /**
   * Dump the current state of the world to a JSON string.
   *
   * @param pretty Whether to return a spaced and indented string or not
   * @return A JSON string representing the game state
   **/
  public String dumpStr(boolean pretty) {
    Json json = new Json();

    if (pretty) {
      return json.prettyPrint(gameplay);
    }
    return json.toJson(gameplay);
  }

  /**
   * Write a JSON string representing the current game state to a file.
   *
   * @param fileName The name of the file to save to
   * @param pretty Whether the file should be spaced and indented
   **/
  public void toFile(String fileName, boolean pretty) {
    FileHandle file = Gdx.files.local("saves/" + fileName + ".json");
    file.writeString(dumpStr(pretty), false);
  }
}
