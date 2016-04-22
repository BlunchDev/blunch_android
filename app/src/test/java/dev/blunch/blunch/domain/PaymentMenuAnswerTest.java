package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by casassg on 22/04/16.
 *
 * @author casassg
 */
public class PaymentMenuAnswerTest {

    private PaymentMenuAnswer answer;
    private String idMenu;
    private Dish dish;

    @Before
    public void setUp() throws Exception {
        dish = new Dish("HELLOW");
        idMenu = "42";
        answer = new PaymentMenuAnswer(Collections.singletonList(dish), idMenu);

    }

    @Test
    public void testGetChoosenDishes() throws Exception {
        assertEquals(Collections.singletonList(dish),answer.getChoosenDishes());
    }

    @Test
    public void testSetChoosenDishes() throws Exception {
        answer.setChoosenDishes(null);
        assertNotSame(Collections.singletonList(dish),answer.getChoosenDishes());
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

}