package ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;



/**
 * manage npcs in the game
 */
public class Npc_manager {


    public World world;
    public TiledMap map;
    public ArrayList<NPC> npcs = new ArrayList<>();
    public ArrayList<float[]> spawn_position = new ArrayList<>();


    public Npc_manager(World world,TiledMap map){

        this.world = world;
        this.map = map;
        generate_initialPosition(map);
        generateNPC(world);
    }

    /**
     * generate random spawn positions for npc
     */
    public void generate_initialPosition(TiledMap map){

        MapLayer npcSpawn = map.getLayers().get("npcSpawns");

        while (spawn_position.size() < 20){
            for (MapObject object : npcSpawn.getObjects()){
                Rectangle point = ((RectangleMapObject) object).getRectangle();
                float [] position = new float[]{point.x,point.y};
                double randomPo = Math.random();
                if (randomPo > 0.5 && !spawn_position.contains(position)){
                    spawn_position.add(position);
                }
            }
        }
    }

    /**
     * create NPC in box2D world and set initial destination for npcs
     * @param world
     */
    public void generateNPC(World world){

        int destcount = 1;

        for(int i = 0; i < spawn_position.size(); i ++){

            float[] position = spawn_position.get(i);
            // set destination for npc
            float[] dest = spawn_position.get(destcount);
            destcount += 1;
            // pic for NPC needed
            NPC npc = new NPC(world,"",position[0],position[1]);
            npc.set_Dest(dest[0],dest[1]);
            npc.move_toDest();
            npcs.add(npc);

        }

    }

    /**
     * render the npc, should be called in render loop
     * @param batch
     */
    public void render_npc(SpriteBatch batch){

        for (NPC npc: npcs){
            npc.draw(batch);
        }

    }


    /**
     * update npc, should be called in GamePlay update
     * @param delta
     */
    public void update_NPC(float delta){

        for(NPC npc: npcs){

            Vector2 position = npc.b2body.getPosition();
            float dest_x = npc.dest_x;
            float dest_y = npc.dest_y;
            if (position.x == dest_x && position.y == dest_y){
                generateNextPosition(npc);
            }
            npc.update(delta);

        }

    }

    public void generateNextPosition(NPC npc){
        int random = (int) Math.random();
        float [] destination = spawn_position.get(random * 20);
        npc.set_Dest(destination[0],destination[1]);
        npc.move_toDest();
    }


}
