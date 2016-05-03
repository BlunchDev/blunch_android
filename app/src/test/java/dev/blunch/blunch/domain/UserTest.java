package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertNull(user.getEmail());
        assertNull(user.getImageFile());
        assertNull(user.getMyMenus());
        assertNull(user.getParticipatedMenus());
    }

    @Test
    public void create_correctly_a_payment_menu() throws Exception {
        assertEquals(user.getId(), USER_ID);
        assertEquals(user.getName(), USER_NAME);
        assertEquals(user.getEmail(), USER_EMAIL);
        assertEquals(user.getImageFile(), USER_IMAGE);
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
        String newEMAIL = "mariano@rajoy.pp";
        user.setEmail(newEMAIL);
        String newIMAGE = "akslfj34ui23smfsisd";
        user.setImageFile(newIMAGE);

        assertEquals(user.getId(), newID);
        assertEquals(user.getName(), newNAME);
        assertEquals(user.getEmail(), newEMAIL);
        assertEquals(user.getImageFile(), newIMAGE);
    }

}
