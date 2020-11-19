package screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Align;
import sprites.Systems;

import java.util.ArrayList;
import java.util.HashMap;

public class System_status_menu extends VerticalGroup {

    public Skin myskin;

    public Label sabotage_count;

    public int count = 0;

    public HashMap<Systems,Label> statusMap = new HashMap<>();

    public System_status_menu(){
        super();
        myskin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
        setFillParent(true);

        // sabotage count label to display number of sabotaged systems
        sabotage_count = new Label("SABOTAGED:0/15",myskin,"title");
        sabotage_count.setName("SABOTAGED:");
        sabotage_count.getStyle().font.getData().setScale(.45f,.45f);

        addActor(sabotage_count);

        align(Align.topLeft);
        columnAlign(Align.left);
        padLeft(20);
        padTop(20);

    }

    /**
     * Generate system labels
     * @param systems Arraylist of System objects
     */
    public void generate_systemLabels(ArrayList<Systems> systems){

        for (Systems system: systems){
            Label sys = new Label(system.getSystemName(),myskin,"alt");
            // setname to store name if ememy stop sabotaing, change the label text back to normal
            sys.setName(system.getSystemName());
            // scale the font size
            sys.getStyle().font.getData().setScale(.7f,.7f);
            addActor(sys);
            // build hashmap for system and menu
            statusMap.put(system,sys);
        }

    }

    /**
     * update the menu
     * @param systems Arraylist of System objects
     */
    public void update_status(ArrayList<Systems> systems){

        for (Systems system: systems){

            Label sys_label = statusMap.get(system);
            // if enemy is sabotaging system, label should warn player
            if(system.getSabotage_status().equals("sabotaging") && !sys_label.getColor().equals(Color.RED)){
                // add "under_attack" to label
                sys_label.setText(sys_label.getText() + ": Under Attack");
                // change the color of label to red
                sys_label.setColor(Color.RED);

            }
            // if system is sabotaged, label should go gray
            else if(system.getSabotage_status().equals("sabotaged") && !sys_label.getColor().equals(Color.GRAY)){
                // add "sabotaged" to the label
                sys_label.setText(sys_label.getText() + ": Sabotaged");
                // change the color of label to grey
                sys_label.setColor(Color.GRAY);
                // update the count
                count += 1;
                sabotage_count.setText(sabotage_count.getName() + count + "/15");
                if(count >= 10){
                    // if system sabotaged over 10, change color of title to red
                    sabotage_count.setColor(Color.RED);
                }

            }
            // if system not being sabotaging or enemy stop sabotaging label should back to normal
            else if(system.getSabotage_status().equals("not sabotaged")){
                // set label back to normal
                sys_label.setText(sys_label.getName());
                // change the color back to white
                sys_label.setColor(Color.WHITE);
            }

        }


    }







}
