package com.team3.game.tests;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.math.Vector2;
import com.team3.game.GdxTestRunner;
import com.team3.game.characters.Player;
import com.team3.game.tools.CharacterRenderer;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;


@RunWith(GdxTestRunner.class)
public class TestMovement {

  Player player = new Player(null, 0, 0);
  CharacterRenderer.Sprite sprite;
  CharacterRenderer renderer = new CharacterRenderer(sprite);

  @Test
  public void testMovement() throws AWTException {
    Robot robot = new Robot();  // Create instance of robot class
    int keyCode = KeyEvent.VK_UP; // The up arrow key
    robot.keyPress(keyCode);
    player.update(1);
    assertEquals(((Vector2) Whitebox.getInternalState(player, "position")).y, 60f);
    robot.delay(40);
    robot.keyRelease(keyCode);
  }
}