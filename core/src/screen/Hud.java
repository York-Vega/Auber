package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import screen.actors.ArrestedHeader;
import screen.actors.HealthBar;
import screen.actors.System_status_menu;
import screen.actors.Teleport_Menu;


/**
 * Hud to display information
 */
public class Hud {

    public Stage stage;
    public Viewport viewport;

    // actors need to be added to the hud
    public HealthBar healthBar;
    public Teleport_Menu teleport_menu;
    public System_status_menu system_status_menu;
    public ArrestedHeader arrestedHeader;

    /**
     * Create a new instantiated hud.

     * @param spriteBatch the GamePlay batch
     */
    public Hud(final SpriteBatch spriteBatch)  {

        // create a new viewport and a fixed camera for the stage
        viewport = new FitViewport(1280, 720, new OrthographicCamera());
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // pass in the game spritebatch
        stage = new Stage(viewport, spriteBatch);
        // create a new table as a container to lay out the actors
        Table table = new Table();
        // pad the table to the top of screen
        table.top();
        // set the table same as the size of the stage
        table.setFillParent(true);
        // create teleport_menu (SelectBox<String>)actor
        teleport_menu = new Teleport_Menu();
        // create healthbar (ProgressBar)actor
        healthBar = new HealthBar();
        // create a system_status_menu actor
        system_status_menu = new System_status_menu();
        // create a arrested count header actor
        arrestedHeader = new ArrestedHeader();
        // add teleport_menu to the table
        table.add(teleport_menu).padLeft(20).width(Value.percentWidth(.2f, table));
        // add hp_text in front of bar, 20 is the space between hp text and teleport menu
        table.add(healthBar.hp_text).padLeft(20);
        // add healthBar to the table, 5 is the space between hp text and healthbar
        table.add(healthBar).padLeft(5).width(Value.percentWidth(.2f, table));
        // add arrest header to the table
        table.add(arrestedHeader).padLeft(40).width(Value.percentWidth(.2f,table));

        // show the layout, to be deleted when deploy
        //stage.setDebugAll(true);
        // add table to the stage
        stage.addActor(table);
        // add system_status_menu to the stage
        stage.addActor(system_status_menu);

    }

    public void resize(int width, int height)  {
        viewport.update(width, height);
        
    }

}
