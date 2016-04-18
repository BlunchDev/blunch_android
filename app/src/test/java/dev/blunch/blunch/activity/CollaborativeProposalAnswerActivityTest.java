package dev.blunch.blunch.activity;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.FirebaseRepository;
import dev.blunch.blunch.utils.MockRepository;

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
