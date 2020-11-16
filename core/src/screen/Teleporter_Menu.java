package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Teleporter menu to select which room to teleport
 */
public class Teleporter_Menu {

    public Stage stage;
    public Viewport viewport;
    SelectBox<String> teleporters;

    /**
     * Create a new instantiated teleporter menu

     * @param spriteBatch the GamePlay batch
     */
    public Teleporter_Menu(final SpriteBatch spriteBatch){

        // create a new viewport and a fixed camera for the stage
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),new OrthographicCamera());
        // pass in the game spritebatch
        stage = new Stage(viewport,spriteBatch);
        // create a new table to pad the selectbox to the top of the game screen
        Table table = new Table();
        table.top();
        // set the table same as the size of the stage
        table.setFillParent(true);
        // load the skin with gdx internal file handling
        Skin myskin = new Skin(Gdx.files.internal("teleport_menu_skin/skin/comic-ui.json"));
        // build the teleporter List
        String[] teleporter = new String[]{"Teleport","controlRoom","mess","infirmary","hangar","reactor"};
        // build the teleporters SelectBox
        teleporters = new SelectBox<String>(myskin);
        // pass in the teleporter List
        teleporters.setItems(teleporter);
        //center the text in the List
        teleporters.getList().setAlignment(Align.center);
        // rezie the font of the selectbox list
        teleporters.getStyle().listStyle.font.getData().setScale(0.8f,1f);
        // !! to be deleted when deploy, for testing purpose !!
        addlistener(teleporters,spriteBatch);
        // set the selected box to disable initially
        teleporters.setDisabled(true);
        // add SelectBox to the table and set the width 0.3 of the table size
        table.add(teleporters).width(Value.percentWidth(.2f,table));
        // add table to the stage
        stage.addActor(table);

    }
    // !! test purpose, to be deleted !!
    public void addlistener(final SelectBox<String> teleporters, final SpriteBatch spriteBatch){
        teleporters.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(teleporters.getSelected());
            }
        });

    }

}
