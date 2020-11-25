package characters.ai;

import characters.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.HashMap;
import sprites.Systems;



/**
 * Manage enemies in the game.
 */
public class EnemyManager {

    public World world;
    public TiledMap map;
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<float[]> spawn_position = new ArrayList<>();
    public static ArrayList<float[]> target_position = new ArrayList<>();
    public static ArrayList<Systems> systems = new ArrayList<>();
    public static HashMap<Systems, Enemy> information;

    /**
     * EnemyManager to manage enemies behavior.

     *
     * @param world box2D world
     *
     * @param map Tiled map
     *
     * @param systems Arraylist Systems objects
     */
    public EnemyManager(World world, TiledMap map, ArrayList<Systems> systems) {
        this.world = world;
        this.map = map;
        EnemyManager.systems = systems;
        information = new HashMap<>();
        generate_spawn_position(map);
        generate_enemy(world);
        initial_sabotageTarget(systems);
    }

    /**
     * generate random start position for enemies.
     *
     * @param map TiledMap object
     */
    public void generate_spawn_position(TiledMap map) {

        MapLayer enemySpawn = map.getLayers().get("npcSpawns");

        while (spawn_position.size() < 8) {
            for (MapObject object : enemySpawn.getObjects()) {
                Rectangle point = ((RectangleMapObject) object).getRectangle();
                float[] position = new float[]{point.x, point.y};
                double randomPro = Math.random();
                if (randomPro > 0.5 && !spawn_position.contains(position)) {
                    spawn_position.add(position);
                }
            }
        }

    }

    /**
     * create Enemy instance and store in Arraylist enemy.
     *
     * @param world World object
     */
    public void generate_enemy(World world) {

        for (int i = 0; i < 8; i++) {
            float[] position = spawn_position.get(i);
            // pic needs to be changed with enemy pic
            Enemy enemy = new Enemy(world, position[0], position[1]);
            enemies.add(enemy);

        }

    }

    /**
     *  generate 8 initial targets for enemies.
     *
     * @param systems Arraylist stores system objects
     */
    public void initial_sabotageTarget(ArrayList<Systems> systems) {

        ArrayList<Integer> randomIndex = new ArrayList<>();
        // generate random target positions
        for (int i = 0; i < 8; i++) {
            // generate a double  [0,1]
            double randomD = Math.random();
            // generate a index [0,15]
            int index = (int) (randomD * 15);
            // take away healing pod for initial traget, for difficulty
            while (randomIndex.contains(index) 
                    && !systems.get(index).sysName.equals("headlingPod")) {
                randomD = Math.random();
                index = (int) (randomD * 15);
            }
            randomIndex.add(index);
        }

        // set targets
        for (int i = 0; i < randomIndex.size(); i++) {
            int index = randomIndex.get(i);
            Systems sys = systems.get(index);

            float endX = sys.getposition()[0];
            float endY = sys.getposition()[1];

            Enemy enemy = enemies.get(i);
            // set the target
            enemy.set_target_system(sys);
            // set the destination
            enemy.setDest(endX, endY);
            enemy.moveToDest();
            // update the information hash table, aviod enemy targeting the same system
            information.put(sys, enemy);

        }

    }

    /**
     * render the enemy, should be called in gameplay render loop.
     *
     * @param batch SpriteBatch used in game
     */
    public void render_ememy(SpriteBatch batch) {

        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }

    }

    /**
     * update the enemy, should be called in gameplay update.
     *
     * @param delta The time in secconds since the last update
     */
    public void update_enemy(float delta) {

        for (Enemy enemy : enemies) {

            //if (enemy.ability.provked && ! enemy.ability.disabled 
            //&& enemy.ability.target != null && !enemy.is_attcking_mode()) {
            if (enemy.ability.inUse && !enemy.usingAbility) {    
                Player target = enemy.ability.target;
                enemy.ability.useAbility(enemy, target);
                enemy.update(delta);
                enemy.usingAbility = true;
                continue;
            }
            if (enemy.isArrested()) {
                // if enemy have a taget system
                if (enemy.get_target_system() != null) {
                    // remove it from information for other enemies to target that system.
                    if (enemy.get_target_system().is_not_sabotaged() 
                            && information.containsKey(enemy.get_target_system())) {
                        information.remove(enemy.get_target_system());
                        enemy.targetSystem = null;
                        enemy.update(delta);
                        continue;
                    }
                    enemy.update(delta);
                    continue;
                }
            } else {
                // get targeted system object
                Systems sys = enemy.get_target_system();
                // if no system left to sabotage, should start attacking auber
                if (sys == null) {
                    // still have systems not sabotaged, should keep generating next target
                    if (information.size() < 17) {
                        generateNextTarget(enemy);
                        enemy.update(delta);
                        if (enemy.get_target_system() == null) {
                            continue;
                        }
                    }
                    continue;
                }
                if (enemy.is_attcking_mode()) {
                    enemy.sabotage(sys);
                }
                // generate next traget if system sabotaged
                if (sys.is_sabotaged()) {
                    generateNextTarget(enemy);
                }
            }
            enemy.update(delta);
        }
    }

    /**
     * If enemy successfully sabotage one target, generate next target for it.
     *
     * @param enemy enemy object
     */
    public void generateNextTarget(Enemy enemy) {
        for (Systems system : systems) {
            if (!information.containsKey(system)) {
                float endx = system.getposition()[0];
                float endy = system.getposition()[1];
                enemy.setDest(endx, endy);
                enemy.set_target_system(system);
                information.put(system, enemy);
                enemy.moveToDest();
                // set enemy back to standBy mode before it contacts with the next target system,
                // otherwise the system will lose HP before contact
                enemy.set_standByMode();
                return;
            }
        }
        // if there is no systems left for sabotaging,
        // set enemy to standby mode and remove the target system
        enemy.set_standByMode();
        enemy.targetSystem = null;
    }

}
