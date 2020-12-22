package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;

/**
 * Win or Lose Screen.
 */
public class WinLoseScreen implements Screen {

    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;
    private String status;

    /**
     * Create an instantiated instance of the win or lose screen.

     * @param batch spriteBatch of the game
     * @param status win or lose
     */
    public WinLoseScreen(SpriteBatch batch, String status) {

        this.status = status;
        atlas = new TextureAtlas("skin/hudskin/comic-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"), atlas);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ScreenViewport(camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // create a main table into which all ui elements will be placed
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label gamestatus = new Label(status, skin);
        TextButton playButton = new TextButton("Main Menu", skin);
        TextButton exitButton = new TextButton("Exit", skin);


        // load a new game
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain game = (GameMain) Gdx.app.getApplicationListener();
                game.setScreen(new MainMenu(game.getBatch()));
            }
        });

        // exit the game
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        root.add(gamestatus);
        root.row();
        //root.add(playButton).spaceBottom(10).spaceTop(10);
        //root.row();
        root.add(exitButton);

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

    }

}
