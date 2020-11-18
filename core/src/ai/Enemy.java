package ai;

import auber.Player;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;

public class Enemy extends AICharacter{

    public float dest_x;
    public float dest_y;

    /**
     *  Enemy
     * @param world
     * @param name
     * @param x position x
     * @param y position y
     */
    public Enemy(World world, String name, float x, float y){
        super(world,name,x,y);
        this.dest_x = 0;
        this.dest_y = 0;
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
     * ability to sabotage the system
     * @param system
     */
    public void sabotage(Systems system){
        if(system.hp > 0f){
            system.hp --;
        }
    }

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
