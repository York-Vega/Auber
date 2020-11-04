package scenes;

import auber.player;
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
import com.team3.game.gameMain;
import tools.B2worldCreator;
import tools.Teleport_contact_listener;


public class Gameplay implements Screen {

    private gameMain game;

    private player p1;

    OrthographicCamera camera;


    /// Tile map loader
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;



    public Gameplay(gameMain game){

        this.game = game;

        world = new World(new Vector2(0,0),true); // create a box2D world

        maploader = new TmxMapLoader(); // creater maploader for tiled map
        map = maploader.load("wholemap.tmx"); // load the tiled map
        renderer = new OrthogonalTiledMapRenderer(map);

        p1 = new player(world,"player_test.png",300,200); // this image is only for test purpose, needs to be changed with proper sprite
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,640);// set the viewport area for camera

        b2dr = new Box2DDebugRenderer(); // create a box2d render

        new B2worldCreator(world,map); // create 2d box world for objects , walls, teleport...

        world.setContactListener(new Teleport_contact_listener());

    }


    public void update(){


        world.step(Gdx.graphics.getDeltaTime(),8,3); // update the world
        p1.b2body.setLinearDamping(3f);
        // input listener
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            p1.b2body.applyLinearImpulse(new Vector2(-50f,0),p1.b2body.getWorldCenter(),true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            p1.b2body.applyLinearImpulse(new Vector2(50f,0),p1.b2body.getWorldCenter(),true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            p1.b2body.applyLinearImpulse(new Vector2(0,50f),p1.b2body.getWorldCenter(),true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            p1.b2body.applyLinearImpulse(new Vector2(0,-50f),p1.b2body.getWorldCenter(),true);
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        update();

        p1.updatePlayer(delta);
        camera.position.set(p1.b2body.getPosition().x,p1.b2body.getPosition().y,0); // set camera follow the player(bod2d body)
        renderer.setView(camera); // enable tiled map movable view with camera
        camera.update();// update the camera

        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); // render the tiled map

        b2dr.render(world, camera.combined); // render the 2Dbox world and same as map, enable world movable view with camera

        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        p1.draw(game.getBatch()); // draw the player sprite

        game.getBatch().end();

        //dispose();

    }



    @Override
    public void resize(int width, int height) {

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
