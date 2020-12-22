package screen.actors;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HealthBar extends ProgressBar {
    // label bofore the bar
    public Label hpText;

    /**
     * healthbar to show the auber's health.
     */
    public HealthBar() {
        super(0, 100, .5f, false, new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
        setAnimateDuration(1.5f);
        hpText = new Label("HP", new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")));
    }

    /**
     * update the health of the player.

     * @param auber player instance
     */
    public void updateHp(Player auber) {
        setValue(auber.health);
    }
}
