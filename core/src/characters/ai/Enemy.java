package characters.ai;

import characters.Player;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;

public class Enemy extends AiCharacter {

    public Systems targetSystem;
    public Systems currentContactSystem; // used for contact listener
    public String mode;
    public static int numberofInfiltrators;
    /**
     * Enemy.

     * @param world The game world
     *
     * @param x position x
     *
     * @param y position y
     */
    public Enemy(World world, float x, float y) {
        super(world, x, y);
        this.destX = x;
        this.destY = y;
        numberofInfiltrators++;
        this.b2body.setUserData("Infiltrators" + numberofInfiltrators);
        mode = "";
    }

    /**
     * set sabotage system target.
     *
     * @param system Systems Arraylist
     */
    public void set_target_system(Systems system) {
        targetSystem = system;
    }

    /**
     * Get target system object.
     *
     * @return targeted system
     */
    public Systems get_target_system() {
        return targetSystem;
    }

    /**
     * ability to sabotage the system.
     *
     * @param system system object
     */
    public void sabotage(Systems system) {
        if (system.hp > 0) {
            system.hp -= 1;
        } else {
            system.set_sabotaged();
        }
    }

    /**
     * set enemy to attcking mode.
     */
    public void set_attackSystemMode() {
        mode = "attacking_system";
    }

    /**
     * set enemy to standby mode.
     */
    public void set_standByMode() {
        mode = "";
    }

    /**
     * check enemy is attcking a system or not.
     *
     * @return true if it is in attacking mode
     */
    public boolean is_attcking_mode() {
        return mode.equals("attacking_system");
    }

    /**
     * check enemy is standby or not.
     *
     * @return true if it is in standby mode
     */
    public boolean is_standBy_mode() {
        return mode.equals("");
    }

    /**
     * set enemy to arrested.
     */
    public void set_ArrestedMode() {
        mode = "arrested";
    }

    public boolean isArrested() {
        return mode.equals("arrested");
    }


    // TO DO
    // Enemies special abilities
    // ...

    /**
     * ability to slow down auber.
     *
     * @param auber Player object
     */
    public void ability_slower_player(Player auber) {
        auber.speed -= 20f;
    }

    /**
     * increase its own speed.
     */
    public void ability_speeding() {
        this.speed  += 100f;
    }




}
