package ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.team3.game.GameMain;
import screen.Gameplay;

import java.util.ArrayList;
import java.util.Random;

/**
 * Manage enemies in the game
 */
public class Enemy_manager {

    public World world;
    public TiledMap map;
    public ArrayList<AICharacter> enemies = new ArrayList<>();
    public ArrayList<float[]> spawn_position = new ArrayList<>();
    public ArrayList<float[]> target_position = new ArrayList<>();

    public Enemy_manager(World world,TiledMap map){
        this.world = world;
        this.map = map;
        generate_spwan_position(map);
        generate_enemy(world);
        sabotage_target(map);
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
     * target different systems for AI to sabotage
     * @param map
     */
    public void sabotage_target(TiledMap map){

        MapLayer systems_layer = map.getLayers().get("systems");
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

        for (MapObject object : systems_layer.getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            target_position.add(new float[]{rect.x,rect.y});
        }

        // set targets
        for (int i = 0; i < 8; i ++){
            float endX = (float) target_position.get(random_index.get(i))[0];
            float endY = (float) target_position.get(random_index.get(i))[1];
            AICharacter enemy = enemies.get(i);
            enemy.goTo(endX,endY);
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
            aiCharacter.update(delta);
        }

    }


}
