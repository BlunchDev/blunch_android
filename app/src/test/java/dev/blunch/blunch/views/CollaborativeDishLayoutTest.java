package dev.blunch.blunch.views;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.view.CollaborativeDishLayout;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Collaborative Dish Layout Test
 * @author albert
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@SuppressWarnings("all")
public class CollaborativeDishLayoutTest {

    CollaborativeDishLayout layout;

    @Before
    public void setUp() {
        layout = new CollaborativeDishLayout(RuntimeEnvironment.application, 1);
    }

    @Test
    public void test() throws Exception {
        assertTrue(layout.getDishName().contains(Integer.toString(1)));
        assertNotNull(layout.getClose());
    }

}
