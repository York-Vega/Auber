package com.team3.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;
import com.team3.game.tools.BackgroundRenderer;
import com.team3.game.tools.Serializer;

/**
 * MainMenu.
 */
public class MainMenu extends ScreenAdapter {

  private Viewport viewport;
  private OrthographicCamera camera;
  private TextureAtlas atlas;
  private Stage stage;
  private Skin skin;
  private SpriteBatch batch;
  private BackgroundRenderer backgroundRenderer;
  private Texture instructionsTexture;

  /**
   * Creates an instantiated instance of the MainMenu screen.

   * @param batch The spritebatch to draw with
   */
  public MainMenu(SpriteBatch batch) {
    this.batch = batch;
    atlas = new TextureAtlas("skin/hudskin/comic-ui.atlas");
    skin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"), atlas);
    instructionsTexture = new Texture(Gdx.files.internal("menu/instructions.png"));

    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    viewport = new ScreenViewport(camera);
    viewport.apply();

    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    camera.update();

    stage = new Stage(viewport, batch);

    backgroundRenderer = new BackgroundRenderer(batch, viewport);


  }

  @Override
  public void show() {
    // Passes all input to the stage.
    Gdx.input.setInputProcessor(stage);

    // Create a main table into which all ui elements will be placed.
    Table root = new Table();
    root.setFillParent(true);
    root.center();

    final GameMain game = (GameMain) Gdx.app.getApplicationListener();

    // New Game Buttons (Three difficulties)

    TextButton newEasyButton = new TextButton("New Game (Easy)", skin);
    newEasyButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gameplay gameplay = new Gameplay(game, false, Gameplay.Difficulty.EASY);
        game.setScreen(gameplay);
      }
    });

    TextButton newMediumButton = new TextButton("New Game (Medium)", skin);
    newMediumButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gameplay gameplay = new Gameplay(game, false, Gameplay.Difficulty.MEDIUM);
        game.setScreen(gameplay);
      }
    });

    TextButton newHardButton = new TextButton("New Game (Hard)", skin);
    newHardButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gameplay gameplay = new Gameplay(game, false, Gameplay.Difficulty.HARD);
        game.setScreen(gameplay);
      }
    });

    // Continue Game Button
    TextButton continueButton = new TextButton("Continue Game", skin);
    continueButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (Serializer.saveExists()) {
          Gameplay gameplay = Serializer.fromFile("save", game);
          game.setScreen(gameplay);
        }
      }
    });

    // Demo button
    TextButton demoButton = new TextButton("Demo", skin);
    demoButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new GameDemo(game));
      }
    });

    // Game title
    Label title = new Label("Vega - Auber", skin);

    // Instructions image
    Image instructionsImage = new Image(instructionsTexture);
    instructionsImage.setScaling(Scaling.fit);

    root.add(title);
    root.row();
    root.add(newEasyButton);
    root.row();
    root.add(newMediumButton);
    root.row();
    root.add(newHardButton);
    root.row();
    root.add(continueButton);
    root.row();
    root.add(demoButton);
    root.row();
    root.add(instructionsImage);

    stage.addActor(root);
  }

  @Override
  public void render(float delta) {
    viewport.apply();
    backgroundRenderer.update(delta);
    stage.act();

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.position.set(0, 0, 0);
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    backgroundRenderer.render();
    camera.position.set(viewport.getScreenWidth() / 2f, viewport.getScreenHeight() / 2f, 0);
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }

  @Override
  public void dispose() {
    skin.dispose();
    atlas.dispose();
  }
}

