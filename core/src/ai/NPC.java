package ai;

import com.badlogic.gdx.physics.box2d.World;

public class NPC extends AICharacter{
    public float dest_x;
    public float dest_y;
    /**
     * NPC object
     * @param world
     * @param name
     * @param x the initial spawn position x
     * @param y the initial spawn position y
     */
    public NPC(World world,String name, float x, float y){
        super(world,name,x,y);
        dest_x = 0;
        dest_y = 0;
    }


}
