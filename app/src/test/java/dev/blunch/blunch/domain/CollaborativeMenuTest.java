package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CollaborativeMenuTest {

    private static final String ID = "1234";
    private static final String NAME = "Wakiki Lunch";
    private static final String AUTHOR = "Pepe Botella";
    private static final String DESCRIPTION = "Salsa, tequila, corason";
    private static final String LOCALIZATION = "C/Aribau 34, Barcelona";
    private static final Set<String> SUGGESTED_DISHES = new LinkedHashSet<>();
    private static final Set<String> OFFERED_DISHES = new LinkedHashSet<>();
    private static Date DATE_START, DATE_END;

    private static CollaborativeMenu collaborativeMenu;


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

        collaborativeMenu = new CollaborativeMenu(  NAME, AUTHOR, DESCRIPTION,
                                                    LOCALIZATION, DATE_START, DATE_END,
                                                    SUGGESTED_DISHES, OFFERED_DISHES);
        collaborativeMenu.setId(ID);
    }

    @Test
    public void create_correctly_an_empty_collaborative_menu() throws Exception{
        CollaborativeMenu newCollaborativeMenu = new CollaborativeMenu();
        assertNull(newCollaborativeMenu.getId());
        assertNull(newCollaborativeMenu.getName());
        assertNull(newCollaborativeMenu.getAuthor());
        assertNull(newCollaborativeMenu.getDateStart());
        assertNull(newCollaborativeMenu.getDateEnd());
        assertNull(newCollaborativeMenu.getDescription());
        assertNull(newCollaborativeMenu.getLocalization());
        assertTrue(newCollaborativeMenu.getOfferedDishes().size() == 0);
        assertTrue(newCollaborativeMenu.getSuggestedDishes().size() == 0);
    }

    @Test
    public void create_correctly_a_collaborative_menu() throws Exception {
        assertEquals(collaborativeMenu.getId(), ID);
        assertEquals(collaborativeMenu.getName(), NAME);
        assertEquals(collaborativeMenu.getAuthor(), AUTHOR);
        assertEquals(collaborativeMenu.getDescription(), DESCRIPTION);
        assertEquals(collaborativeMenu.getLocalization(), LOCALIZATION);
        assertEquals(collaborativeMenu.getDateStart(), DATE_START);
        assertEquals(collaborativeMenu.getDateEnd(), DATE_END);
    }

    @Test
    public void update_correctly() throws Exception {
        String newID = "5678";
        collaborativeMenu.setId(newID);
        String newNAME = "Pa amb tomaquet";
        collaborativeMenu.setName(newNAME);
        String newAUTHOR = "Mariano Rajoy";
        collaborativeMenu.setAuthor(newAUTHOR);
        String newDESCRIPTION = "Molt bo";
        collaborativeMenu.setDescription(newDESCRIPTION);
        String newLOCALIZATION = "Puerta del Sol, Madrid";
        collaborativeMenu.setLocalization(newLOCALIZATION);

        Calendar newStartCalendar = Calendar.getInstance();
        newStartCalendar.set(Calendar.HOUR_OF_DAY, 15);
        newStartCalendar.set(Calendar.MINUTE, 0);
        Date newDATE_START = newStartCalendar.getTime();
        collaborativeMenu.setDateStart(newDATE_START);
        Calendar newEndCalendar = Calendar.getInstance();
        newEndCalendar.set(Calendar.HOUR_OF_DAY, 17);
        newEndCalendar.set(Calendar.MINUTE, 30);
        Date newDATE_END = newEndCalendar.getTime();
        collaborativeMenu.setDateEnd(newDATE_END);

        assertEquals(collaborativeMenu.getId(), newID);
        assertEquals(collaborativeMenu.getName(), newNAME);
        assertEquals(collaborativeMenu.getAuthor(), newAUTHOR);
        assertEquals(collaborativeMenu.getDescription(), newDESCRIPTION);
        assertEquals(collaborativeMenu.getLocalization(), newLOCALIZATION);
        assertEquals(collaborativeMenu.getDateStart(), newDATE_START);
        assertEquals(collaborativeMenu.getDateEnd(), newDATE_END);
    }

    @Test
    public void check_suggested_dishes() throws Exception {
        String SUGGESTED_DISH_KEY_1 = "1234";
        String SUGGESTED_DISH_KEY_2 = "4321";
        String SUGGESTED_DISH_KEY_3 = "0000";
        SUGGESTED_DISHES.add(SUGGESTED_DISH_KEY_1);
        SUGGESTED_DISHES.add(SUGGESTED_DISH_KEY_2);
        collaborativeMenu.setSuggestedDishes(SUGGESTED_DISHES);
        assertTrue(collaborativeMenu.getSuggestedDishes().keySet().size() == 2);
        assertTrue(collaborativeMenu.containsSuggestedDish(SUGGESTED_DISH_KEY_1));
        assertTrue(collaborativeMenu.containsSuggestedDish(SUGGESTED_DISH_KEY_2));

        collaborativeMenu.removeSuggestedDish(SUGGESTED_DISH_KEY_2);
        assertFalse(collaborativeMenu.containsSuggestedDish(SUGGESTED_DISH_KEY_2));
        collaborativeMenu.addSuggestedDish(SUGGESTED_DISH_KEY_2);
        assertTrue(collaborativeMenu.containsSuggestedDish(SUGGESTED_DISH_KEY_2));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(SUGGESTED_DISH_KEY_3, true);
        collaborativeMenu.setSuggestedDishes(map);
        assertTrue(collaborativeMenu.getSuggestedDishes().containsKey(SUGGESTED_DISH_KEY_3));
        assertFalse(collaborativeMenu.getSuggestedDishes().containsKey(SUGGESTED_DISH_KEY_1));
        assertFalse(collaborativeMenu.getSuggestedDishes().containsKey(SUGGESTED_DISH_KEY_2));
    }

    @Test
    public void check_offered_dishes() throws Exception {
        String OFFERED_DISH_KEY_1 = "1234";
        String OFFERED_DISH_KEY_2 = "4321";
        String OFFERED_DISH_KEY_3 = "0000";
        OFFERED_DISHES.add(OFFERED_DISH_KEY_1);
        OFFERED_DISHES.add(OFFERED_DISH_KEY_2);
        collaborativeMenu.setOfferedDishes(OFFERED_DISHES);
        assertTrue(collaborativeMenu.getOfferedDishes().keySet().size() == 2);
        assertTrue(collaborativeMenu.containsOfferedDish(OFFERED_DISH_KEY_1));
        assertTrue(collaborativeMenu.containsOfferedDish(OFFERED_DISH_KEY_2));

        collaborativeMenu.removeOfferedDish(OFFERED_DISH_KEY_2);
        assertFalse(collaborativeMenu.containsOfferedDish(OFFERED_DISH_KEY_2));
        collaborativeMenu.addOfferedDish(OFFERED_DISH_KEY_2);
        assertTrue(collaborativeMenu.containsOfferedDish(OFFERED_DISH_KEY_2));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OFFERED_DISH_KEY_3, true);
        collaborativeMenu.setOfferedDishes(map);
        assertTrue(collaborativeMenu.getOfferedDishes().containsKey(OFFERED_DISH_KEY_3));
        assertFalse(collaborativeMenu.getOfferedDishes().containsKey(OFFERED_DISH_KEY_1));
        assertFalse(collaborativeMenu.getOfferedDishes().containsKey(OFFERED_DISH_KEY_2));
    }
}
