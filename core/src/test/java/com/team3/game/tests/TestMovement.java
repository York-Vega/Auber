package com.team3.game.tests;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
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
  public Robot robot;

  public TestMovement() throws AWTException {
    robot = new Robot(); // Create instance of robot class.
  }

  @Test
  public void testMoveUp() throws AWTException {
    CharacterRenderer.loadTextures();
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    float speed = 6f;
    Vector2 input = new Vector2(0, 0); // Stores expected movement.
    Robot robot = new Robot(); // Create instance of robot class.
    int keyCode = KeyEvent.VK_UP; // The up arrow key.
    robot.keyPress(keyCode);
    
    input.add(0, speed); // Move up.
    player.update(1);
    robot.delay(40);
    robot.keyRelease(keyCode);

    assertEquals(input.y, ((Vector2) Whitebox.getInternalState(player, "position")).y, 0.1f);
  }

  @Test
  public void testMoveDown() throws AWTException {
    CharacterRenderer.loadTextures();
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    float speed = 6f;
    Vector2 input = new Vector2(0, 0); // Stores expected movement.
    int keyCode = KeyEvent.VK_DOWN; // The down arrow key.
    robot.keyPress(keyCode);

    input.add(0, -speed); // Move down.
    player.update(1);
    robot.delay(40);
    robot.keyRelease(keyCode);

    assertEquals(input.y, ((Vector2) Whitebox.getInternalState(player, "position")).y, 0.1f);
  }

  @Test
  public void testMoveLeft() throws AWTException {
    CharacterRenderer.loadTextures();
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    float speed = 6f;
    Vector2 input = new Vector2(0, 0); // Stores expected movement.
    Robot robot = new Robot(); // Create instance of robot class.
    int keyCode = KeyEvent.VK_LEFT; // The left arrow key.
    robot.keyPress(keyCode);

    input.add(-speed, 0); // Move left.
    player.update(1);
    robot.delay(40);
    robot.keyRelease(keyCode);

    assertEquals(input.x, ((Vector2) Whitebox.getInternalState(player, "position")).x, 0.1f);
  }

  @Test
  public void testMoveRight() throws AWTException {
    CharacterRenderer.loadTextures();
    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    float speed = 6f;
    Vector2 input = new Vector2(0, 0); // Stores expected movement.
    Robot robot = new Robot(); // Create instance of robot class.
    int keyCode = KeyEvent.VK_RIGHT; // The right arrow key.
    robot.keyPress(keyCode);

    input.add(speed, 0); // Move right.
    player.update(1);
    robot.delay(40);
    robot.keyRelease(keyCode);

    assertEquals(input.x, ((Vector2) Whitebox.getInternalState(player, "position")).x, 0.1f);
  }
}
