package dev.blunch.blunch.activity;

import android.os.Build;
import android.view.View;
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

/**
 * Created by jmotger on 6/04/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CollaborativeProposalAnswerActivityTest {

    private MockRepository<CollaborativeMenu> repositoryMenu;
    private MockRepository<CollaborativeMenuAnswer> repositoryMenuAnswer;
    private MockRepository<Dish> repositoryDish;
    private CollaborativeMenuService collaborativeMenuService;

    private String menuId;
    private final String name = "Comida mexicana";
    private final String author = "Donald Trump";
    private final String description = "Ricos tacos y burritos de este hermoso país";
    private final String localization = "local_test";
    private final Date dateStart = Calendar.getInstance().getTime();
    private final Date dateEnd = Calendar.getInstance().getTime();
    private final Date date = Calendar.getInstance().getTime();
    private List<Dish> offeredDishes;
    private List<Dish> suggestedDishes;

    private final String dishName1 = "Burritos";
    private final String dishName2 = "Nachos";

    private final String guest = "Pablo Iglesias";

    private CollaborativeMenuAnswerActivity activity;

    private EditText newSuggestionEditText;
    private Button newSuggestionButton;
    private LinearLayout suggestionsHostLayout;
    private LinearLayout suggestionsGuestLayout;

    @Before
    public void before() {

        repositoryMenu = new MockRepository<>();
        repositoryDish = new MockRepository<>();
        repositoryMenuAnswer = new MockRepository<>();

        collaborativeMenuService = new CollaborativeMenuService(repositoryMenu, repositoryDish, repositoryMenuAnswer);

        final Dish dish1 = new Dish(dishName1);
        final Dish dish2 = new Dish(dishName2);

        offeredDishes = new ArrayList<>();
        suggestedDishes = new ArrayList<>();

        offeredDishes.add(dish1);
        suggestedDishes.add(dish2);

        final CollaborativeMenu collaborativeMenu = collaborativeMenuService.save(new CollaborativeMenu(name, author, description,
                localization, dateStart, dateEnd), offeredDishes, suggestedDishes);

        menuId = collaborativeMenu.getId();
    }

    @After
    public void after() {
    }

    @Test
    public void answer_to_collaborativeMenu_domain() {
        try {
            CollaborativeMenuAnswer collaborativeMenuAnswer = null;
            collaborativeMenuAnswer = collaborativeMenuService.
                    reply(new CollaborativeMenuAnswer(guest, menuId,
                            date, suggestedDishes));
            assertEquals(guest, collaborativeMenuAnswer.getGuest());
            assertEquals(menuId, collaborativeMenuAnswer.getMenuId());
            assertEquals(date, collaborativeMenuAnswer.getDate());
            int i = 0;
            for (String s : collaborativeMenuAnswer.getOfferedDishes().keySet()) {
                assertEquals(suggestedDishes.get(i).getId(), s);
                ++i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void answer_to_collaborativeMenu_activity() throws Exception {
        activity = Robolectric.setupActivity(CollaborativeMenuAnswerActivity.class);

        suggestionsHostLayout = (LinearLayout) activity.findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        newSuggestionEditText = (EditText) activity.findViewById(R.id.CollaborativeMenuAnswerEt);
        newSuggestionButton = (Button) activity.findViewById(R.id.CollaborativeMenuAnswerBtn);
        suggestionsGuestLayout = (LinearLayout) activity.findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);

        activity.fillHostSuggestions(suggestedDishes);

        for (int i = 0; i < suggestionsHostLayout.getChildCount(); ++i) {
            CheckBox checkBox = (CheckBox) suggestionsHostLayout.getChildAt(i);
            assertEquals(checkBox.getText(), suggestedDishes.get(i).getName());
            assertEquals(checkBox.isChecked(), false);
        }

        CheckBox checkBox = (CheckBox) suggestionsHostLayout.getChildAt(0);
        checkBox.performClick();
        assertEquals(checkBox.isChecked(), true);

        ArrayList<String> dishes = new ArrayList<>();
        dishes.add("Quesadillas de jamón");
        dishes.add("Tequila");

        newSuggestionEditText.setText(dishes.get(0));
        newSuggestionButton.performClick();

        newSuggestionEditText.setText(dishes.get(1));
        newSuggestionButton.performClick();

        for (int i = 0; i < suggestionsGuestLayout.getChildCount(); ++i) {
            TextView textView = (TextView) ((LinearLayout) suggestionsGuestLayout.getChildAt(i)).getChildAt(0);
            assertEquals(textView.getText(), dishes.get(i));
        }

        ImageButton removeSuggestion = (ImageButton) ((LinearLayout)suggestionsGuestLayout.getChildAt(1)).getChildAt(1);
        removeSuggestion.performClick();
        dishes.remove(1);

        for (int i = 0; i < suggestionsGuestLayout.getChildCount(); ++i) {
            TextView textView = (TextView) ((LinearLayout) suggestionsGuestLayout.getChildAt(i)).getChildAt(0);
            assertEquals(textView.getText(), dishes.get(i));
        }

    }

    public static void runAndWaitUntil(FirebaseRepository<CollaborativeMenuAnswer> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
        final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(1);
        semaphore.acquire();


        FirebaseRepository.OnChangedListener listener = new FirebaseRepository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                semaphore.release();
            }
        };


        ref.setOnChangedListener(listener);
        task.run();
        boolean isDone = false;
        boolean adquired = false;
        long startedAt = System.currentTimeMillis();
        while (!adquired && System.currentTimeMillis()-startedAt<2000) {
            ShadowApplication.getInstance().getBackgroundThreadScheduler().runOneTask();
            adquired = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            try {
                isDone = done.call();
            } catch (Exception e) {
                e.printStackTrace();
                // and we're not done
            }
        }
        if (!isDone) {
            throw new AssertionFailedError();
        }
        ref.setOnChangedListener(null);
    }

}
