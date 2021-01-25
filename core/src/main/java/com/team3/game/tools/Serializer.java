package com.team3.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.team3.game.screen.Gameplay;


public class Serializer {
  private Gameplay gameplay;

  public Serializer(Gameplay gameplay) {
    this.gameplay = gameplay;
  }

  public String dumpStr(boolean pretty) {
    Json json = new Json();

    if (pretty) {
      return json.prettyPrint(gameplay);
    }
    return json.toJson(gameplay);
  }

  public void toFile(String fileName, boolean pretty) {
    FileHandle file = Gdx.files.local("saves/" + fileName + ".json");
    file.writeString(dumpStr(pretty), false);
  }
}
