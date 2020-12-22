package screen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class Teleport_Menu extends SelectBox<String> {

    public String[] teleporter;

    /**
     * selectbox teleport menu
     */
    public Teleport_Menu(){
        super(new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
        teleporter = new String[]{"Teleport","controlRoom","mess","infirmary","hangar","reactor","Jail"};
        setItems(teleporter);
        // center the list
        getList().setAlignment(Align.center);
        getStyle().listStyle.font.getData().setScale(.8f,1f);
        setDisabled(true);
    }

}
