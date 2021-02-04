package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.team3.game.characters.Player;

/**
 * Displays the number of arrested infiltrators.
 */
public class ArrestedHeader extends Label {

  /**
   * Label to show the number of arrested infiltrators.
   */
  public ArrestedHeader() {

    super("Arrested: 0/8 (Press E to arrest)",
        new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")), "title");
    setName("Arrested:");
    getStyle().font.getData().setScale(.5f, .5f);
    setColor(Color.WHITE);

  }

  /**
   * Update the number of arrested infiltrators.
   *
   * @param auber Player
   */
  public void update_Arrested(Player auber) {
    int arrestedCount = auber.arrestedCount;
    setText(getName() + arrestedCount + "/8 (Press E to arrest)");
    if (arrestedCount > 0) {
      setColor(Color.GREEN);
    }
  }

}
