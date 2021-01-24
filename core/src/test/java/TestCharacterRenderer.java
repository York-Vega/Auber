import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestCharacterRenderer {

  static final String[] assets = { "sprites/dinoSprites.atlas" };

  @Test
  public void allAssetsAvailable() {
    for (String asset : assets) {
      assertTrue("Error, could not find asset " + asset,
          Gdx.files.internal("assets/" + asset).exists());
    }
  }
}
