package com.team3.game.tests;

import static org.junit.Assert.assertTrue;

import com.team3.game.GdxTestRunner;
import com.team3.game.characters.Player;
import com.team3.game.characters.ai.Enemy;
import com.team3.game.tools.ObjectContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestArrest {
  
  
  @Test
  public void testArrest() throws AWTException {
    
    /*Code has error where it won't run due to being unable to load & read gdx-box2d64. 
    This is present as of the latest pull request*/

    World world = new World(new Vector2(0, 0), true);
    Player player = new Player(world, 0, 0);
    Enemy enemy = new Enemy(world, 0, 0);
    Robot robot = new Robot();  // Create instance of robot class
    int keyCode = KeyEvent.VK_A; // The A key
    world.setContactListener(new ObjectContactListener());
    
    robot.keyPress(keyCode);
    player.update(1);
    robot.delay(40);
    robot.keyRelease(keyCode);
    
    assertTrue("Test unsuccessful, enemy was not arrested", enemy.isArrested());
  }
}
