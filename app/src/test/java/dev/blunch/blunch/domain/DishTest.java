package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DishTest {

    final String ID = "id";
    final String NAME = "Pescado";
    final Double PRICE = 10.0;

    Dish dish;

    @Before
    public void before() {
        dish = new Dish(NAME, PRICE);
        dish.setId(ID);
    }

    @Test
    public void create_correctly_a_dish() throws Exception {
        assertEquals(dish.getId(), ID);
        assertEquals(dish.getName(), NAME);
        assertEquals(dish.getPrice(), PRICE);
    }

    @Test
    public void update_dish() throws Exception {
        final String newID = "id2";
        final String newNAME = "Carne";
        final Double newPRICE = 50.0;

        dish.setId(newID);
        assertEquals(dish.getId(), newID);
        dish.setName(newNAME);
        assertEquals(dish.getName(), newNAME);
        dish.setPrice(newPRICE);
        assertEquals(dish.getPrice(), newPRICE);
    }

    @Test
    public void create_correctly_an_empty_dish() throws Exception {
        Dish emptyDish = new Dish();
        assertNull(emptyDish.getId());
        assertNull(emptyDish.getName());
        assertNull(emptyDish.getPrice());
    }

}