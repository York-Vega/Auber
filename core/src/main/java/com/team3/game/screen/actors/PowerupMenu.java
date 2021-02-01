package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.team3.game.sprites.Powerup;
import com.team3.game.sprites.StationSystem;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Adds a status menu of the player's powerups to the game.
 */
public class PowerupMenu extends VerticalGroup {

    public Label powerupsList;
    private Skin skin;

    /**
     * Show status of powerups.
     */
    public PowerupMenu() {
        super();
        skin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
        setFillParent(true);

        // Label to list powerups collected, but not yet used, by the player
        powerupsList = new Label("POWERUPS:", skin, "title");
        powerupsList.setName("POWERUPS");
        powerupsList.getStyle().font.getData().setScale(.5f, .5f);

        addActor(powerupsList);

        align(Align.bottomRight);
        columnAlign(Align.right).space(3);
        padRight(20);
        padBottom(20);
    }

    /**
     * Update the menu.
     *
     * @param powerup Arraylist of System objects
     */
    public void update_powerup_status(Powerup powerup) {

        System.out.println(powerup);

        /*for (StationSystem system : systems) {

            Label sysLabel = statusMap.get(system);

            // If enemy is sabotaging system, label should warn player.
            if (system.is_sabotaging()) {

                sysLabel.setColor(Color.RED);
                sysLabel.setText(system.sysName + ": Under Attack" + ": (" + Math.round(system.hp) + "%)");

            }
            // If system is sabotaged, label should go gray.
            if (system.is_sabotaged()) {
                // Already sabotaged.
                if (sysLabel.getColor().equals(Color.GRAY)) {

                    sysLabel.setText(system.sysName + ": Sabotaged(" + Math.round(system.hp) + "%)");

                } else {
                    sysLabel.setText(system.sysName + ": Sabotaged("  + Math.round(system.hp) + "%)");
                    sysLabel.setColor(Color.GRAY);
                    // Update sabotaged count.
                    count += 1;
                    sabotageCount.setText(sabotageCount.getName() + " " + count + "/17");
                    if (count >= 10) {
                        // If system sabotaged over 10, change color of title to red.
                        sabotageCount.setColor(Color.RED);
                    }
                }
                // Just be sabotaged.
            }
            // If system not being sabotaging or enemy stop sabotaging label should back to normal.
            if (system.is_not_sabotaged()) {
                sysLabel.setColor(Color.WHITE);
                sysLabel.setText(system.sysName + ": (" + Math.round(system.hp) + "%)");
            }
        }*/
    }

}
