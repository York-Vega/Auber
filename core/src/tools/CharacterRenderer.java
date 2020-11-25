package tools;

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
 * to staically load all of the textures.
 */
public class CharacterRenderer {

    private static TextureAtlas atlas;
    private static Object[][] animations;

    private Sprite sprite;
    private State state;
    private Boolean right;
    private Float timeInState;
    private TextureRegion currentFrame;

    /**
     * Sprites the CharacterRenderer can draw.
     */
    public enum Sprite {
        AUBER(0), NPC1(1), NPC2(2), NPC3(3);

        int index;

        Sprite(int i) {
            this.index = i;
        }
    }

    /**
     * Possible character animation cycles.
     */
    public enum State {
        idle(0), move(1), kick(2), hurt(3), crouch(4), sneak(5);

        int index;

        State(int i) {
            this.index = i;
        }
    }

    /**
     * Loads all textures needed by the Character Render should be called before game starts.
     */
    public static void loadTextures() {
        atlas = new TextureAtlas("sprites/dinoSprites.atlas");

        animations = new Object[Sprite.values().length][State.values().length];
        for (Sprite s : Sprite.values()) {
            for (State t : State.values()) {
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
    public CharacterRenderer(Sprite sprite) {
        this.sprite = sprite;
        right = false;
        state = State.idle;
        timeInState = 0f;
    }

    private State nextState;

    /**
     * Updates the animation based on time passed and character movement.

     * @param dt time in secconds since last update call
     * @param movement the direction of movement. don't pass velocity but instead acceleration
     */
    @SuppressWarnings("unchecked")
    public void update(float dt, Vector2 movement) {
        timeInState += dt;

        if (movement == null) {
            movement = new Vector2();
        }
        
        if (movement.len2() > 0.1) {
            nextState = State.move;
            if (movement.x >= 0) {
                right = true;
            } else {
                right = false;
            }
        } else if (state != State.crouch) {
            nextState = State.idle;
            if (timeInState > 5f) {
                nextState = State.crouch;
            }
        }
        if (state != nextState) {
            timeInState = 0f;
            state = nextState;
        }

        
        Animation<TextureRegion> animation = 
            (Animation<TextureRegion>) animations[sprite.index][state.index];
        currentFrame = animation.getKeyFrame(timeInState, true);
        
            
    }

    /**
     * Reders the character at the given position using the given SpriteBatch.

     * @param position the bottom left of the character
     * @param batch SpriteBatch to be drawn with, make sure to use SpriteBatch.begin() before hand
     */
    public void render(Vector2 position, SpriteBatch batch) {
        int width = currentFrame.getRegionWidth() * 2;
        int height = currentFrame.getRegionHeight() * 2;
        if (right) {
            batch.draw(currentFrame, position.x, position.y, width, height);
        } else {
            batch.draw(currentFrame, position.x + width, position.y, -width, height);
        }
    }

}
