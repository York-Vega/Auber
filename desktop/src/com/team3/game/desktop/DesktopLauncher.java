package com.team3.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.team3.game.GameMain;

public class DesktopLauncher {
    public static void main(String[] args)  {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 800;
        config.height = 640;
        config.resizable = true;


        // TexturePacker.Settings sets = new TexturePacker.Settings();
        // sets.pot = true;
        // sets.fast = true;
        // sets.combineSubdirectories = true;
        // sets.paddingX = 1;
        // sets.paddingY = 1;
        // sets.edgePadding = true;
        // TexturePacker.process(sets,"Tiles","./","textures");

        new LwjglApplication(new GameMain(), config);
    }
}
