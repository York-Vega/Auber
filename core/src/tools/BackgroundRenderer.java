package tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackgroundRenderer {

    private TextureAtlas atlas = new TextureAtlas("background/background.atlas");
    private SpriteBatch batch;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private Viewport viewport;

    /**
     * A class to handle rendering the space background.

     * @param batch spritebatch to draw with
     */
    public BackgroundRenderer(SpriteBatch batch, Viewport viewport) {
        this.viewport = viewport;
        this.batch = batch;
        animation = 
            new Animation<TextureRegion>(0.25f, atlas.findRegions("stars"), PlayMode.LOOP);
        
    }

    private float timeElapsed = 0f;

    /**
     * updates the animation.

     * @param delta secconds since last update
     */
    public void update(float delta) {

        timeElapsed += delta;
        currentFrame = animation.getKeyFrame(timeElapsed, true);
    }

    int width;
    int height;
    final int tileWidth = 160;

    /**
     * renders the background.
     */
    public void render() {
        batch.begin();
        width = viewport.getScreenWidth();
        height = viewport.getScreenHeight();
        for (int x = 0; x <= width; x += tileWidth) {
            for (int y = 0; y <= height; y += tileWidth) {
                batch.draw(currentFrame, x, y, tileWidth, tileWidth);
            }
        }
        
        batch.end();

    }
    
}
