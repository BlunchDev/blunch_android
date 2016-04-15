package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PaymentMenuTest {

    private static final String ID = "1234";
    private static final String NAME = "Wakiki Lunch";
    private static final String AUTHOR = "Pepe Botella";
    private static final String DESCRIPTION = "Salsa, tequila, corason";
    private static final String LOCALIZATION = "C/Aribau 34, Barcelona";
    private static final Set<String> DISHES_SET = new LinkedHashSet<>();
    private static final List<String> DISHES_LIST = new ArrayList<>();
    private static Date DATE_START, DATE_END;


    PaymentMenu paymentMenu;

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
        paymentMenu.setId(ID);
    }

    @Test
    public void create_correctly_an_empty_collaborative_menu() {
        PaymentMenu newCollaborativeMenu = new PaymentMenu();
        assertNull(newCollaborativeMenu.getId());
        assertNull(newCollaborativeMenu.getName());
        assertNull(newCollaborativeMenu.getAuthor());
        assertNull(newCollaborativeMenu.getDateStart());
        assertNull(newCollaborativeMenu.getDateEnd());
        assertNull(newCollaborativeMenu.getDescription());
        assertNull(newCollaborativeMenu.getLocalization());
        assertTrue(newCollaborativeMenu.getDishes().size() == 0);
    }

    @Test
    public void create_correctly_a_payment_menu() throws Exception {
        assertEquals(paymentMenu.getId(), ID);
        assertEquals(paymentMenu.getName(), NAME);
        assertEquals(paymentMenu.getAuthor(), AUTHOR);
        assertEquals(paymentMenu.getDescription(), DESCRIPTION);
        assertEquals(paymentMenu.getLocalization(), LOCALIZATION);
        assertEquals(paymentMenu.getDateStart(), DATE_START);
        assertEquals(paymentMenu.getDateEnd(), DATE_END);
    }

    @Test
    public void update_correctly() throws Exception {
        String newID = "5678";
        paymentMenu.setId(newID);
        String newNAME = "Pa amb tomaquet";
        paymentMenu.setName(newNAME);
        String newAUTHOR = "Mariano Rajoy";
        paymentMenu.setAuthor(newAUTHOR);
        String newDESCRIPTION = "Molt bo";
        paymentMenu.setDescription(newDESCRIPTION);
        String newLOCALIZATION = "Puerta del Sol, Madrid";
        paymentMenu.setLocalization(newLOCALIZATION);

        Calendar newStartCalendar = Calendar.getInstance();
        newStartCalendar.set(Calendar.HOUR_OF_DAY, 15);
        newStartCalendar.set(Calendar.MINUTE, 0);
        Date newDATE_START = newStartCalendar.getTime();
        paymentMenu.setDateStart(newDATE_START);
        Calendar newEndCalendar = Calendar.getInstance();
        newEndCalendar.set(Calendar.HOUR_OF_DAY, 17);
        newEndCalendar.set(Calendar.MINUTE, 30);
        Date newDATE_END = newEndCalendar.getTime();
        paymentMenu.setDateEnd(newDATE_END);

        assertEquals(paymentMenu.getId(), newID);
        assertEquals(paymentMenu.getName(), newNAME);
        assertEquals(paymentMenu.getAuthor(), newAUTHOR);
        assertEquals(paymentMenu.getDescription(), newDESCRIPTION);
        assertEquals(paymentMenu.getLocalization(), newLOCALIZATION);
        assertEquals(paymentMenu.getDateStart(), newDATE_START);
        assertEquals(paymentMenu.getDateEnd(), newDATE_END);
    }

    @Test
    public void check_dishes() throws Exception {
        String DISH_KEY_1 = "1234";
        String DISH_KEY_2 = "4321";
        String DISH_KEY_3 = "0000";

        DISHES_LIST.add(DISH_KEY_1);
        paymentMenu.setDishes(DISHES_LIST);
        assertTrue(paymentMenu.getDishes().keySet().size() == 1);
        paymentMenu.removeDish(DISH_KEY_1);
        assertTrue(paymentMenu.getDishes().keySet().size() == 0);

        DISHES_SET.add(DISH_KEY_1);
        DISHES_SET.add(DISH_KEY_2);
        paymentMenu.setDishes(DISHES_SET);
        assertTrue(paymentMenu.getDishes().keySet().size() == 2);
        assertTrue(paymentMenu.containsDish(DISH_KEY_1));
        assertTrue(paymentMenu.containsDish(DISH_KEY_2));

        paymentMenu.removeDish(DISH_KEY_2);
        assertFalse(paymentMenu.containsDish(DISH_KEY_2));
        paymentMenu.addDish(DISH_KEY_2);
        assertTrue(paymentMenu.containsDish(DISH_KEY_2));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(DISH_KEY_3, true);
        paymentMenu.setDishes(map);
        assertTrue(paymentMenu.getDishes().containsKey(DISH_KEY_3));
        assertFalse(paymentMenu.getDishes().containsKey(DISH_KEY_1));
        assertFalse(paymentMenu.getDishes().containsKey(DISH_KEY_2));
    }

}
