package screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.Hashtable;
import sprites.Systems;


public class SystemStatusMenu extends VerticalGroup {

    public Skin myskin;

    public Label sabotageCount;

    public int count = 0;

    public Hashtable<Systems, Label> statusMap = new Hashtable<>();

    /**
     * Show status of each systems.
     */
    public SystemStatusMenu() {

        super();
        myskin = new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json"));
        setFillParent(true);

        // sabotage count label to display number of sabotaged systems
        sabotageCount = new Label("SABOTAGED:0/17", myskin, "title");
        sabotageCount.setName("SABOTAGED:");
        sabotageCount.getStyle().font.getData().setScale(.45f, .45f);

        addActor(sabotageCount);

        align(Align.topLeft);
        columnAlign(Align.left).space(3);
        padLeft(20);
        padTop(20);


    }

    /**
     * Generate system labels.
     *
     * @param systems Arraylist of System objects
     */
    public void generate_systemLabels(ArrayList<Systems> systems) {

        for (Systems system : systems) {
            Label sys = new Label(system.getSystemName(), myskin, "alt");
            sys.setColor(Color.WHITE);
            // setname to store name if ememy stop sabotaing, change the label text back to normal
            sys.setName(system.getSystemName());
            // scale the font size
            sys.getStyle().font.getData().setScale(.8f, .8f);
            addActor(sys);
            // build hashmap for system and menu
            statusMap.put(system, sys);
        }

    }

    /**
     * update the menu.
     *
     * @param systems Arraylist of System objects
     */
    public void update_status(ArrayList<Systems> systems) {

        for (Systems system : systems) {

            Label sysLabel = statusMap.get(system);

            // if enemy is sabotaging system, label should warn player
            if (system.is_sabotaging()) {

                sysLabel.setColor(Color.RED);
                sysLabel.setText(system.sysName + ": Under Attack" + ": (" + system.hp + "%)");

            }
            // if system is sabotaged, label should go gray
            if (system.is_sabotaged()) {
                // alreday sabotaged
                if (sysLabel.getColor().equals(Color.GRAY)) {

                    sysLabel.setText(system.sysName + ": Sabotaged(" + system.hp + "%)");

                } else {
                    sysLabel.setText(system.sysName + ": Sabotaged("  + system.hp + "%)");
                    sysLabel.setColor(Color.GRAY);
                    // update sabotaged count
                    count += 1;
                    sabotageCount.setText(sabotageCount.getName() + count + "/17");
                    if (count >= 10) {
                        // if system sabotaged over 10, change color of title to red
                        sabotageCount.setColor(Color.RED);
                    }

                }
                // just be sabotaged

            }
            // if system not being sabotaging or enemy stop sabotaging label should back to normal
            if (system.is_not_sabotaged()) {
                sysLabel.setColor(Color.WHITE);
                sysLabel.setText(system.sysName + ": (" + system.hp + "%)");

            }
        }

    }


}
