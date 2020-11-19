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
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }
}
