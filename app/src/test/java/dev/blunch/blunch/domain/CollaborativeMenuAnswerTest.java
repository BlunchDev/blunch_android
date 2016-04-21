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
 * Created by jmotger on 12/04/16.
 */
public class CollaborativeMenuAnswerTest {

    private static final String ID = "5678";
    private static final String MENU_ID = "1234";
    private static final String GUEST = "Donald Trump";
    private static final List<Dish> OFFERED_DISHES = new ArrayList<>();
    private static Date DATE;

    private static CollaborativeMenuAnswer collaborativeMenuAnswer;

    @Before
    public void before() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);
        DATE = calendar.getTime();

        collaborativeMenuAnswer = new CollaborativeMenuAnswer( GUEST, MENU_ID, DATE, OFFERED_DISHES);
        collaborativeMenuAnswer.setId(ID);
        
    }

    @Test
    public void create_correctly_an_empty_collaborative_menu_answer() throws Exception{
        CollaborativeMenuAnswer newCollaborativeMenuAnswer = new CollaborativeMenuAnswer();
        assertNull(newCollaborativeMenuAnswer.getId());
        assertNull(newCollaborativeMenuAnswer.getMenuId());
        assertNull(newCollaborativeMenuAnswer.getGuest());
        assertNull(newCollaborativeMenuAnswer.getDate());
        assertTrue(newCollaborativeMenuAnswer.getOfferedDishes().size() == 0);
    }

    @Test
    public void create_correctly_a_collaborative_menu_answer() throws Exception {
        assertEquals(collaborativeMenuAnswer.getId(), ID);
        assertEquals(collaborativeMenuAnswer.getMenuId(), MENU_ID);
        assertEquals(collaborativeMenuAnswer.getGuest(), GUEST);
        assertEquals(collaborativeMenuAnswer.getDate(), DATE);
    }

    @Test
    public void update_correctly() throws Exception {
        String newID = "9012";
        collaborativeMenuAnswer.setId(newID);
        String newMENU_ID = "3456";
        collaborativeMenuAnswer.setMenuId(newMENU_ID);
        String newGUEST = "Mariano Rajoy";
        collaborativeMenuAnswer.setGuest(newGUEST);

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.HOUR_OF_DAY, 15);
        newCalendar.set(Calendar.MINUTE, 0);
        Date newDATE = newCalendar.getTime();
        collaborativeMenuAnswer.setDate(newDATE);

        assertEquals(collaborativeMenuAnswer.getId(), newID);
        assertEquals(collaborativeMenuAnswer.getMenuId(), newMENU_ID);
        assertEquals(collaborativeMenuAnswer.getGuest(), newGUEST);
        assertEquals(collaborativeMenuAnswer.getDate(), newDATE);
    }

    @Test
    public void check_offered_dishes() throws Exception {
        String OFFERED_DISH_KEY_1 = "1234";
        String OFFERED_DISH_KEY_2 = "4321";
        String OFFERED_DISH_KEY_3 = "0000";
        Dish d1 = new Dish();
        d1.setId(OFFERED_DISH_KEY_1);
        Dish d2 = new Dish();
        d2.setId(OFFERED_DISH_KEY_2);
        Dish d3 = new Dish();
        d3.setId(OFFERED_DISH_KEY_3);
        OFFERED_DISHES.add(d1);
        OFFERED_DISHES.add(d2);
        collaborativeMenuAnswer.setOfferedDishesList(OFFERED_DISHES);
        assertTrue(collaborativeMenuAnswer.getOfferedDishes().keySet().size() == 2);
        assertTrue(collaborativeMenuAnswer.containsOfferedDish(OFFERED_DISH_KEY_1));
        assertTrue(collaborativeMenuAnswer.containsOfferedDish(OFFERED_DISH_KEY_2));

        collaborativeMenuAnswer.removeOfferedDish(OFFERED_DISH_KEY_2);
        assertFalse(collaborativeMenuAnswer.containsOfferedDish(OFFERED_DISH_KEY_2));
        collaborativeMenuAnswer.addOfferedDish(OFFERED_DISH_KEY_2);
        assertTrue(collaborativeMenuAnswer.containsOfferedDish(OFFERED_DISH_KEY_2));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OFFERED_DISH_KEY_3, true);
        collaborativeMenuAnswer.setOfferedDishes(map);
        assertTrue(collaborativeMenuAnswer.getOfferedDishes().containsKey(OFFERED_DISH_KEY_3));
        assertFalse(collaborativeMenuAnswer.getOfferedDishes().containsKey(OFFERED_DISH_KEY_1));
        assertFalse(collaborativeMenuAnswer.getOfferedDishes().containsKey(OFFERED_DISH_KEY_2));
    }

}
