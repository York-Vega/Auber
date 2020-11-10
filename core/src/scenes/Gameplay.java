package scenes;

import auber.Player;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;
import tools.B2worldCreator;
import tools.TeleportContactListener;


/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay implements Screen {

    private GameMain game;

    public Player p1;

    /// Tile map loader
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Viewport viewport;

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera camera;

    private float playerSpeed = 50f;

    /**
     * Creates a new instatntiated game.

     * @param game The game object used in Libgdx things
     */
    public Gameplay(GameMain game)  {

        this.game = game;

        this.world = new World(new Vector2(0, 0), true); // create a box2D world

        maploader = new TmxMapLoader(); // creater maploader for tiled map
        map = maploader.load("Map/Map.tmx"); // load the tiled map
        renderer = new OrthogonalTiledMapRenderer(map);
    
        // this image is only for test purpose, needs to be changed with proper sprite
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 270);
        camera.update();

        viewport = new FitViewport(480, 270, camera);
        // set the viewport area for camera

        b2dr = new Box2DDebugRenderer(); // create a box2d render

        // create 2d box world for objects , walls, teleport...
        B2worldCreator.createWorld(world, map, this); 

        world.setContactListener(new TeleportContactListener());

    }

    /**
     * Updates the game, logic will go here called by libgdx GameMain.
     */
    public void update()  {

        world.step(Gdx.graphics.getDeltaTime(), 8, 3); // update the world
        p1.b2body.setLinearDamping(3f);
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

    }

    @Override
    public void render(float delta) {


        update();

        p1.updatePlayer(delta);
        // set camera follow the player(bod2d body)
        camera.position.set(p1.b2body.getPosition().x, p1.b2body.getPosition().y, 0);
        camera.update(); // update the camera
        renderer.setView(camera); // enable tiled map movable view with camera
        

        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); // render the tiled map

        // render the 2Dbox world and same as map, enable world movable view with camera
        b2dr.render(world, camera.combined); 

        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        p1.draw(game.getBatch()); // draw the player sprite

        game.getBatch().end();

        //dispose();

    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

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
