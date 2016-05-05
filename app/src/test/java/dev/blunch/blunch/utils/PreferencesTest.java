package dev.blunch.blunch.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by albert on 5/05/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class PreferencesTest {

    private static String USER = "pepito@gmail";

    @Test
    public void test() throws Exception {
        Preferences.init(RuntimeEnvironment.application);

        Preferences.setCurrentUserEmail(USER);
        assertEquals(Preferences.getCurrentUserEmail(), USER);
    }

}
