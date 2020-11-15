package scenes;

import auber.Player;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;
import tools.B2worldCreator;
import tools.TeleportContactListener;
import tools.Teleport_process;

/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay implements Screen {

    private GameMain game;

    public Player p1;

    OrthographicCamera camera;

    /// Tile map loader
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private float playerSpeed = 60f;

    public Teleporter_Menu teleporter_menu;

    public Teleport_process teleport_process;

    private boolean lightSabotaged = false;

    private RayHandler rayHandler;


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

        rayHandler = new RayHandler(world);
    
        // this image is only for test purpose, needs to be changed with proper sprite
        //p1 = new Player(world, "player_test.png", 1133, 1011);
        camera = new OrthographicCamera();
        // set the viewport area for camera
        camera.setToOrtho(false, 800, 640);
        // create a box2d render
        b2dr = new Box2DDebugRenderer(); // create a box2d render
        // create 2d box world for objects , walls, teleport...
        B2worldCreator.createWorld(world, map, this); 
        // set the contact listener for the world
        world.setContactListener(new TeleportContactListener());
        // create the teleport drop down menu
        teleporter_menu = new Teleporter_Menu(game.getBatch());
        // get the selectedBox from the table in stage
        Table boxTable = (Table) teleporter_menu.stage.getActors().get(0);
        SelectBox<String> selected_room = (SelectBox<String>) boxTable.getChild(0);
        // create a teleport_process instance
        teleport_process = new Teleport_process(selected_room,p1,map);

    }



    /**
     * Updates the game, logic will go here called by libgdx GameMain.
     */
    public void update()  {

        world.step(Gdx.graphics.getDeltaTime(), 8, 3); // update the world
        // update the teleport_menu stage viewport size
        teleporter_menu.stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );

        if (lightSabotaged == true) {
            rayHandler.setAmbientLight(.3f);
        } else {
            rayHandler.setAmbientLight(.8f);
        }
        rayHandler.update();

        p1.updatePlayer(1/60f);

        p1.b2body.setLinearDamping(5f);
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
    }


    @Override
    public void show() {
        // !! This is important, without this setting, the menu will not response !!
        Gdx.input.setInputProcessor(teleporter_menu.stage);
    }

    @Override
    public void render(float delta) {


        update();
        // set camera follow the player(bod2d body)
        camera.position.set(p1.b2body.getPosition().x, p1.b2body.getPosition().y, 0);
        // enable tiled map movable view with camera
        renderer.setView(camera);
        // update the camera
        camera.update();
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render the tiled map
        renderer.render();
        // render the 2Dbox world and same as map, enable world movable view with camera
        rayHandler.render();
        b2dr.render(world, camera.combined);

        game.getBatch().setProjectionMatrix(camera.combined);
        // this is needed to be called before the bath.begin(), or scrren will frozen
        teleporter_menu.stage.act();
        // start the batch
        game.getBatch().begin();
        // draw the player sprite
        p1.draw(game.getBatch());
        // end the batch
        game.getBatch().end();
        // render the teleporter_menu(the selectBox)
        teleporter_menu.stage.draw();
        // validate the teleportation
        teleport_process.validate();

        //dispose();

    }


    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false,width,height);

        camera.update();
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
        rayHandler.dispose();
    }
}
