package dev.blunch.blunch.activity;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

/**
 * Get Menu Collaborative Activity Test
 * @author albert
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class GetMenuCollaborativeActivityTest {

    GetMenuCollaborativeActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(GetMenuCollaborativeActivity.class);


    }

    @Test
    public void test() throws Exception {

    }

}
