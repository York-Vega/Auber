package com.team3.game;

import com.team3.game.tests.TestArrestedHeader;
import com.team3.game.tests.TestBackgroundRenderer;
import com.team3.game.tests.TestCharacterRenderer;
import com.team3.game.tests.TestGameplay;
import com.team3.game.tests.TestHealthBar;
import com.team3.game.tests.TestMainMenu;
import com.team3.game.tests.TestMenu;
import com.team3.game.tests.TestSystemStatusMenu;
import com.team3.game.tests.TestTeleportMenu;
import com.team3.game.tests.TestWinLoseScreen;
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
