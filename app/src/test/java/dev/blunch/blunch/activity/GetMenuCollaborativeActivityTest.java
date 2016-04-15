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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void textViewCorrect() throws Exception {
        final String ID = "1234";
        final String AUTHOR = "Manolo Lama";
        final String NAME = "El plato del bicho";
        final String DESCRIPTION = "Ponte como el bicho con este menu";
        final String LOCALIZATION = "Santiago Bernabeu, Madrid";
        final Date DATE_START = createHourOfToday(20, 45);
        final Date DATE_END = createHourOfToday(22, 30);
        final String SUGGEST_DISH_KEY_1 = "0001";
        final String SUGGEST_DISH_KEY_2 = "0002";
        final String OFFERED_DISH_KEY_1 = "0003";
        final String SUGGEST_DISH_NAME_1 = "Jamon del bicho";
        final String SUGGEST_DISH_NAME_2 = "Sudor del bicho";
        final String OFFERED_DISH_NAME_1 = "CR7 en persona";

        CollaborativeMenu collaborativeMenu = new CollaborativeMenu();
        List<Dish> suggestedDishes = new ArrayList<>();
        List<Dish> offeredDishes = new ArrayList<>();
        collaborativeMenu.setId(ID);
        collaborativeMenu.setAuthor(AUTHOR);
        collaborativeMenu.setName(NAME);
        collaborativeMenu.setDescription(DESCRIPTION);
        collaborativeMenu.setLocalization(LOCALIZATION);
        collaborativeMenu.setDateStart(DATE_START);
        collaborativeMenu.setDateEnd(DATE_END);
        collaborativeMenu.addSuggestedDish(SUGGEST_DISH_KEY_1);
        suggestedDishes.add(new Dish(SUGGEST_DISH_NAME_1));
        collaborativeMenu.addSuggestedDish(SUGGEST_DISH_KEY_2);
        suggestedDishes.add(new Dish(SUGGEST_DISH_NAME_2));
        collaborativeMenu.addOfferedDish(OFFERED_DISH_KEY_1);
        offeredDishes.add(new Dish(OFFERED_DISH_NAME_1));
        activity.setPredefinedMenu(collaborativeMenu, suggestedDishes, offeredDishes);

        assertEquals(userName.getText(), AUTHOR);
        assertTrue(LOCALIZATION.contains(localization.getText()));
        assertTrue(LOCALIZATION.contains(city.getText()));
        assertTrue(hostDishes.getText().toString().contains(OFFERED_DISH_NAME_1));
        assertTrue(suggestions.getText().toString().contains(SUGGEST_DISH_NAME_1));
        assertTrue(suggestions.getText().toString().contains(SUGGEST_DISH_NAME_2));
        assertEquals(description.getText(), DESCRIPTION);
        assertTrue(hour.getText().toString().contains(hourToString(DATE_START)));
        assertTrue(hour.getText().toString().contains(hourToString(DATE_END)));
        assertEquals(toolbar.getTitle(), NAME);
    }

    private Date createHourOfToday(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private String hourToString(Date date) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour + ":";
        if (minute < 10) result += "0";
        result += minute;
        return result;
    }

}
