package com.team3.game.characters.ai;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Initializes an npc object.
 */
public class Npc extends AiCharacter {
  public static int numberOfCrew;

  /**
   * NPC object.
   *
   * @param world The game world
   * @param x The initial spawn position x
   * @param y The initial spawn position y
   */
  public Npc(World world, float x, float y) {
    super(world, x, y);
    numberOfCrew++;
    super.b2body.setUserData("crew" + numberOfCrew);
    super.b2body.getFixtureList().get(0).setSensor(true);
    destX = x;
    destY = y;
  }
}
