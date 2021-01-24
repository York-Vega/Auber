import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestHealthBar {

  static final String[] assets = { "skin/hudskin/comic-ui.json" };

  @Test
  public void allAssetsAvailable() {
    for (String asset : assets) {
      assertTrue("Error, could not find asset " + asset,
          Gdx.files.internal("assets/" + asset).exists());
    }
  }
}
