package ai;

import com.badlogic.gdx.physics.box2d.World;

public class NPC extends AICharacter{
    public float dest_x;
    public float dest_y;
    public static int numberof_crew;
    /**
     * NPC object
     * @param world
     * @param name
     * @param x the initial spawn position x
     * @param y the initial spawn position y
     */
    public NPC(World world,String name, float x, float y){
        super(world,name,x,y);
        numberof_crew ++;
        super.b2body.setUserData("crew" + numberof_crew);
        dest_x = x;
        dest_y = y;
    }

    /**
     * set npc's destination
     * @param x
     * @param y
     */
    public void set_Dest( float x, float y){
        this.dest_x = x;
        this.dest_y = y;
    }

    /**
     * move npc to the destination
     */
    public void move_toDest(){
        goTo(dest_x,dest_y);
    }


}
