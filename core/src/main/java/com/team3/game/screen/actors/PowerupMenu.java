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
}
