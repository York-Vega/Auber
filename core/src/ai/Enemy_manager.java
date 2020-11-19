package ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Manage enemies in the game
 */
public class Enemy_manager {

    public World world;
    public TiledMap map;
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<float[]> spawn_position = new ArrayList<>();
    public static ArrayList<float[]> target_position = new ArrayList<>();
    public static ArrayList<Systems> systems = new ArrayList<>();
    public static HashMap<Systems,Enemy> information;

    public Enemy_manager(World world,TiledMap map,ArrayList<Systems> systems){
        this.world = world;
        this.map = map;
        this.systems = systems;
        this.information = new HashMap<>();
        generate_spwan_position(map);
        generate_enemy(world);
        initial_sabotageTarget(systems);
    }

    /**
     * generate random start position for enemies
     * @param map
     */
    public void generate_spwan_position(TiledMap map){

        MapLayer enemySpawn = map.getLayers().get("npcSpawns");

        while(spawn_position.size() < 8){
            for (MapObject object : enemySpawn.getObjects()){
                Rectangle point = ((RectangleMapObject) object).getRectangle();
                float[] position = new float[]{point.x,point.y};
                double randomPro = Math.random();
                if (randomPro > 0.5 && !spawn_position.contains(position)){
                    spawn_position.add(position);
                }
            }
        }

    }

    /**
     * create Enemy instance and store in Arraylist enemy
     * @param world
     */
    public void generate_enemy(World world){

        for(int i = 0; i < 8; i ++){
            float[] position = spawn_position.get(i);
            // pic needs to be changed with enemy pic
            Enemy enemy = new Enemy(world,"player.png",position[0],position[1]);
            enemies.add(enemy);

        }

    }

    /**
     * target different systems for AI to sabotage, this should generate initial 8 targets for NPC
     * @param map
     */
    public void initial_sabotageTarget(ArrayList<Systems> systems){

        ArrayList<Integer> random_index = new ArrayList<>();
        // generate random target positions
        for (int i = 0; i < 8; i++){
            // generate a double  [0,1]
            double random_d = Math.random();
            // generate a index [0,15]
            int index =(int) (random_d * 15);
            // take away healing pod for initial traget, for difficulty
            while (random_index.contains(index) && !systems.get(index).sys_name.equals("headlingPod")){
                random_d = Math.random();
                index = (int) (random_d * 15);
            }
            random_index.add(index);
        }

        // set targets
        for (int i = 0; i < random_index.size(); i ++){
            int index = random_index.get(i);
            Systems sys = systems.get(index);

            float end_X = sys.getposition()[0];
            float end_Y = sys.getposition()[1];

            Enemy enemy = enemies.get(i);
            // set the target
            enemy.set_target_system(sys);
            // set the destination
            enemy.setDest(end_X,end_Y);
            enemy.move_toDest();
            // update the information hash table, aviod enemy targeting the same system
            information.put(sys,enemy);

        }

    }

    /**
     * render the enemy, should be called in gameplay render loop
     * @param game
     */
    public void render_ememy(SpriteBatch batch){

        for(Enemy enemy: enemies){
            enemy.draw(batch);
        }

    }

    /**
     * update the enemy, should be called in gameplay update
     * @param delta
     */
    public void update_enemy(float delta){

        for(Enemy enemy: enemies){

            // get targeted system object
            Systems sys = enemy.get_target_system();
            // if no system left to sabotage, should start attacking auber
            if (sys == null){
                continue;
            }
            if (enemy.is_attcking_mode()){
                enemy.sabotage(sys);
            }
            // generate next traget if system sabotaged
            if (sys.is_sabotaged()){
                generateNextTarget(enemy);
            }
            enemy.update(delta);
        }

    }


    /**
     * If enemy successfuly sabotage one target, generate next target for it
     * @param enemy
     */
    public void generateNextTarget(Enemy enemy){
        for (Systems system: this.systems ){
            if (!information.containsKey(system)){
                float end_X = system.getposition()[0];
                float end_Y = system.getposition()[1];
                enemy.setDest(end_X,end_Y);
                enemy.set_target_system(system);
                information.put(system,enemy);
                enemy.move_toDest();
                // set enemy back to standBy mode before it contacts with the next target system, otherwise the system will lose HP before contact
                enemy.set_standByMode();
                return;
            }
        }
        // if there is no systems left for sabotaging, set enemy to standby mode and remove the target system
        enemy.set_standByMode();
        enemy.target_system = null;
    }


}
