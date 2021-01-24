import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Contains every class to test.
 */
@RunWith(Suite.class)
@SuiteClasses({
    TestArrestedHeader.class,
    TestBackgroundRenderer.class,
    TestCharacterRenderer.class,
    TestGameplay.class,
    TestHealthBar.class,
    TestMainMenu.class,
    TestMenu.class,
    TestSystemStatusMenu.class,
    TestTeleportMenu.class,
    TestWinLoseScreen.class
})

// Run AllTests to test everything within @SuitClasses
public class AllTests {}
