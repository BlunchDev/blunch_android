package dev.blunch.blunch.activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

/**
 * Get Collaborative Menu Activity Test
 * @author albert on 12/04/16.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricGradleTestRunner.class)
public class GetMenuCollaborativeActivityTest {

    private GetMenuCollaborativeActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(GetMenuCollaborativeActivity.class);
    }

    @Test
    public void shouldNotBeNull() throws Exception {

    }

}
