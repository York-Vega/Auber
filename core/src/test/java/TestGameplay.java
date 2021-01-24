import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import test.java.GdxTestRunner;

import com.badlogic.gdx.Gdx;

@RunWith(GdxTestRunner.class)
public class TestGameplay {

    static final String[] assets = { "Map/Map.tmx" };

	@Test
	public void allAssetsAvailable() {
        for (String asset : assets) {
          assertTrue("Error, could not find asset " + asset,
                Gdx.files.internal("assets/" + asset).exists());
        }
      }
}
