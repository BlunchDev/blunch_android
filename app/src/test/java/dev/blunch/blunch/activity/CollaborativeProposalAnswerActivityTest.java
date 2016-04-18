package dev.blunch.blunch.activity;

import android.os.Build;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by jmotger on 6/04/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CollaborativeProposalAnswerActivityTest {

    private CollaborativeMenuAnswerActivity activity;

    private EditText newSuggestionEditText;
    private Button newSuggestionButton;
    private LinearLayout suggestionsHostLayout;
    private LinearLayout suggestionsGuestLayout;

    @Before
    public void before() {
        activity = Robolectric.setupActivity(CollaborativeMenuAnswerActivity.class);

        suggestionsHostLayout = (LinearLayout) activity.findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        newSuggestionEditText = (EditText) activity.findViewById(R.id.CollaborativeMenuAnswerEt);
        newSuggestionButton = (Button) activity.findViewById(R.id.CollaborativeMenuAnswerBtn);
        suggestionsGuestLayout = (LinearLayout) activity.findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);

    }

    @Test
    public void isNotNull() throws Exception {

        assertNotNull(suggestionsHostLayout);
        assertNotNull(newSuggestionEditText);
        assertNotNull(newSuggestionButton);
        assertNotNull(suggestionsGuestLayout);

    }

    @Test
    public void addSuggestions() throws Exception {

        newSuggestionEditText.setText("Patatas a la riojana");
        newSuggestionButton.performClick();

        newSuggestionEditText.setText("Sangría (Don Simón)");
        newSuggestionButton.performClick();

        assertEquals(activity.getGuestNewSuggestions().size(), 2);

        ((ImageButton) ((ViewGroup) suggestionsGuestLayout.getChildAt(0)).getChildAt(1)).performClick();
        assertEquals(activity.getGuestNewSuggestions().size(), 1);
        assertEquals(activity.getGuestNewSuggestions().get(0), "Sangría (Don Simón)");

    }

}
