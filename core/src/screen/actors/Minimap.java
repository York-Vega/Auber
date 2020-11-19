package screen.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Minimap extends ScreenViewport {

    public Minimap(OrthographicCamera camera){
        super(camera);
    }

    public void update (boolean centreCamera) {
        setScreenBounds(getScreenX(), getScreenY(), getScreenWidth(), getScreenHeight());
        setWorldSize(getScreenWidth() * getUnitsPerPixel(), getScreenHeight() * getUnitsPerPixel());
        apply(centreCamera);
    }

}
