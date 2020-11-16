package com.team3.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screen.MainMenu;

public class GameMain extends Game  {
    SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenu(batch));
    }

    @Override
    public void render() {
        super.render(); // render multiple screen

        // Gdx.gl.glClearColor(1, 0, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // batch.begin();
        //
        // batch.end();
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }
}
