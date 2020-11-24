package screen.actors;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ArrestedHeader extends Label {

    /**
     * Label to show the number of arrested infiltrators.
     */
    public ArrestedHeader() {

        super("Arrested: 0/8 (Press A to arrest)", new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")), "title");
        setName("Arrested:");
        getStyle().font.getData().setScale(.45f, .45f);
        setColor(Color.WHITE);

    }

    /**
     * update the number of arrested infiltrators.
     *
     * @param auber player
     */
    public void update_Arrested(Player auber) {
        int arrestedCount = auber.arrestedCount;
        setText(getName() + arrestedCount + "/8 (Press A to arrest)");
        if (arrestedCount > 0) {
            setColor(Color.GREEN);
        }
    }


}
