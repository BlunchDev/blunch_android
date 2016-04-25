package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by casassg on 22/04/16.
 *
 * @author casassg
 */
public class PaymentMenuAnswerTest {

    private PaymentMenuAnswer answer;
    private String idMenu;
    private List<Dish> dishes = new ArrayList<>();
    private Dish dish1, dish2;
    private String guest;
    private Date date;

    @Before
    public void setUp() throws Exception {
        dish1 = new Dish("Pasta", 3.0);
        dish1.setId("1");
        dish2 = new Dish("Carne", 5.0);
        dish2.setId("2");
        dishes.add(dish1);
        dishes.add(dish2);
        idMenu = "42";
        guest = "Manolo";
        date = Calendar.getInstance().getTime();
        answer = new PaymentMenuAnswer();
        answer = new PaymentMenuAnswer(idMenu, guest, date, dishes);

    }

    @Test
    public void testGetChoosenDishes() throws Exception {
        Map<String, Object> dishs = answer.getChoosenDishes();
        assertEquals(dishs.size(), 2);
    }

    @Test
    public void testSetChoosenDishes() throws Exception {
        String NEW_KEY = "NEW_KEY";
        Map<String, Object> dishes = answer.getChoosenDishes();
        dishes.put(NEW_KEY, true);
        answer.setChoosenDishes(dishes);
        assertEquals(answer.getChoosenDishes().size(), 3);
        answer.removeChoosenDish(NEW_KEY);
        assertEquals(answer.getChoosenDishes().size(), 2);

        List<String> dishesListKeys = new ArrayList<>();
        dishesListKeys.add("1");
        answer.setChoosenDishesKeys(dishesListKeys);
        assertEquals(answer.getChoosenDishes().size(), 1);

        List<Dish> dishesList = new ArrayList<>();
        Dish testDish = new Dish("Comida", 4.0);
        testDish.setId("5678");
        dishesList.add(testDish);
        answer.setChoosenDishesList(dishesList);
        assertEquals(answer.getChoosenDishes().size(), 1);
    }

    @Test
    public void testGetIdMenu() throws Exception {
        assertEquals(idMenu,answer.getIdMenu());
    }

    @Test
    public void testSetIdMenu() throws Exception {
        answer.setIdMenu("2");
        assertEquals("2",answer.getIdMenu());
    }

    @Test
    public void testGetId() throws Exception {
        answer.setId("2");
        assertEquals("2",answer.getId());
    }

    @Test
    public void testGetGuest() throws Exception {
        assertEquals(guest,answer.getGuest());
    }

    @Test
    public void testSetGuest() throws Exception {
        answer.setGuest("CR7");
        assertEquals("CR7",answer.getGuest());
    }

    @Test
    public void testGetDate() throws Exception {
        assertEquals(date, answer.getDate());
    }

    @Test
    public void testSetDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 2);
        calendar.set(Calendar.MINUTE, 30);
        answer.setDate(calendar.getTime());
        assertEquals(calendar.getTime(), answer.getDate());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(answer.containsChoosenDish(dish1.getId()));
    }

    @Test
    public void testAdd() throws Exception {
        answer.addChoosenDish("NEW_DISH");
        assertTrue(answer.containsChoosenDish("NEW_DISH"));
        assertEquals(answer.getChoosenDishes().size(), 3);
        Dish dish = new Dish("Comida", 5.0);
        dish.setId("30125");
        answer.addChoosenDish(dish);
        assertEquals(answer.getChoosenDishes().size(), 4);
    }

}