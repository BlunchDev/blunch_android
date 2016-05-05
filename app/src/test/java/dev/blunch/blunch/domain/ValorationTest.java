package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Valoration Test
 * @author albert
 */
public class ValorationTest {

    private static final String ID = "id";
    private static final String HOST = "pepito@gmail";
    private static final String GUEST = "juanito@gmail";
    private static final String MENU = "1234";
    private static final String COMMENT = "Mu rico";
    private static final Double POINTS = 4.5;

    Valoration valoration;

    @Before
    public void before() {
        valoration = new Valoration(HOST, GUEST, MENU, POINTS, COMMENT);
        valoration.setId(ID);
    }

    @Test
    public void create_correctly_a_dish() throws Exception {
        assertEquals(valoration.getId(), ID);
        assertEquals(valoration.getHost(), HOST);
        assertEquals(valoration.getGuest(), GUEST);
        assertEquals(valoration.getMenu(), MENU);
        assertEquals(valoration.getComment(), COMMENT);
        assertEquals(valoration.getPoints(), POINTS, 0.1);
    }

    @Test
    public void update_dish() throws Exception {
        final String newID = "id2";
        final String newHOST = "manolo@gmail";
        final String newGUEST = "lama@gmail";
        final String newCOMMENT = "El bicho el mejor";
        final String newMENU = "4567";
        final Double newPOINTS = 4.0;

        valoration.setId(newID);
        assertEquals(valoration.getId(), newID);
        valoration.setHost(newHOST);
        assertEquals(valoration.getHost(), newHOST);
        valoration.setGuest(newGUEST);
        assertEquals(valoration.getGuest(), newGUEST);
        valoration.setComment(newCOMMENT);
        assertEquals(valoration.getComment(), newCOMMENT);
        valoration.setMenu(newMENU);
        assertEquals(valoration.getMenu(), newMENU);
        valoration.setPoints(newPOINTS);
        assertEquals(valoration.getPoints(), newPOINTS, 0.1);
    }

    @Test
    public void create_correctly_an_empty_dish() throws Exception {
        Valoration emptyValoration = new Valoration();
        assertNull(emptyValoration.getId());
        assertNull(emptyValoration.getHost());
        assertNull(emptyValoration.getGuest());
        assertNull(emptyValoration.getComment());
        assertNull(emptyValoration.getMenu());
    }


}
