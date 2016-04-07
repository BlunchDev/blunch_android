package dev.blunch.blunch.activity;

import android.os.Build;
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
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.MockRepository;
import dev.blunch.blunch.utils.Repository;
import static org.junit.Assert.assertNotNull;

/**
 * New Collaborative Menu Activity Test
 * @author albert on 7/04/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
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

    private CollaborativeMenuService service;
    private MockRepository<CollaborativeMenu> repository;
    private Repository.OnChangedListener.EventType lastChangedType;

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

        repository = new MockRepository<>();
        service = new CollaborativeMenuService(repository);
        lastChangedType = null;
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
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
        this.publish.performClick();
    }


}
