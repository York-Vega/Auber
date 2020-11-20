package screen.actors;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ArrestedHeader extends Label {

    public ArrestedHeader(){
        super("Arrested: 0/8",new Skin(Gdx.files.internal("skin/hudskin/comic-ui.json")),"title");
        setName("Arrested:");
        getStyle().font.getData().setScale(.45f,.45f);
        setColor(Color.WHITE);

    }

    public void update_Arrested(Player auber){
        int arrested_count = auber.arrestedCount;
        setText(getName() + arrested_count + "/8");
        if (arrested_count > 0){
            setColor(Color.GREEN);
        }
    }


}
