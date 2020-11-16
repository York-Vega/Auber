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



        new LwjglApplication(new GameMain(), config);
    }
}
