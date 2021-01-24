package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Adds a teleport menu to the game.
 */
public class TeleportMenu extends SelectBox<String> {

  public String[] teleporter;

  /**
   * SelectBox teleport menu.
   */
  public TeleportMenu() {
    super(new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
    teleporter = new String[]{"Teleport", "controlRoom", "mess", "infirmary", 
      "hangar", "reactor", "Jail"};
    setItems(teleporter);
    // Center the list.
    getList().setAlignment(Align.center);
    getStyle().listStyle.font.getData().setScale(.8f, 1f);
    setDisabled(true);
  }

}
