package ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import sprites.Systems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Manage enemies in the game
 */
public class Enemy_manager {

    public World world;
    public TiledMap map;
    public static ArrayList<AICharacter> enemies = new ArrayList<>();
    public static ArrayList<float[]> spawn_position = new ArrayList<>();
    public static ArrayList<float[]> target_position = new ArrayList<>();
    public static ArrayList<Systems> systems = new ArrayList<>();
    public static HashMap<Systems,AICharacter> information;

    public Enemy_manager(World world,TiledMap map,ArrayList<Systems> systems){
        this.world = world;
        this.map = map;
        this.systems = systems;
        this.information = new HashMap<>();
        generate_spwan_position(map);
        generate_enemy(world);
        sabotage_target(systems);
    }

    /**
     * generate start positions of AI
     * @param map
     */
    public void generate_spwan_position(TiledMap map){
        // TEST, An enemy spawn layer needed, use hardcoded position to generate 8 enemies for now
        spawn_position.add(new float[]{254,4078});
        spawn_position.add(new float[]{222,4078});
        spawn_position.add(new float[]{190,4078});
        spawn_position.add(new float[]{158,4078});
        spawn_position.add(new float[]{254,4046});
        spawn_position.add(new float[]{222,4046});
        spawn_position.add(new float[]{190,4046});
        spawn_position.add(new float[]{158,4046});
    }

    /**
     * create AI instance and store in Arraylist enemy
     * @param world
     */
    public void generate_enemy(World world){

        for(int i = 0; i < 8; i ++){
            float[] position = spawn_position.get(i);
            // pic needs to be changed with enemy pic
            AICharacter enemy = new AICharacter(world,"player.png",position[0],position[1]);
            enemies.add(enemy);
        }

    }

    /**
     * target different systems for AI to sabotage, this should generate initial 8 targets for NPC
     * @param map
     */
    public void sabotage_target( ArrayList<Systems> systems){

        ArrayList<Integer> random_index = new ArrayList<>();
        // generate random target positions
        for (int i = 0; i < 8; i++){
            // generate a double  [0,1]
            double random_d = Math.random();
            // generate a index [0,15]
            int index =(int) (random_d * 15);
            while (random_index.contains(index)){
                random_d = Math.random();
                index = (int) (random_d * 15);
            }
            random_index.add(index);
        }

        // set targets
        for (int i = 0; i < 8; i ++){
            int index = random_index.get(i);
            Systems sys = systems.get(index);

            float end_X = sys.getposition()[0];
            float end_Y = sys.getposition()[1];

            AICharacter enemy = enemies.get(i);
            // set the fixture userdata of enemy to system object
            enemy.b2body.getFixtureList().get(0).setUserData(sys);
            enemy.goTo(end_X,end_Y);
            // update the information hash map, aviod enemy targeting the same system
            information.put(sys,enemy);
            //System.out.println(enemy.b2body.getUserData() + "will be attacking " + sys.sys_name);

        }

    }

    /**
     * render the enemy, should be called in gameplay render loop
     * @param game
     */
    public void render_ememy(SpriteBatch batch){

        for(AICharacter aiCharacter: enemies){
            aiCharacter.draw(batch);
        }

    }

    /**
     * update the enemy, should be called in gameplay update
     * @param delta
     */
    public void update_ai(float delta){

        for(AICharacter aiCharacter: enemies){
            // get targeted system object
            Systems sys = (Systems) aiCharacter.b2body.getFixtureList().get(0).getUserData();
            if (validate_sabotage(aiCharacter)){
                aiCharacter.sabotage(sys);
            }
            // generate next traget if system sabotaged
            if (sys.getSabotage_status().equals("system_sabotaged")){
                generateNextTarget(aiCharacter);
            }
            aiCharacter.update(delta);
        }

        for (int i = 0; i < systems.size(); i ++){
            System.out.println(systems.get(i).sys_name + " : " + systems.get(i).getSabotage_status() + " with HP: " + systems.get(i).hp);
        }
    }

    /**
     *
     * @param enemy
     * @return validate NPC in attack mode or not
     */
    public boolean validate_sabotage(AICharacter enemy){
        String attack = ".*attack.*";
        String mode = (String) enemy.b2body.getUserData();
        boolean isAttack = Pattern.matches(attack,mode);
        return isAttack;
    }

    /**
     * If NPC successfuly sabotage one target, generate next target for it
     * @param aiCharacter
     */
    public void generateNextTarget(AICharacter enemy){
        for (Systems system: this.systems ){
            if (!information.containsKey(system)){
                enemy.b2body.getFixtureList().get(0).setUserData(system);
                information.put(system,enemy);
                float end_X = system.getposition()[0];
                float end_Y = system.getposition()[1];
                enemy.goTo(end_X,end_Y);
                return;
            }
        }

    }


}
