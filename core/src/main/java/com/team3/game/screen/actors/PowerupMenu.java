package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.team3.game.characters.Player;

/**
 * Adds a status menu of the player's powerups to the game.
 */
public class PowerupMenu extends VerticalGroup {

  public Label powerupsList;
  private Skin skin;
  public Label currentPowerup;
  public Label speedLabel;
  public Label visionLabel;

  /**
   * Show status of powerups.
   */
  public PowerupMenu() {
    super();
    skin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
    setFillParent(true);

    // Heading label
    powerupsList = new Label("POWERUPS:", skin, "title");
    powerupsList.setName("POWERUPS");
    powerupsList.getStyle().font.getData().setScale(.5f, .5f);

    // Label to show currently held powerup in inventory
    currentPowerup = new Label("No powerup in inventory!", skin, "title");

    // Label to show active powerups
    speedLabel = new Label("", skin, "title");
    visionLabel = new Label("", skin, "title");
    speedLabel.setColor(Color.BLUE);
    visionLabel.setColor(Color.PURPLE);

    addActor(powerupsList);
    addActor(currentPowerup);
    addActor(speedLabel);
    addActor(visionLabel);

    align(Align.bottomRight);
    columnAlign(Align.right).space(3);
    padRight(20);
    padBottom(20);
  }

  /**
   * Update the menu.
   *
   * @param player Player to query powerup status, and held powerups.
   */
  public void update_powerup_status(Player player) {

    if (player.speedActive) {
      speedLabel.setText("Increased speed active!");
      if (player.powerup != null && player.powerup.name().equals("SPEED")) {
        player.setPowerup(null);
      }
    } else {
      speedLabel.setText("");
    }

    if (player.visionActive) {
      visionLabel.setText("Increased vision active!");
      if (player.powerup != null && player.powerup.name().equals("VISION")) {
        player.setPowerup(null);
      }
    } else {
      visionLabel.setText("");
    }

    if (player.powerup == null) {
      currentPowerup.setColor(Color.RED);
      currentPowerup.setText("No powerup in inventory!");
    } else {
      currentPowerup.setColor(Color.WHITE);
      currentPowerup.setText(player.powerup.name());
    }
  }

}
