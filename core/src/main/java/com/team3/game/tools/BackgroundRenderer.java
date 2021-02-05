package com.team3.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Renders the background of the game.
 */
public class BackgroundRenderer {

  private TextureAtlas atlas = new TextureAtlas("background/background.atlas");
  private SpriteBatch batch;
  private Animation<TextureRegion> animation;
  private TextureRegion currentFrame;
  private Viewport viewport;

  /**
   * A class to handle rendering the space background.

   * @param batch Spritebatch to draw with
   * @param viewport The viewport to render the background using
   */
  public BackgroundRenderer(SpriteBatch batch, Viewport viewport) {
    this.viewport = viewport;
    this.batch = batch;
    animation = 
      new Animation<TextureRegion>(0.25f, atlas.findRegions("stars"), PlayMode.LOOP);

  }

  private float timeElapsed = 0f;

  /**
   * Updates the animation.

   * @param delta Seconds since last update
   */
  public void update(float delta) {

    timeElapsed += delta;
    currentFrame = animation.getKeyFrame(timeElapsed, true);
  }

  float width;
  float height;
  int tileWidth = 160;

  /**
   * Renders the background.
   */
  public void render() {
    batch.begin();
    width = viewport.getWorldWidth() * 2;
    height = viewport.getWorldHeight() * 2;
    tileWidth = (int) (width / 5);
    for (float x = -width / 2; x <= width / 2; x += tileWidth) {
      for (float y = -height / 2; y <= height / 2; y += tileWidth) {
        batch.draw(currentFrame, x, y, tileWidth, tileWidth);
      }
    }

    batch.end();

  }

}
