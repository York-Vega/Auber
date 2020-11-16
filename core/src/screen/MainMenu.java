package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;

/**
 * MainMenu.
 */
public class MainMenu implements Screen {

    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;

    /**
     * Creates an instantiated instance of the MainMenu screen.
     */
    public MainMenu(SpriteBatch batch) {
        atlas = new TextureAtlas("neonui/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("neonui/neon-ui.json"), atlas);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new FitViewport(480, 270, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        // passes all input to the stage
        Gdx.input.setInputProcessor(stage);

        // create a main table into which all ui elements will be placed
        Table root = new Table();
        root.setFillParent(true);
        root.top();

        // main play button (others can be added easily as needed)
        TextButton playButton = new TextButton("Play", skin);

        // creates a listener to listen for clicks on the button
        // when button is clicked start an instance of Gameplay to start playing the game
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain game = (GameMain) Gdx.app.getApplicationListener();
                game.setScreen(new Gameplay(game));
            }
        });

        Label title = new Label("Vega - Auber", skin);

        root.add(title);
        root.row();
        root.add(playButton);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    
    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}
