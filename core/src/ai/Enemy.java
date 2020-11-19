package ai;

import auber.Player;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;

public class Enemy extends AICharacter{

    public float dest_x;
    public float dest_y;
    public Systems target_system;
    public Systems current_contact_system; // used for contact listener
    public String mode;
    public static int numberofInfiltrators;
    /**
     *  Enemy
     * @param world
     * @param name
     * @param x position x
     * @param y position y
     */
    public Enemy(World world, String name, float x, float y){
        super(world,name,x,y);
        this.dest_x = x;
        this.dest_y = y;
        numberofInfiltrators ++;
        this.b2body.setUserData("Infiltrators" + numberofInfiltrators);
        mode = "";
    }


    /**
     * set the destinated postion
     * @param x
     * @param y
     */
    public void setDest(float x, float y){
        this.dest_x = x;
        this.dest_y = y;
    }

    /**
     * move to destination
     */
    public void move_toDest(){
        goTo(dest_x,dest_y);
    }

    /**
     * set sabotage system target
     * @param system
     */
    public void set_target_system(Systems system){
        target_system = system;
    }

    /**
     *
     * @return targeted system
     */
    public Systems get_target_system(){
        return target_system;
    }

    /**
     * ability to sabotage the system
     * @param system
     */
    public void sabotage(Systems system){
        if(system.hp > 0){
            system.hp -=1;
        }
        else{
            system.set_sabotaged();
        }
    }

    /**
     * set enemy to attcking mode
     */
    public void set_attackSystemMode(){
        mode = "attacking_system";
    }

    /**
     * set enemy to standby mode
     */
    public void set_standByMode(){
        mode = "";
    }

    /**
     * check enemy is attcking a system or not
     * @return
     */
    public boolean is_attcking_mode(){
        return mode.equals("attacking_system");
    }

    /**
     * check enemy is standingby or not
     * @return
     */
    public boolean is_standBy_mode(){
        return mode.equals("");
    }

    /**
     * set enemy to arrested
     */
    public void set_ArrestedMode(){
        mode = "arrested";
    }

    public boolean isArrested(){
        return mode.equals("arrested");
    }


    // TO DO
    // Enemies special abilities
    // ...

    /**
     * ability to slow down auber
     * @param auber
     */
    public void ability_slower_player(Player auber){
        auber.playerSpeed -= 20f;
    }

    /**
     * increase its own speed
     */
    public void ability_speeding(){
        this.speed  += 100f;
    }




}
