package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.team3.game.sprites.StationSystem;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Adds a status menu of the system to the game.
 */
public class SystemStatusMenu extends VerticalGroup {

  public Skin myskin;

  public Label sabotageCount;

  public int count = 0;

  public Hashtable<StationSystem, Label> statusMap = new Hashtable<>();

  /**
   * Show status of each systems.
   */
  public SystemStatusMenu() {
    super();
    myskin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
    setFillParent(true);

    // Sabotage count label to display number of sabotaged systems.
    sabotageCount = new Label("SABOTAGED: 0/17", myskin, "title");
    sabotageCount.setName("SABOTAGED:");
    sabotageCount.getStyle().font.getData().setScale(.5f, .5f);

    addActor(sabotageCount);

    align(Align.topLeft);
    columnAlign(Align.left).space(3);
    padLeft(20);
    padTop(20);
  }

  /**
   * Generate system labels.
   *
   * @param systems Arraylist of System objects
   */
  public void generate_systemLabels(ArrayList<StationSystem> systems) {
    for (StationSystem system : systems) {
      Label sys = new Label(system.getSystemName(), myskin, "title");
      sys.setColor(Color.WHITE);
      // setName to store name if enemy stop sabotaging, change the label text back to normal.
      sys.setName(system.getSystemName());
      // Scale the font size.
      sys.getStyle().font.getData().setScale(.35f, .35f);
      addActor(sys);
      // Build hashmap for system and menu.
      statusMap.put(system, sys);
    }

  }

  /**
   * Update the menu.
   *
   * @param systems Arraylist of System objects
   */
  public void update_status(ArrayList<StationSystem> systems) {

    count = 0;

    for (StationSystem system : systems) {

      Label sysLabel = statusMap.get(system);

      // If enemy is sabotaging system, label should warn player.
      if (system.is_sabotaging()) {

        sysLabel.setColor(Color.RED);
        sysLabel.setText(system.sysName + ": Under Attack" + ": (" + Math.round(system.hp) + "%)");

      }
      // If system is sabotaged, label should go gray.
      if (system.isSabotaged()) {
        count++;

        // Already sabotaged.
        if (sysLabel.getColor().equals(Color.GRAY)) {

          sysLabel.setText(system.sysName + ": Sabotaged(" + Math.round(system.hp) + "%)");

        } else {
          sysLabel.setText(system.sysName + ": Sabotaged("  + Math.round(system.hp) + "%)");
          sysLabel.setColor(Color.GRAY);
        }
      }

      // If system not being sabotaging or enemy stop sabotaging label should back to normal.
      if (system.isNotSabotaged()) {
        sysLabel.setColor(Color.WHITE);
        sysLabel.setText(system.sysName + ": (" + Math.round(system.hp) + "%)");
      }
    }

    // Update sabotage count.
    sabotageCount.setText(sabotageCount.getName() + " " + count + "/17");
    if (count >= 10) {
      sabotageCount.setColor(Color.RED);
    }
  }
}
