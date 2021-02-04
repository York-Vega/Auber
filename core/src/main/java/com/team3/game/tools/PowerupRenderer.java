//package com.team3.game.tools;
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.Array;
//
///**
// * PowerupRenderer is used to render powerups. for each powerup a new instance
// * should be created.
// *
// * Before use make sure to call PowerupRenderer.loadTextures()
// * to statically load all of the textures.
// */
//public class PowerupRenderer {
//
//    private static TextureAtlas atlas;
//
//    private Sprite sprite;
//    private TextureRegion currentFrame;
//
//    /**
//     * Sprites the PowerupRenderer can draw.
//     */
//    public enum Sprite {
//        POWERUP_speed(0), POWERUP_vision(1), POWERUP_repair(2), POWERUP_heal(3), POWERUP_arrest(4);
//
//        int index;
//
//        Sprite(int i) {
//            this.index = i;
//        }
//    }
//
//    /**
//     * Loads all textures needed by the Powerup Render should be called before game starts.
//     */
//    public static void loadTextures() {
//        atlas = new TextureAtlas("sprites/powerupSprites.atlas");
//
//        for (Sprite s : Sprite.values()) {
//                Array<AtlasRegion> tex = atlas.findRegions(s.name());
//        }
//    }
//    /**
//     * Creates a new Powerup renderer instance.
//
//     * @param sprite The PowerupRenderer.Sprite to use when drawing the character
//     */
//    public PowerupRenderer(Sprite sprite) {
//        this.sprite = sprite;
//    }
//
//    /**
//     * Renders the Powerup at the given position using the given SpriteBatch.
//
//     * @param position The bottom left of the powerup
//     * @param batch SpriteBatch to be drawn with, make sure to use SpriteBatch.begin() before hand
//     */
//    public void render(Vector2 position, SpriteBatch batch) {
//        int width = 24 * 2;
//        int height = 24 * 2;
//        batch.draw(currentFrame, position.x, position.y, width, height);
//    }
//
//}

package com.team3.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * CharacterRenderer is used to render player and npc characters. for each character a new instance
 * should be created.
 *
 * <p>Before use make sure to call CharacterRenderer.loadTextures()
 * to statically load all of the textures.
 */
public class PowerupRenderer {

    private static TextureAtlas atlas;
    private static Object[][] animations;

    private Sprite sprite;
    private State state;
    private Boolean hidden;
    private Float timeInState;
    private TextureRegion currentFrame;

    /**
     * Sprites the CharacterRenderer can draw.
     */
    public enum Sprite {
        POWERUP(0);

        int index;

        Sprite(int i) {
            this.index = i;
        }
    }

    /**
     * Possible character animation cycles.
     */
    public enum State {
        speed(0), vision(1), repair(2), heal(3), arrest(4), hidden(5);

        int index;

        State(int i) {
            this.index = i;
        }
    }

    /**
     * Loads all textures needed by the Character Render should be called before game starts.
     */
    public static void loadTextures() {
        atlas = new TextureAtlas("sprites/powerupSprites.atlas");

        animations = new Object[Sprite.values().length][State.values().length];
        for (Sprite s : Sprite.values()) {
            System.out.println(s);
            for (State t : State.values()) {
                System.out.println(t);
                Array<AtlasRegion> tex = atlas.findRegions(s.name() + "_" + t.name());
                animations[s.index][t.index] =
                        new Animation<TextureRegion>(0.10f, tex, PlayMode.LOOP);
            }
        }
    }

    /**
     * Creates a new Character renderer instance.

     * @param sprite The CharacterRenderer.Sprite to use when drawing the character
     */
    public PowerupRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.hidden = false;
        state = State.speed;
        timeInState = 0f;
    }

    private PowerupRenderer.State nextState;

    /**
     * Updates the animation based on time passed and character movement.

     * @param dt Time in seconds since last update call
     * @param type Type of powerup.
     */
    @SuppressWarnings("unchecked")
    public void update(float dt, String type) {
        timeInState += dt;

        int temp = 0;
        switch(type) {
            case "speed":
                temp = 0;
                break;
            case "vision":
                temp = 1;
                break;
            case "repair":
                temp = 2;
                break;
            case "heal":
                temp = 3;
                break;
            case "arrest":
                temp = 4;
                break;
            case "hidden":
                temp = 5;
            default:
                break;
        }

        Animation<TextureRegion> animation =
                (Animation<TextureRegion>) animations[sprite.index][temp];
        currentFrame = animation.getKeyFrame(timeInState, true);
    }

    /**
     * Renders the character at the given position using the given SpriteBatch.

     * @param position The bottom left of the character
     * @param batch SpriteBatch to be drawn with, make sure to use SpriteBatch.begin() before hand
     */
    public void render(Vector2 position, SpriteBatch batch) {
        int width = currentFrame.getRegionWidth() * 2;
        int height = currentFrame.getRegionHeight() * 2;
        if (!this.hidden) {
            batch.draw(currentFrame, position.x, position.y, width, height);
        }
    }

}