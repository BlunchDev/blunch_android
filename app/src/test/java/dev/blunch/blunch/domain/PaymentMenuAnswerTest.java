package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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
    private String guest;
    private Date date;

    @Before
    public void setUp() throws Exception {
        dish = new Dish("HELLOW");
        idMenu = "42";
        guest = "Manolo";
        date = Calendar.getInstance().getTime();
        answer = new PaymentMenuAnswer(idMenu, guest, date, Collections.singletonList(dish));

    }

    @Test
    public void testGetChoosenDishes() throws Exception {
        assertEquals(1, 1);
    }

    @Test
    public void testSetChoosenDishes() throws Exception {
        assertNotSame(1, 2);
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