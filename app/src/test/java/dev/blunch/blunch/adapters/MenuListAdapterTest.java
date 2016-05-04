package dev.blunch.blunch.adapters;

import android.app.Activity;
import android.os.Build;

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
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.dummy.EmptyActivity;

import static org.junit.Assert.assertEquals;

/**
 * Menu List Adapter Test
 * @author albert
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@SuppressWarnings("all")
public class MenuListAdapterTest {

    private final List<Menu> data = new ArrayList<>();
    private MenuListAdapter menuListAdapter;
    private Activity mockActivity;
    private Menu firstMenu, secondMenu, thirdMenu, fourthMenu;

    @Before
    public void before() {
        mockActivity = Robolectric.setupActivity(EmptyActivity.class);

        firstMenu = new CollaborativeMenu(  "El Gran Menu", "Maria", "Bonissim",
                                            "C/Mallorca, Barcelona", toDate(10, 30), toDate(13, 45));
        firstMenu.setId("1234");
        secondMenu = new CollaborativeMenu( "Vine a menjar", "Pere", "Tots junts podrem",
                                            "C/Numancia, Barcelona", toDate(22, 30), toDate(23, 45));
        secondMenu.setId("5678");
        thirdMenu = new CollaborativeMenu(  "No te'n penediras", "Gerard", "Em recordaras tota la vida",
                                            "C/Sants, Barcelona", toDate(16, 30), toDate(19, 45));
        thirdMenu.setId("9012");
        fourthMenu = new PaymentMenu(       "Molt car", "Quim", "Treu la cartera",
                                            "C/Hospitalet, Barcelona", toDate(15, 30), toDate(20, 45));
        fourthMenu.setId("3456");

        data.add(firstMenu);
        data.add(secondMenu);
        data.add(thirdMenu);
        data.add(fourthMenu);
        menuListAdapter = new MenuListAdapter(mockActivity.getApplicationContext(), data, new ArrayList<User>());
    }

    @Test
    public void getCountTest() throws Exception {
        assertEquals(menuListAdapter.getCount(), 4);
        data.add(new PaymentMenu("T'agradara", "Victor", "Mu barato",
                "C/Colon, Barcelona", toDate(10, 30), toDate(20, 45)));
        assertEquals(menuListAdapter.getCount(), 5);
    }

    @Test
    public void getItemTest() throws Exception {
        assertEquals(menuListAdapter.getItem(0).getName(), firstMenu.getName());
        assertEquals(menuListAdapter.getItem(2).getAuthor(), thirdMenu.getAuthor());
    }

    private Date toDate(Integer hour, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

}
