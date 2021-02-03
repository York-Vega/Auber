package com.team3.game.characters.ai;

import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.team3.game.characters.Character;
import com.team3.game.map.Distance;
import com.team3.game.map.Map;
import com.team3.game.map.Node;
import com.team3.game.map.Path;
import com.team3.game.tools.CharacterRenderer;

/**
 * AI Character object for the game.
 */
public class AiCharacter extends Character {

  public float destX;
  public float destY;

  private PathFinder<Node> pathFinder;
  private Path path;
  private int pathIndex;

  private static int numberOfHostiles = 0;
  private static CharacterRenderer.Sprite[] sprites = new CharacterRenderer.Sprite[] {
    CharacterRenderer.Sprite.NPC1,
    CharacterRenderer.Sprite.NPC2,
    CharacterRenderer.Sprite.NPC3
    };

  /**
   * Creates an semi-initialized AI character the physics body is still uninitiated.

   * @param world The game world
   *
   * @param x The initial x location of the character
   *
   * @param y The initial y location of the character
   */
  public AiCharacter(World world, float x, float y) {
    super(world, x, y, sprites[numberOfHostiles % 3]);

    // From what I can tell, the scale for the speed of AI characters differs from that for the
    // player
    speed = 1000.0f;
    AiCharacter.numberOfHostiles++;

    path = new Path();
    pathFinder = new IndexedAStarPathFinder<Node>(Map.graph);

  }

  /**
   * Creates the physics bodies for the character Sprite.
   */
  @Override
  public void createBody()  {
    super.createBody();
    b2body.setUserData("NPC_" + AiCharacter.numberOfHostiles);
    b2body.getFixtureList().get(0).setSensor(true);
    b2body.getFixtureList().get(0).setUserData(this);
  }


  /**
   * Updates the character, should be called every update cycle.

   * @param delta The time in seconds since the last update
   */
  @Override
  public void update(float delta)  {
    Vector2 direction = decideDirection();
    move(delta, direction);
    // position sprite properly within the box
    position.set(b2body.getPosition().x - size.x,
        b2body.getPosition().y - size.y + 4);

    renderer.update(delta, direction);

  }

  /**
   * Applies the move to the character.

   * @param delta Seconds since last update
   * @param direction Direction to move character
   */
  public void move(float delta, Vector2 direction) {

    // Applies a velocity of direction * time delta * speed.
    Vector2 vel = direction.scl(delta * this.speed);
    this.b2body.applyLinearImpulse(vel, this.b2body.getWorldCenter(), true);
  }

  /**
   * Decides the direction to be made by the AI.

   * @return A unit vector representing direction
   */
  private Vector2 decideDirection() {
    if (this.isMoving()) {
      Node target = path.get(pathIndex);
      int targetY = (target.getIndex() / Map.mapTileWidth) * Map.tilePixelHeight + 16;
      int targetX = (target.getIndex() % Map.mapTileWidth) * Map.tilePixelWidth + 16;

      float x = this.b2body.getPosition().x;
      float y = this.b2body.getPosition().y;

      float xcomp = 0;
      float ycomp = 0;

      // If the difference in x values between character and node is above 1
      // move in x direction n towards node.
      if (Math.abs(x - targetX) > 1) {
        xcomp = targetX - x;
      }
      // If the difference in y values between character and node is above 1
      // move in y direction n towards node.
      if (Math.abs(y - targetY) > 1) {
        ycomp = targetY - y;
      }

      // If the character is in the bounds of the node
      // target the next node.
      if (Math.abs(y - targetY) < 4 && Math.abs(x - targetX) < 4) {
        this.pathIndex++;
        return new Vector2(0, 0);
      }

      float abs = (float) Math.sqrt(Math.pow(xcomp, 2) + Math.pow(ycomp, 2));
      return new Vector2(xcomp / abs, ycomp / abs);
    }
    return new Vector2(0, 0);
  }

  /**
   * Set the destination position.

   * @param x x component
   * @param y y component
   */
  public void setDest(float x, float y) {
    destX = x;
    destY = y;
  }

  /**
   * Move to destination.
   */
  public void moveToDest() {
    goTo(destX, destY);
  }

  /**
   * Calculates if there is a path to the given position.

   * @param x x coordinate of destination in pixels
   * @param y y coordinate of destination in pixels
   * @return True if there is a path between character and destination, false otherwise
   */
  public boolean goTo(float x, float y) {
    Vector2 position = this.b2body.getPosition();

    Node startNode = Map.graph.getNodeByXy((int) position.x, (int) position.y);
    Node endNode = Map.graph.getNodeByXy((int) x, (int) y);

    // If the character is already at or moving to the destination.
    if (this.path.getCount() > 0) {
      int currentEndNode = this.path.get(this.path.getCount() - 1).getIndex();
      if (startNode.getIndex() == endNode.getIndex()
          || endNode.getIndex() == currentEndNode) {
        return true;
      }
    }

    // Resets the path.
    this.path = new Path();
    this.pathFinder = new IndexedAStarPathFinder<Node>(Map.graph);
    pathIndex = 1;

    // A* search between character and destination.
    pathFinder.searchNodePath(startNode, endNode, new Distance(), path);

    // If the path is empty.
    if (path.getCount() == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * If the character is currently moving along a path.

   * @return Whether the character is currently following a path
   */
  public boolean isMoving() {
    return this.pathIndex < this.path.getCount();
  }

  /**
   * Stops the character from following its path.
   */
  public void stop() {
    this.pathIndex = this.path.getCount();
  }

  @Override
  public void write(Json json) {
    super.write(json);
    json.writeValue("dest_x", destX);
    json.writeValue("dest_y", destY);
  }
}
