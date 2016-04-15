package dev.blunch.blunch.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;

import static org.junit.Assert.assertNotNull;

/**
 * Get Menu Collaborative Activity Test
 * @author albert
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class GetMenuCollaborativeActivityTest {

    GetMenuCollaborativeActivity activity;

    private TextView    userName,
                        localization,
                        city,
                        hostDishes,
                        suggestions,
                        description,
                        hour;
    private Button join;
    private Toolbar toolbar;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(GetMenuCollaborativeActivity.class);

        userName = (TextView) activity.findViewById(R.id.hostName);
        localization = (TextView) activity.findViewById(R.id.hostLocalization);
        city = (TextView) activity.findViewById(R.id.hostCity);
        hostDishes = (TextView) activity.findViewById(R.id.hostDishes);
        suggestions = (TextView) activity.findViewById(R.id.suggestions);
        description = (TextView) activity.findViewById(R.id.description);
        hour = (TextView) activity.findViewById(R.id.hour);
        join = (Button) activity.findViewById(R.id.join);
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(userName);
        assertNotNull(localization);
        assertNotNull(city);
        assertNotNull(hostDishes);
        assertNotNull(suggestions);
        assertNotNull(description);
        assertNotNull(hour);
        assertNotNull(join);
        assertNotNull(toolbar);
    }

}
