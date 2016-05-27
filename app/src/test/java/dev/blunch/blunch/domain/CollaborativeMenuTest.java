package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CollaborativeMenuTest {

    private final String ID = "1234";
    private final String NAME = "Wakiki Lunch";
    private final String AUTHOR = "Pepe Botella";
    private final String DESCRIPTION = "Salsa, tequila, corason";
    private final String LOCALIZATION = "C/Aribau 34, Barcelona";
    private final List<Dish> SUGGESTED_DISHES = new ArrayList<>();
    private final List<Dish> OFFERED_DISHES = new ArrayList<>();
    private final List<String> SUGGESTED_DISHES_KEYS = new ArrayList<>();
    private final List<String> OFFERED_DISHES_KEYS = new ArrayList<>();
    private Date DATE_START, DATE_END;

    private CollaborativeMenu collaborativeMenu;


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
        collaborativeMenu.setDietTagsString("VEGETARIAN");
    }

    @Test
    public void create_correctly_without_dishes() throws Exception {
        CollaborativeMenu collaborativeMenu = new CollaborativeMenu(NAME, AUTHOR, DESCRIPTION,
                                                                    LOCALIZATION, DATE_START, DATE_END);
        collaborativeMenu.setDietTagsString("VEGETARIAN");
        assertEquals(collaborativeMenu.getName(), NAME);
        assertEquals(collaborativeMenu.getAuthor(), AUTHOR);
        assertEquals(collaborativeMenu.getDescription(), DESCRIPTION);
        assertEquals(collaborativeMenu.getLocalization(), LOCALIZATION);
        assertEquals(collaborativeMenu.getDateStart(), DATE_START);
        assertEquals(collaborativeMenu.getDateEnd(), DATE_END);
        assertEquals(collaborativeMenu.getSuggestedDishes().size(), 0);
        assertEquals(collaborativeMenu.getOfferedDishes().size(), 0);
        assertEquals(collaborativeMenu.getDietTagsString(), "VEGETARIAN");
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
        List<DietTags> dietTags = new ArrayList<>();
        dietTags.add(DietTags.GLUTEN_FREE);
        collaborativeMenu.addDietTags(dietTags);
        assertEquals(collaborativeMenu.getId(), ID);
        assertEquals(collaborativeMenu.getName(), NAME);
        assertEquals(collaborativeMenu.getAuthor(), AUTHOR);
        assertEquals(collaborativeMenu.getDescription(), DESCRIPTION);
        assertEquals(collaborativeMenu.getLocalization(), LOCALIZATION);
        assertEquals(collaborativeMenu.getDateStart(), DATE_START);
        assertEquals(collaborativeMenu.getDateEnd(), DATE_END);
        collaborativeMenu.getDietTags();
        assertEquals(collaborativeMenu.getDietTagsString(), "VEGETARIAN&GLUTEN_FREE");
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

        SUGGESTED_DISHES_KEYS.add(SUGGESTED_DISH_KEY_1);
        collaborativeMenu.setSuggestedDishesKeys(SUGGESTED_DISHES_KEYS);
        assertEquals(1, collaborativeMenu.getSuggestedDishes().keySet().size());
        collaborativeMenu.removeSuggestedDish(SUGGESTED_DISH_KEY_1);
        assertEquals(0, collaborativeMenu.getSuggestedDishes().keySet().size());

        Dish dish1 = new Dish(SUGGESTED_DISH_KEY_1);
        dish1.setId(SUGGESTED_DISH_KEY_1);
        Dish dish2 = new Dish(SUGGESTED_DISH_KEY_2);
        dish2.setId(SUGGESTED_DISH_KEY_2);
        SUGGESTED_DISHES.add(dish1);
        SUGGESTED_DISHES.add(dish2);
        collaborativeMenu.setSuggestedDishes(SUGGESTED_DISHES);
        assertEquals(2, collaborativeMenu.getSuggestedDishes().keySet().size());
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

        OFFERED_DISHES_KEYS.add(OFFERED_DISH_KEY_1);
        collaborativeMenu.setOfferedDishesListKeys(OFFERED_DISHES_KEYS);
        assertEquals(1, collaborativeMenu.getOfferedDishes().keySet().size());
        collaborativeMenu.removeOfferedDish(OFFERED_DISH_KEY_1);
        assertEquals(0, collaborativeMenu.getOfferedDishes().keySet().size());

        Dish dish1 = new Dish(OFFERED_DISH_KEY_1);
        Dish dish2 = new Dish(OFFERED_DISH_KEY_2);
        dish1.setId(OFFERED_DISH_KEY_1);
        dish2.setId(OFFERED_DISH_KEY_2);
        OFFERED_DISHES.add(dish1);
        OFFERED_DISHES.add(dish2);
        assertEquals(2,OFFERED_DISHES.size());
        collaborativeMenu.setOfferedDishes(OFFERED_DISHES);
        assertEquals(collaborativeMenu.getOfferedDishes().keySet().size(), 2);
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
