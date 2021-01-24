import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import test.java.GdxTestRunner;

import com.badlogic.gdx.Gdx;

@RunWith(GdxTestRunner.class)
public class TestCharacterRenderer {

    static final String[] assets = { "dinoSprites.atlas" };

	@Test
	public void allAssetsAvailable() {
        for (String asset : assets) {
          assertTrue("Error, could not find asset " + asset,
                Gdx.files.internal("assets/" + asset).exists());
        }
      }
}
