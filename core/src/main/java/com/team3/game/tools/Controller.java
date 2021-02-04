package com.team3.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Allows for user input to control the character.
 */
public abstract class Controller {
  // Initial controls.
  private static int up = Input.Keys.W;
  private static int down = Input.Keys.S;
  private static int left = Input.Keys.A;
  private static int right = Input.Keys.D;

  public static boolean isUpPressed() {
    return Gdx.input.isKeyPressed(up);
  }

  public static boolean isDownPressed() {
    return Gdx.input.isKeyPressed(down);
  }

  public static boolean isLeftPressed() {
    return Gdx.input.isKeyPressed(left);
  }

  public static boolean isRightPressed() {
    return Gdx.input.isKeyPressed(right);
  }

  public static boolean isArrestPressed() { 
    return Gdx.input.isKeyPressed(Input.Keys.E);
  }

  public static boolean isPowerupAbilityPressed() {
    return Gdx.input.isKeyPressed(Input.Keys.Q);
  }

  /**
   * Sets the controls of the user.
   *
   * @param u Up key string
   * @param d Down key string
   * @param l Left key string
   * @param r Right key string
   */
  public static void changeControls(String u, String d, String l, String r) {
    int newUp = Input.Keys.valueOf(u.toString()); 
    if (newUp != -1) {
      up = newUp;
    }

    int newDown = Input.Keys.valueOf(d.toString()); 
    if (newDown != -1) {
      down = newDown;
    }

    int newLeft = Input.Keys.valueOf(l.toString()); 
    if (newLeft != -1) {
      left = newLeft;
    }

    int newRight = Input.Keys.valueOf(r.toString()); 
    if (newRight != -1) {
      right = newRight;
    }

  } 

  public static String up() {
    return Input.Keys.toString(up);
  }

  public static String down() {
    return Input.Keys.toString(down);
  }

  public static String left() {
    return Input.Keys.toString(left);
  }

  public static String right() {
    return Input.Keys.toString(right);
  }


}
