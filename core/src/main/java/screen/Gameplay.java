package screen;

import characters.Player;
import characters.ai.EnemyManager;
import characters.ai.NpcManager;
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
import java.util.ArrayList;
import map.Map;
import screen.actors.ArrestedHeader;
import screen.actors.HealthBar;
import screen.actors.SystemStatusMenu;
import screen.actors.Teleport_Menu;
import sprites.Door;
import sprites.Systems;
import tools.B2worldCreator;
import tools.BackgroundRenderer;
import tools.CharacterRenderer;
import tools.DoorControll;
import tools.LightControl;
import tools.ObjectContactListener;
import tools.TeleportProcess;



/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay implements Screen {

    private final GameMain game;

    public static ArrayList<Door> doors = new ArrayList<>();

    public static ArrayList<Systems> systems = new ArrayList<>();

    public static Player player;

    public EnemyManager enemyManager;

    public NpcManager npcManager;

    public OrthographicCamera camera;

    public Viewport viewport;

    public Hud hud;

    public TeleportProcess teleportProcess;

    public HealthBar healthBar;

    public Teleport_Menu teleportMenu;

    public SystemStatusMenu systemStatusMenu;

    public ArrestedHeader arrestedHeader;

    private final TmxMapLoader maploader;

    private final TiledMap map;

    private final OrthogonalTiledMapRenderer renderer;

    private final BackgroundRenderer backgroundRenderer;

    private final World world;

    private boolean paused = false;

    private final LightControl lightControl;

    /**
     * Creates a new instantiated game.

     * @param game The game object used in Libgdx things
     */
    public Gameplay(GameMain game) {
        this(game, new Vector2(640, 360));
    }

    /**
     * Creates a new instantiated game.

     * @param game The game object used in Libgdx things
     * @param screenSize size of the rendered game screen, doesn't effect screen size
     */
    protected Gameplay(GameMain game, Vector2 screenSize) {

        this.game = game;
        // create a box2D world
        this.world = new World(new Vector2(0, 0), true);
        // create map loader for tiled map
        maploader = new TmxMapLoader();
        // load the tiled map
        map = maploader.load("Map/Map.tmx");
        Map.create(map);
        renderer = new OrthogonalTiledMapRenderer(map);
        // load all textures for render
        CharacterRenderer.loadTextures();
        // create a light control object
        lightControl = new LightControl(world);
        // create a new orthographic camera
        camera = new OrthographicCamera();
        // set the viewport area for camera
        viewport = new FitViewport(screenSize.x, screenSize.y, camera);
        // create a new background Render
        backgroundRenderer = new BackgroundRenderer(game.getBatch(), viewport);
        // create 2d box world for objects , walls, teleport...
        B2worldCreator.createWorld(world, map, this);
        // set the contact listener for the world
        world.setContactListener(new ObjectContactListener());
        // create HUD
        hud = new Hud(game.getBatch());
        // teleportMenu
        teleportMenu = hud.teleportMenu;
        // healthBar
        healthBar = hud.healthBar;
        // create a teleport_process instance
        teleportProcess = new TeleportProcess(teleportMenu, player, map);
        // system_status_menu
        systemStatusMenu = hud.systemStatusMenu;
        // generate all systems labels for status menu
        systemStatusMenu.generate_systemLabels(systems);
        // create arrest_status header
        arrestedHeader = hud.arrestedHeader;
        // create enemy_manager instance
        enemyManager = new EnemyManager(world, map, systems);
        // create Npc_manager instance
        npcManager = new NpcManager(world, map);

    }



    /**
     * Updates the game, logic will go here called by libgdx GameMain.
     */
    public void update()  {

        float delta = Gdx.graphics.getDeltaTime();
        backgroundRenderer.update(delta);
        world.step(delta, 8, 3);
        hud.stage.act(delta);
        player.update(delta);
        teleportProcess.validate();
        healthBar.updateHp(player);
        lightControl.light_update(systems);
        DoorControll.updateDoors(systems, delta);
        enemyManager.update_enemy(delta);
        npcManager.updateNpc(delta);
        systemStatusMenu.update_status(systems);
        arrestedHeader.update_Arrested(player);
        // if escape is pressed pause the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.pause();
        }
        checkGameState();

    }


    @Override
    public void show() {
        // set hud to be the input processor
        Gdx.input.setInputProcessor(hud.stage);
    }

    private static final int[] backgroundLayers = new int[]{0, 1, 2};
    private static final int[] forgroundLayers = new int[]{3};

    @Override
    public void render(float delta) {

        // if the game is not paused, update it
        // else if the pause menu indicates resume, resume the game
        // else if the pause menu indicates exit, end the game
        if (!this.paused) {
            update();
        } else if (this.hud.pauseMenu.resume()) {
            resume();
        } else if (this.hud.pauseMenu.exit()) {
            Gdx.app.exit();
        }


        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // set camera follow the player
        camera.position.set(0, 0, 0);
        camera.update();

        // this is needed to be called before the batch.begin(), or screen will freeze
        game.getBatch().setProjectionMatrix(camera.combined);
        viewport.apply();
        backgroundRenderer.render();

        // set camera follow the player
        camera.position.set(player.b2body.getPosition().x, player.b2body.getPosition().y, 0);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        // render the tilemap background
        renderer.setView(camera);
        renderer.render(backgroundLayers);

        // begin the batch
        game.getBatch().begin();
        // render player
        player.draw(game.getBatch());
        // render Infiltrators
        enemyManager.render_ememy(game.getBatch());
        // render NPC
        npcManager.renderNpc(game.getBatch());
        // end the batch
        game.getBatch().end();
        // render tilemap that should appear in front of the player
        renderer.render(forgroundLayers);
        // render the light
        lightControl.rayHandler.render();
        // render the hud
        hud.viewport.apply();
        hud.stage.draw();

    }


    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);

        hud.resize(width, height);
    }

    /**
     * to pause the game
     * set the paused flag and show the pause menu.
     */
    @Override
    public void pause() {
        this.paused = true;
        this.hud.pauseMenu.show();
    }

    /**
     * to resume the game
     * set the pause flag and hide the pause menu.
     */
    @Override
    public void resume() {
        this.paused = false;
        this.hud.pauseMenu.hide();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * check whether game ends.
     */
    public void checkGameState() {

        if (player.arrestedCount == 8) {
            game.setScreen(new WinLoseScreen(game.getBatch(), "YOU WIN!!"));
        }
        int sabotagedCount = 0;
        for (Systems system : systems) {
            if (system.is_sabotaged()) {
                sabotagedCount++;
            }
        }
        if (sabotagedCount >= 15 || player.health <= 1) {
            game.setScreen(new WinLoseScreen(game.getBatch(), "YOU LOSE!!"));
        }

    }
}
