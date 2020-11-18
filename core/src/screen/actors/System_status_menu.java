package screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
        sabotage_count = new Label("SABOTAGED:0/17",myskin,"title");
        sabotage_count.setName("SABOTAGED:");
        sabotage_count.getStyle().font.getData().setScale(.45f,.45f);

        addActor(sabotage_count);

        align(Align.topLeft);
        columnAlign(Align.left).space(3);
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
            sys.getStyle().font.getData().setScale(.8f,.8f);
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
            if(system.getSabotage_status().equals("system_sabotaging") && !system.getSabotage_status().equals("system_sabotaged")){
                // already sabotaging
                if (sys_label.getColor().equals(Color.RED)){
                    sys_label.setText(system.sys_name + ": Under Attack" + ": (" + system.hp + "%)");
                }
                // just be sabotaging
                else{
                    sys_label.setText(system.sys_name + ": Under Attack" + ": (" + system.hp + "%)");
                    sys_label.setColor(Color.RED);
                }

            }
            // if system is sabotaged, label should go gray
            else if(system.getSabotage_status().equals("system_sabotaged") ){
                // alreday sabotaged
                if(sys_label.getColor().equals(Color.GRAY)){
                    sys_label.setText(system.sys_name + ": Sabotaged(" + system.hp + "%)");
                }
                // just be sabotaged
                else if(sys_label.getColor().equals(Color.RED)){
                    sys_label.setText(system.sys_name + ": Sabotaged("  + system.hp + "%)");
                    sys_label.setColor(Color.GRAY);
                    // update sabotaged count
                    count += 1;
                    sabotage_count.setText(sabotage_count.getName() + count + "/17");
                    if(count >= 10){
                        // if system sabotaged over 10, change color of title to red
                        sabotage_count.setColor(Color.RED);
                    }
                }
                // bug fix
                else if(sys_label.getColor().equals(Color.WHITE)){
                    sys_label.setText(system.sys_name + ": Sabotaged" + "(" + system.hp + "%)");
                    sys_label.setColor(Color.GRAY);
                }
            }
            // if system not being sabotaging or enemy stop sabotaging label should back to normal
            else if(system.getSabotage_status().equals("system_not_sabotaged")){
                // not being sabotaged
                if (sys_label.getColor().equals(Color.WHITE)){
                    sys_label.setText(system.sys_name + ": (" + system.hp + "%)");
                }
                // sabotaging just stopped
                else if (sys_label.getColor().equals(Color.RED)){
                    sys_label.setText(system.sys_name + ": (" + system.hp + "%)");
                    sys_label.setColor(Color.WHITE);
                }
            }
        }
    }


}
