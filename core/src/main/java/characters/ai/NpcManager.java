package characters.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.Random;


/**
 * manage npcs in the game.
 */
public class NpcManager {


    public World world;
    public TiledMap map;
    public static ArrayList<Npc> npcs = new ArrayList<>();
    public static ArrayList<float[]> spawnPositions = new ArrayList<>();


    /**
     * Instantiates a new NPC Manager.

     * @param world The game world
     * @param map the tiled map
     */
    public NpcManager(World world, TiledMap map) {

        this.world = world;
        this.map = map;
        generate_initialPosition(map);
        generateNpc(world);
    }

    /**
     * generate random spawn positions for npc.

     * @param map The world map to generate inital position
     */
    public void generate_initialPosition(TiledMap map) {

        MapLayer npcSpawn = map.getLayers().get("npcSpawns");

        while (spawnPositions.size() < 20) {
            for (MapObject object : npcSpawn.getObjects()) {
                Rectangle point = ((RectangleMapObject) object).getRectangle();
                float [] position = new float[]{point.x, point.y};
                double randomPo = Math.random();
                if (randomPo > 0.5 && !spawnPositions.contains(position)) {
                    spawnPositions.add(position);
                }
            }
        }
    }

    /**
     * create NPC in box2D world and set initial destination for npcs.

     * @param world The game world
     */
    public void generateNpc(World world) {

        int destcount = 1;

        for (int i = 0; i < spawnPositions.size(); i++) {

            float[] position = spawnPositions.get(i);
            // set destination for npc
            if (i == spawnPositions.size() - 1) {
                destcount = 0;
            }
            float[] dest = spawnPositions.get(destcount);
            destcount += 1;
            // pic for NPC needed
            Npc npc = new Npc(world, position[0], position[1]);
            npc.setDest(dest[0], dest[1]);
            npc.moveToDest();
            npcs.add(npc);

        }

    }

    /**
     * render the npc, should be called in render loop.

     * @param batch the SpriteBatch to draw the npc to
     */
    public void renderNpc(SpriteBatch batch) {
        for (Npc npc : npcs) {
            npc.draw(batch);
        }

    }


    /**
     * update npc, should be called in GamePlay update.

     * @param delta The time in secconds since the last update
     */
    public void updateNpc(float delta) {

        for (Npc npc : npcs) {
            if (!npc.isMoving()) {
                generateNextPosition(npc);
            }
            npc.update(delta);
        }

    }

    /**
     * Generates the next random position an npc will go to.

     * @param npc the npc to randomly pathfind
     */
    public void generateNextPosition(Npc npc) {
        int index;
        Random random = new Random();
        index = random.nextInt(20);

        float [] destination = spawnPositions.get(index);
        npc.setDest(destination[0], destination[1]);
        npc.moveToDest();
    }


}
