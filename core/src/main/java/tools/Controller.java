package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public abstract class Controller {
    // initial controls
    private static int up = Input.Keys.UP;
    private static int down = Input.Keys.DOWN;
    private static int left = Input.Keys.LEFT;
    private static int right = Input.Keys.RIGHT;

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

    public static boolean isArrestPressed() { return Gdx.input.isKeyPressed(Input.Keys.A); }

    /**
     * sets the controls of the user
     * @param u up key string
     * @param d down key string
     * @param l left key string
     * @param r right key string
     */
    public static void changeControls(String u, String d, String l, String r) {
        int newUp = Input.Keys.valueOf(u.toString()); 
        int newDown = Input.Keys.valueOf(d.toString()); 
        int newLeft = Input.Keys.valueOf(l.toString()); 
        int newRight = Input.Keys.valueOf(r.toString()); 

        if (newUp != -1) {
            up = newUp;
        }
        if (newDown != -1) {
            down = newDown;
        }
        if (newLeft != -1) {
            left = newLeft;
        }
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