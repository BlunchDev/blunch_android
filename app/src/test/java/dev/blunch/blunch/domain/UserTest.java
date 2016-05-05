package dev.blunch.blunch.domain;

import android.media.Image;
import android.os.Environment;
import android.widget.ImageView;

import com.google.android.apps.common.testing.accessibility.framework.proto.FrameworkProtos;

import org.apache.maven.model.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.R;
import dev.blunch.blunch.utils.dummy.EmptyActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by jmotger on 3/05/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserTest {

    private static final String PAYMENT_ID = "1234";
    private static final String COLLABORATIVE_ID = "5678";
    private static final String NAME = "Wakiki Lunch";
    private static final String AUTHOR = "Pepe Botella";
    private static final String DESCRIPTION = "Salsa, tequila, corason";
    private static final String LOCALIZATION = "C/Aribau 34, Barcelona";
    private static final Set<String> DISHES_SET = new LinkedHashSet<>();
    private static final List<Dish> DISHES_LIST = new ArrayList<>();
    private static Date DATE_START, DATE_END;

    private static final String USER_ID = "9012";
    private static final String USER_NAME = "Paco Paquito";
    private static final String USER_EMAIL = "paco@paquito.com";
    private static final String USER_IMAGE = "7VVYb7S7NBAbJHJSA77ansja";
    private static final int USER_VALORATION_NUMBER = 3;
    private static final Double USER_VALORATION_AVERAGE = 4.0;

    PaymentMenu paymentMenu;
    CollaborativeMenu collaborativeMenu;

    User user;

    @Before
    public void before() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, 12);
        startCalendar.set(Calendar.MINUTE, 30);
        DATE_START = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, 14);
        endCalendar.set(Calendar.MINUTE, 30);
        DATE_END = endCalendar.getTime();

        paymentMenu = new PaymentMenu(  NAME, AUTHOR, DESCRIPTION, LOCALIZATION,
                DATE_START, DATE_END, DISHES_SET);
        paymentMenu.setId(PAYMENT_ID);

        collaborativeMenu = new CollaborativeMenu( NAME, AUTHOR, DESCRIPTION, LOCALIZATION,
                DATE_START, DATE_END, DISHES_LIST, DISHES_LIST);
        collaborativeMenu.setId(COLLABORATIVE_ID);

        user = new User(USER_NAME, USER_EMAIL, USER_IMAGE);
        user.setId(USER_ID);
        user.addNewMyMenu(paymentMenu);
        user.addNewParticipatedMenu(collaborativeMenu);

    }

    @Test
    public void create_correctly_an_empty_user() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getImageFile());
        assertEquals(user.getMyMenus().size(), 0);
        assertEquals(user.getParticipatedMenus().size(), 0);
    }

    @Test
    public void create_correctly_a_payment_menu() throws Exception {
        assertEquals(user.getId(), USER_ID);
        assertEquals(user.getName(), USER_NAME);
        assertEquals(user.getImageFile(), USER_IMAGE);
        assertEquals(user.getValorationAverage(), 0.0, 0.1);
        assertEquals(user.getValorationNumber(), 0);
        for (String s : user.getMyMenus().keySet()) {
            assertEquals(s, PAYMENT_ID);
        }
        for (String s : user.getParticipatedMenus().keySet()) {
            assertEquals(s, COLLABORATIVE_ID);
        }
    }

    @Test
    public void update_correctly() throws Exception {
        String newID = "abcdef";
        user.setId(newID);
        String newNAME = "Mariano Rajoy";
        user.setName(newNAME);
        String newIMAGE = "akslfj34ui23smfsisd";
        user.setImageFile(newIMAGE);
        user.setValorationAverage(USER_VALORATION_AVERAGE);
        user.setValorationNumber(USER_VALORATION_NUMBER);

        assertEquals(user.getId(), newID);
        assertEquals(user.getName(), newNAME);
        assertEquals(user.getImageFile(), newIMAGE);
        assertEquals(user.getValorationAverage(), USER_VALORATION_AVERAGE, 0.1);
        assertEquals(user.getValorationNumber(), USER_VALORATION_NUMBER);
    }

    @Test
    public void menusTest() throws Exception {
        List<String> myMenuKeysList = new ArrayList<>();
        myMenuKeysList.add("1234");
        myMenuKeysList.add("5678");
        List<String> participatedMenuKeysList = new ArrayList<>();
        participatedMenuKeysList.add("9012");
        user.setMyMenus(myMenuKeysList);
        user.setParticipatedMenus(participatedMenuKeysList);
        assertEquals(user.getParticipatedMenus().size(), 1);
        assertEquals(user.getMyMenus().size(), 2);
    }

    @Test
    public void roundImageTest() throws Exception {
        assertNotNull(user.getImageRounded(RuntimeEnvironment.application.getResources()));
    }

}
