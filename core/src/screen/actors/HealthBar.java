package screen.actors;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HealthBar extends ProgressBar {
    // label bofore the bar
    public Label hp_text;

    /**
     * Progressbar healthbar
     */
    public HealthBar(){
        super(0,100,.5f,false,new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
        setAnimateDuration(1.5f);
        hp_text = new Label("HP",new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
    }

    /**
     *
     * @param auber player instance
     */
    public void update_HP(Player auber){
        setValue(auber.health);
    }
}
