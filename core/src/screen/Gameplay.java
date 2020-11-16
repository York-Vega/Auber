package screen;

import auber.Player;
import map.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;

import ai.AICharacter;
import screen.actors.HealthBar;
import screen.actors.Teleport_Menu;
import tools.B2worldCreator;
import tools.Light_control;
import tools.Object_ContactListener;
import tools.Teleport_process;

/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay implements Screen {

    private GameMain game;

    public static Player p1;

    // TEST
    public AICharacter npc;

    public OrthographicCamera camera;

    public Viewport viewport;

    /// Tile map loader
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private float playerSpeed = 60f;

    public Hud hud;

    public Teleport_process teleport_process;

    public HealthBar healthBar;

    public Teleport_Menu teleport_menu;

    private Light_control light_control;


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
        Map.create(map);
        renderer = new OrthogonalTiledMapRenderer(map);

        // create a light control object
        light_control = new Light_control(world);

        // this image is only for test purpose, needs to be changed with proper sprite
        //p1 = new Player(world, "player_test.png", 1133, 1011);

        
        // create a new orthographic camera
        camera = new OrthographicCamera();
        // set the viewport area for camera
        viewport = new FitViewport(1280, 720, camera);

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

    }

    private float delta; 

    /**
     * Updates the game, logic will go here called by libgdx GameMain.
     */
    public void update()  {
        delta = Gdx.graphics.getDeltaTime();

        world.step(delta, 8, 3); // update the world
        //update player HP
        healthBar.update_HP(p1);
        // update the light
        light_control.light_update();
        //update auber
        p1.updatePlayer(delta);
        
        // input listener
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            p1.b2body.applyLinearImpulse(new Vector2(-playerSpeed, 0),
                p1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            p1.b2body.applyLinearImpulse(new Vector2(playerSpeed, 0),
                p1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            p1.b2body.applyLinearImpulse(new Vector2(0, playerSpeed),
                p1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            p1.b2body.applyLinearImpulse(new Vector2(0, -playerSpeed),
                p1.b2body.getWorldCenter(), true);
        }

        // TEST
        npc.update(1/60f);
    }


    @Override
    public void show() {
        // !! This is important !!
        Gdx.input.setInputProcessor(hud.stage);
    }

    @Override
    public void render(float delta) {

        update();
        // set camera follow the player(bod2d body)
        camera.position.set(p1.b2body.getPosition().x, p1.b2body.getPosition().y, 0);
        // enable tiled map movable view with camera
 
        // update the camera
        camera.update();
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render the tiled map
        renderer.setView(camera);
        renderer.render();

        // render the 2Dbox world with shape, remove this line when deploy
        //b2dr.render(world, camera.combined);

        // render the light
        light_control.rayHandler.render();
        
        game.getBatch().setProjectionMatrix(camera.combined);
        // this is needed to be called before the bath.begin(), or scrren will frozen
        hud.stage.act();
        // start the batch
        game.getBatch().begin();
        // draw the player sprite
        viewport.apply();
        p1.draw(game.getBatch());

        //TEST 
        npc.draw(game.getBatch());

        // end the batch
        game.getBatch().end();
        // render the hud
        hud.viewport.apply();
        hud.stage.draw();
        // validate the teleportation
        teleport_process.validate();

        

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
