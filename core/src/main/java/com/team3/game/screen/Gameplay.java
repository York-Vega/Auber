package com.team3.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.GameMain;
import com.team3.game.characters.Player;
import com.team3.game.characters.ai.EnemyManager;
import com.team3.game.characters.ai.NpcManager;
import com.team3.game.characters.ai.PowerupManager;
import com.team3.game.map.Map;
import com.team3.game.screen.actors.ArrestedHeader;
import com.team3.game.screen.actors.HealthBar;
import com.team3.game.screen.actors.PowerupMenu;
import com.team3.game.screen.actors.SystemStatusMenu;
import com.team3.game.screen.actors.TeleportMenu;
import com.team3.game.sprites.Door;
import com.team3.game.sprites.StationSystem;
import com.team3.game.tools.B2worldCreator;
import com.team3.game.tools.BackgroundRenderer;
import com.team3.game.tools.CharacterRenderer;
import com.team3.game.tools.DoorControl;
import com.team3.game.tools.LightControl;
import com.team3.game.tools.ObjectContactListener;
import com.team3.game.tools.TeleportProcess;
import java.util.ArrayList;


/**
 * Main gameplay object, holds all game data.
 */
public class Gameplay extends ScreenAdapter implements Serializable {

  private final GameMain game;

  public static ArrayList<Door> doors = new ArrayList<>();

  public static ArrayList<StationSystem> systems = new ArrayList<>();

  public static Player player;

  public EnemyManager enemyManager;

  public NpcManager npcManager;

  public PowerupManager powerupManager;

  public OrthographicCamera camera;

  public Viewport viewport;

  public Hud hud;

  public TeleportProcess teleportProcess;

  public HealthBar healthBar;

  public TeleportMenu teleportMenu;

  public SystemStatusMenu systemStatusMenu;

  public PowerupMenu powerupStatusMenu;

  public ArrestedHeader arrestedHeader;

  public static enum Difficulty {
    EASY, MEDIUM, HARD
  }

  public static float SABOTAGE_RATE = 0.05f;

  private final TmxMapLoader maploader;

  public final TiledMap map;

  private final OrthogonalTiledMapRenderer renderer;

  private final BackgroundRenderer backgroundRenderer;

  public final World world;

  private boolean paused = false;

  private final LightControl lightControl;

  public boolean zoomedOut;

  /**
   * Creates a new instantiated game.
   *
   * @param game The game object used in Libgdx
   * @param fromJson Json boolean value
   * @param difficulty Sets difficulty of game
   */
  public Gameplay(GameMain game, boolean fromJson, Difficulty difficulty) {
    this(game, new Vector2(640, 360), fromJson, difficulty);
  }

  /**
   * Creates a new instantiated game.
   *
   * @param game       The game object used in Libgdx
   * @param screenSize Size of the rendered game screen, doesn't affect screen size
   * @param fromJson Json boolean value
   * @param difficulty Sets difficulty of game
   */
  public Gameplay(GameMain game, Vector2 screenSize, boolean fromJson, Difficulty difficulty) {

    switch (difficulty) {
      case EASY:
        SABOTAGE_RATE = 0.05f;
        break;
      case MEDIUM:
        SABOTAGE_RATE = 0.1f;
        break;
      case HARD:
        SABOTAGE_RATE = 0.4f;
        break;
      default:
        throw new IllegalArgumentException("Received unexpected difficulty level");
    }

    this.game = game;
    // Create a box2D world.
    this.world = new World(new Vector2(0, 0), true);
    // Create map loader for tiled map.
    maploader = new TmxMapLoader();
    // Load the tiled map.
    map = maploader.load("Map/Map.tmx");
    Map.create(map);
    renderer = new OrthogonalTiledMapRenderer(map);
    // Load all textures for render.
    CharacterRenderer.loadTextures();
    // Create a light control object.
    lightControl = new LightControl(world);
    // Create a new orthographic camera.
    camera = new OrthographicCamera();
    // Set the viewport area for camera.
    viewport = new FitViewport(screenSize.x, screenSize.y, camera);
    // Create a new background Render.
    backgroundRenderer = new BackgroundRenderer(game.getBatch(), viewport);
    // Create 2d box world for objects , walls, teleport...
    B2worldCreator.createWorld(world, map, this);
    // Set the contact listener for the world.
    world.setContactListener(new ObjectContactListener());
    // Create HUD.
    hud = new Hud(game.getBatch(), this);
    // TeleportMenu.
    teleportMenu = hud.teleportMenu;
    // HealthBar.
    healthBar = hud.healthBar;
    // Create a teleport_process instance.
    teleportProcess = new TeleportProcess(teleportMenu, player, map);
    // System_status_menu
    systemStatusMenu = hud.systemStatusMenu;
    // PowerupMenu
    powerupStatusMenu = hud.powerupMenu;
    // Generate all systems labels for status menu.
    systemStatusMenu.generate_systemLabels(systems);
    // Create arrest_status header.
    arrestedHeader = hud.arrestedHeader;
    // Create enemy_manager instance.
    enemyManager = new EnemyManager(world, map, systems);
    // Create Npc_manager instance.
    npcManager = new NpcManager(world, map);
    // Create powerup_manager instance
    powerupManager = new PowerupManager(world, map);

    if (!fromJson) {
      enemyManager.initialiseRandomEnemies();
      npcManager.generateNpcs();
    }
  }

  /**
   * Updates the game, logic will go here called by libgdx GameMain.
   */
  public void update() {

    // Vision powerup ability.
    if (player.visionActive) {
      if (!zoomedOut) {
        camera.zoom = 2;
        zoomedOut = true;
      }
    } else {
      camera.zoom = 1;
      zoomedOut = false;
    }

    // Arrest powerup ability
    if (player.arrestActive) {
      player.setArrestActive(false);
      if (enemyManager.arrestRandomEnemy(player)) {
        teleportProcess.jail_transform();
      }
    }

    // Repair powerup ability
    if (player.repairActive) {
      for (StationSystem sys : systems) {
        if (sys.isSabotaged() && player.repairActive) {
          sys.set_not_sabotaged();
          sys.hp = 100;
          player.setRepairActive(false);
        }
      }
    }

    float delta = Gdx.graphics.getDeltaTime();
    backgroundRenderer.update(delta);
    world.step(delta, 8, 3);
    hud.stage.act(delta);
    player.update(delta);
    teleportProcess.validate();
    healthBar.updateHp(player);
    lightControl.light_update(systems);
    DoorControl.updateDoors(systems, delta);
    enemyManager.updateEnemy(delta);
    npcManager.updateNpc(delta);
    powerupManager.updatePowerups(delta);
    systemStatusMenu.update_status(systems);
    powerupStatusMenu.update_powerup_status(player);
    arrestedHeader.update_Arrested(player);
    // If escape is pressed pause the game.
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      this.pause();
    }
    checkGameState();

  }

  @Override
  public void show() {
    // Set hud to be the input processor.
    Gdx.input.setInputProcessor(hud.stage);
  }

  private static final int[] backgroundLayers = new int[] { 0, 1, 2 };
  private static final int[] forgroundLayers = new int[] { 3 };

  @Override
  public void render(float delta) {

    // If the game is not paused, update it,
    // else if the pause menu indicates resume, resume the game,
    // else if the pause menu indicates exit, end the game.
    if (!this.paused) {
      update();
    } else if (this.hud.pauseMenu.resume()) {
      resume();
    } else if (this.hud.pauseMenu.exit()) {
      Gdx.app.exit();
    }

    // Clear the screen.
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Set camera follow the player.
    camera.position.set(0, 0, 0);
    camera.update();

    // This is needed to be called before the batch.begin(), or screen will freeze.
    game.getBatch().setProjectionMatrix(camera.combined);
    viewport.apply();
    backgroundRenderer.render();

    // Set camera follow the player.
    camera.position.set(player.b2body.getPosition().x, player.b2body.getPosition().y, 0);
    camera.update();
    game.getBatch().setProjectionMatrix(camera.combined);

    // Render the tilemap background.
    renderer.setView(camera);
    renderer.render(backgroundLayers);

    // Begin the batch.
    game.getBatch().begin();
    // Render player.
    player.draw(game.getBatch());
    // Render infiltrators.
    enemyManager.renderEnemy(game.getBatch());
    // Render NPC.
    npcManager.renderNpc(game.getBatch());
    // Render powerups
    powerupManager.renderPowerup(game.getBatch());
    // End the batch.
    game.getBatch().end();
    // Render tilemap that should appear in front of the player.
    renderer.render(forgroundLayers);
    // Render the light.
    lightControl.rayHandler.render();
    // Render the hud.
    hud.viewport.apply();
    hud.stage.draw();

  }

  @Override
  public void resize(int width, int height) {

    viewport.update(width, height);

    hud.resize(width, height);
  }

  /**
   * To pause the game set the paused flag and show the pause menu.
   */
  @Override
  public void pause() {
    this.paused = true;
    this.hud.pauseMenu.show();
  }

  /**
   * To resume the game set the pause flag and hide the pause menu.
   */
  @Override
  public void resume() {
    this.paused = false;
    this.hud.pauseMenu.hide();
  }

  /**
   * Check whether game ends.
   */
  public void checkGameState() {

    if (player.arrestedCount == 8) {
      game.setScreen(new WinLoseScreen(game.getBatch(), "YOU WIN!!"));
    }
    int sabotagedCount = 0;
    for (StationSystem system : systems) {
      if (system.isSabotaged()) {
        sabotagedCount++;
      }
    }

    // The 15 appears to be by design. Doors and healing pod cannot be sabotaged
    if (sabotagedCount >= 15 || player.health <= 1) {
      game.setScreen(new WinLoseScreen(game.getBatch(), "YOU LOSE!!"));
    }
  }

  @Override
  public void write(Json json) {
    json.writeValue("systems", systems);
    json.writeValue("enemy_manager", enemyManager);
    json.writeValue("npc_manager", npcManager);
    json.writeValue("player", player);
    json.writeValue("sabotage_rate", SABOTAGE_RATE);
  }

  /**
   * This is blank for a reason. For the JSON read method of Gameplay see
   * the from file method from Serializer.java.
   */
  @Override
  public void read(Json json, JsonValue jsonMap) {}
}
