package screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Menu {
    protected Window window;
    protected Skin skin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
    protected boolean paused;

    protected Menu(String name) {
        this.window = new Window(name, this.skin);
        window.setMovable(false);

        window.setSize(200, 500);
        window.setPosition(1280 / 2 - window.getWidth() / 2, 720 / 2 - window.getHeight() / 2);
        window.setVisible(false);
        window.setLayoutEnabled(false);
        window.setColor(Color.BLACK);

        paused = false;
    }

    /**
     * shows the menu.
     */
    public void show() {
        window.setVisible(true);
        paused = true;
    }

    /**
     * hides the menu.
     */
    public void hide() {
        window.setVisible(false);
    }
}
