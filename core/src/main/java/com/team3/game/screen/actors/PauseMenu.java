package com.team3.game.screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.team3.game.screen.Gameplay;
import com.team3.game.tools.Serializer;

/**
 * Adds a pause menu to the game.
 */
public class PauseMenu extends Menu {
  private boolean exiting;
  TextButton resumeButton = new TextButton("Resume", skin);
  TextButton exitButton = new TextButton("Exit", skin);
  TextButton settingsButton = new TextButton("Settings", skin);
  TextButton saveButton = new TextButton("Save and Quit", skin);
  Label titleLabel = new Label("Paused", skin);
  SettingsMenu settingsMenu;
  Gameplay gameplay;

  /**
   * The pause menu for the game.
   *
   * @param settingsMenu The settings menu to include
   * @param gameplay Gameplay paramter
   */
  public PauseMenu(SettingsMenu settingsMenu, Gameplay gameplay) {
    super("Pause");

    this.gameplay = gameplay;
    this.settingsMenu = settingsMenu;

    // Adds actors to the menu.
    resumeButton.setName("Resume");
    exitButton.setName("Exit");
    settingsButton.setName("Settings");
    saveButton.setName("Save");
    titleLabel.setName("Title");

    window.add(resumeButton);
    window.add(exitButton);
    window.add(settingsButton);
    window.add(titleLabel);
    window.add(saveButton);

    window.findActor("Resume").setPosition(this.window.getWidth() / 2 
        - resumeButton.getWidth() / 2,
        this.window.getHeight() * 7 / 10 - resumeButton.getHeight() / 2);

    window.findActor("Settings").setPosition(this.window.getWidth() / 2 
        - settingsButton.getWidth() / 2,
        this.window.getHeight() * 5 / 10 - settingsButton.getHeight() / 2);

    window.findActor("Exit").setPosition(this.window.getWidth() / 2 
        - exitButton.getWidth() / 2,
        this.window.getHeight() * 3 / 10 - exitButton.getHeight() / 2);

    window.findActor("Title").setPosition(this.window.getWidth() / 2 
        - titleLabel.getWidth() / 2,
        this.window.getHeight() * 9 / 10 - titleLabel.getHeight() / 2);
    window.findActor("Save").setPosition(this.window.getWidth() / 2 
        - saveButton.getWidth() / 2,
        this.window.getHeight() * 1 / 10 - titleLabel.getHeight() / 2);
  }

  /**
   * Shows the menu.
   */
  @Override
  public void show() {
    super.show();

    // Event listeners for buttons.
    resumeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        paused = false;
      }
    });
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        exiting = true;
      }
    });
    settingsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        settingsMenu.show();
        PauseMenu.super.hide();
      }
    });
    saveButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Serializer.toFile("save", false, gameplay);
        exiting = true;
      }
    });
  }

  @Override
  public void hide() {
    super.hide();
    this.settingsMenu.hide();
  }

  public Window pauseWindow() {
    return this.window;
  }

  /**
   * If the game should resume.

   * @return Whether or not gameplay should resume
   */
  public boolean resume() {
    if (settingsMenu.resume() && this.paused) {
      this.show();
    }
    return (!this.paused) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);

  }

  public boolean exit() {
    return exiting;
  }
}
