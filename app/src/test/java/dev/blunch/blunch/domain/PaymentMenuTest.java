package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PaymentMenuTest {

    PaymentMenu paymentMenu;

    @Before
    public void before() {
        paymentMenu = new PaymentMenu();
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


}
