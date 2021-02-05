package com.team3.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3.game.screen.actors.ArrestedHeader;
import com.team3.game.screen.actors.HealthBar;
import com.team3.game.screen.actors.PauseMenu;
import com.team3.game.screen.actors.PowerupMenu;
import com.team3.game.screen.actors.SettingsMenu;
import com.team3.game.screen.actors.SystemStatusMenu;
import com.team3.game.screen.actors.TeleportMenu;


/**
 * Hud to display information.
 */
public class Hud {

  public Stage stage;
  public Viewport viewport;

  public HealthBar healthBar;
  public TeleportMenu teleportMenu;

  public SystemStatusMenu systemStatusMenu;

  public PauseMenu pauseMenu;
  public SettingsMenu settingsMenu;

  public ArrestedHeader arrestedHeader;

  public PowerupMenu powerupMenu;

  /**
   * Create a new instantiated hud.

   * @param spriteBatch The GamePlay batch
   * @param gameplay Gameplay parameter
   */
  public Hud(final SpriteBatch spriteBatch, Gameplay gameplay) {
    // Create a new viewport and a fixed camera for the stage.
    viewport = new FitViewport(1280, 720, new OrthographicCamera());
    viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    // Pass in the game spritebatch.
    stage = new Stage(viewport, spriteBatch);
    // Create a new table as a container to lay out the actors.
    Table table = new Table();
    // Pad the table to the top of screen.
    table.top();
    // Set the table same as the size of the stage.
    table.setFillParent(true);
    // Create TeleportMenu (SelectBox<String>) actor.
    teleportMenu = new TeleportMenu();
    // Create health bar (ProgressBar) actor.
    healthBar = new HealthBar();
    // Create a SystemStatusMenu (Vertical Group) actor.
    systemStatusMenu = new SystemStatusMenu();
    // Create a arrested count header actor.
    arrestedHeader = new ArrestedHeader();
    // Create a setting menu.
    settingsMenu = new SettingsMenu();
    // Create and add a pause menu to the stage.
    pauseMenu = new PauseMenu(settingsMenu, gameplay);
    // Create a SystemStatusMenu instance.
    systemStatusMenu = new SystemStatusMenu();
    // Create a PowerupMenu instance
    powerupMenu = new PowerupMenu();
    // Add teleport menu to the table
    table.add(teleportMenu).padLeft(20).width(Value.percentWidth(.2f, table));
    // Add hp text in front of bar, 20 is the space between hp text and teleport menu.
    table.add(healthBar.hpText).padLeft(20);
    // Add healthBar to the table, 5 is the space between hp text and health bar.
    table.add(healthBar).padLeft(5).width(Value.percentWidth(.2f, table));
    // Add arrest header to the table.
    table.add(arrestedHeader).padLeft(40).width(Value.percentWidth(.2f, table));

    // Add table to the stage.
    stage.addActor(table);
    // Add system_status_menu to the stage.
    stage.addActor(systemStatusMenu);
    stage.addActor(powerupMenu);
    stage.addActor(pauseMenu.pauseWindow());
    stage.addActor(settingsMenu.settingsWindow());

  }

  /**
   * Resize the viewport.

   * @param width  In pixels
   * @param height In pixels
   */
  public void resize(int width, int height) {

    viewport.update(width, height);

  }

}
