package screen;

import auber.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;
import screen.actors.HealthBar;
import screen.actors.System_status_menu;
import screen.actors.Teleport_Menu;
import sprites.Systems;
import tools.B2worldCreator;
import tools.BackgroundRenderer;
import tools.Light_control;
import tools.Object_ContactListener;
import tools.Teleport_process;


import java.util.ArrayList;


/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay implements Screen {

    private GameMain game;

    public Player p1;

    public OrthographicCamera camera;

    public Viewport viewport;

    /// Tile map loader
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private BackgroundRenderer backgroundRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    public Hud hud;

    public Teleport_process teleport_process;

    public HealthBar healthBar;

    public Teleport_Menu teleport_menu;

    private Light_control light_control;

    public ArrayList<Systems> systems = new ArrayList<>();

    public System_status_menu systemStatusMenu;


    /**
     * Creates a new instatntiated game.

     * @param game The game object used in Libgdx things
     */
    public Gameplay(GameMain game)  {

        this.game = game;
        // create a box2D world
        this.world = new World(new Vector2(0, 0), true);
        // creater maploader for tiled map
        maploader = new TmxMapLoader();
        // load the tiled map
        map = maploader.load("Map/Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        // create a light control object
        light_control = new Light_control(world);

        // this image is only for test purpose, needs to be changed with proper sprite
        //p1 = new Player(world, "player_test.png", 1133, 1011);

        // create a new orthographic camera
        camera = new OrthographicCamera();
        // set the viewport area for camera
        viewport = new FitViewport(640, 360, camera);

        backgroundRenderer = new BackgroundRenderer(game.getBatch(), viewport);

        // create a box2d render
        b2dr = new Box2DDebugRenderer();
        // create 2d box world for objects , walls, teleport...
        B2worldCreator.createWorld(world, map, this); 
        // set the contact listener for the world
        world.setContactListener(new Object_ContactListener());
        // create the teleport drop down menu
        hud = new Hud(game.getBatch());
        // to select teleport room
        teleport_menu = hud.teleport_menu;
        // use to update the player HP
        healthBar = hud.healthBar;
        // create a teleport_process instance
        teleport_process = new Teleport_process(teleport_menu,p1,map);
        // create a system_status_menu instance
        systemStatusMenu = hud.system_status_menu;
        systemStatusMenu.generate_systemLabels(systems);

    }

    private float delta; 

    /**
     * Updates the game, logic will go here called by libgdx GameMain.
     */
    public void update()  {
        delta = Gdx.graphics.getDeltaTime();

        backgroundRenderer.update(delta);

        world.step(delta, 8, 3);
        p1.updatePlayer(delta);
        teleport_process.validate();
        healthBar.update_HP(p1);
        hud.stage.act(delta);
        light_control.light_update();
        systemStatusMenu.update_status(systems);
        
    }


    @Override
    public void show() {
        // !! This is important !!
        Gdx.input.setInputProcessor(hud.stage);
    }

    private static final int[] backgroundLayers = new int[]{0, 1};
    private static final int[] forgroundLayers = new int[]{};

    @Override
    public void render(float delta) {
        update();

        // set camera follow the player
        camera.position.set(p1.b2body.getPosition().x, p1.b2body.getPosition().y, 0);
        camera.update();
        
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        backgroundRenderer.render();

        // render the tilemap
        renderer.setView(camera);
        renderer.render(backgroundLayers);

        // this is needed to be called before the batch.begin(), or scrren will freeze
        game.getBatch().setProjectionMatrix(camera.combined);

        // render the player sprite
        game.getBatch().begin();
        p1.draw(game.getBatch());
        game.getBatch().end();

        // render tilemap that should apear infront of the player
        renderer.render(forgroundLayers);

        // render the light
        light_control.rayHandler.render();

        // render the hud
        hud.viewport.apply();
        hud.stage.draw();

        //dispose();

    }


    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);

        hud.resize(width, height);
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
