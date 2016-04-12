package dev.blunch.blunch.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * New Collaborative Menu Activity Test
 * @author albert on 7/04/16.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricGradleTestRunner.class)
public class NewCollaborativeMenuActivityTest {

    private NewCollaborativeMenuActivity activity;

    private EditText        menuText;
    private EditText        address;
    private EditText        city;
    private EditText        description;
    private EditText        dish;
    private Switch          who;
    private Button          publish;
    private ImageButton     dateButton;
    private ImageButton     moreDishes;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(NewCollaborativeMenuActivity.class);

        menuText = (EditText) activity.findViewById(R.id.nomMenu);
        address = (EditText) activity.findViewById(R.id.adress);
        city = (EditText) activity.findViewById(R.id.city);
        description = (EditText) activity.findViewById(R.id.description);
        dish = (EditText) activity.findViewById(R.id.dish1);
        who = (Switch) activity.findViewById(R.id.switch1);
        publish = (Button) activity.findViewById(R.id.publish);
        dateButton = (ImageButton) activity.findViewById(R.id.timetablebutton);
        moreDishes = (ImageButton) activity.findViewById(R.id.moreDishes);

    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(menuText);
        assertNotNull(address);
        assertNotNull(city);
        assertNotNull(description);
        assertNotNull(dish);
        assertNotNull(who);
        assertNotNull(publish);
        assertNotNull(dateButton);
        assertNotNull(moreDishes);
    }

    @Test
    public void complete_form() throws Exception {
        final String menuName = "Nice lunch";
        final String description = "Go home, you're drunk";
        final String dish = "Fish";
        final String address = "C/New York";
        final String city = "Barcelona";

        this.menuText.setText(menuName);
        this.description.setText(description);
        this.dish.setText(dish);
        this.address.setText(address);
        this.city.setText(city);
        this.who.setChecked(true);

        assertEquals(this.menuText.getText().toString(), menuName);
        assertEquals(this.description.getText().toString(), description);
        assertEquals(this.dish.getText().toString(), dish);
        assertEquals(this.address.getText().toString(), address);
        assertEquals(this.city.getText().toString(), city);
        assertTrue(this.who.isChecked());

        this.who.setChecked(false);
        assertFalse(this.who.isChecked());

    }

    @Test
    public void more_dishes_button_works_correctly() throws Exception {
        final String DISH = "Plato ";
        Integer COUNT = 1;
        ImageButton closeDish;

        this.moreDishes.performClick();
        ++COUNT;
        assertEquals(activity.myDishes.size(), 1);
        assertEquals(activity.myDishes.get(COUNT - 2).getDishName(), DISH + COUNT);

        assertFalse(activity.myDishes.get(COUNT - 2).isSuggest());
        activity.myDishes.get(COUNT - 2).getSuggerenciaSwitch().setChecked(true);
        assertTrue(activity.myDishes.get(COUNT - 2).isSuggest());

        this.moreDishes.performClick();
        ++COUNT;
        assertEquals(activity.myDishes.size(), 2);
        assertEquals(activity.myDishes.get(COUNT - 2).getDishName(), DISH + COUNT);

        closeDish = (ImageButton) activity.findViewById(activity.myDishes.get(activity.myDishes.size() - 1).getClose().getId());
        assertNotNull(closeDish);
        closeDish.performClick();
        assertEquals(activity.myDishes.size(), 1);

        closeDish = (ImageButton) activity.findViewById(activity.myDishes.get(activity.myDishes.size() - 1).getClose().getId());
        assertNotNull(closeDish);
        closeDish.performClick();
        assertEquals(activity.myDishes.size(), 0);
    }


}
