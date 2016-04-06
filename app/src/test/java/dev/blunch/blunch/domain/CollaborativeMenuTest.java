package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CollaborativeMenuTest {

    CollaborativeMenu collaborativeMenu;

    @Before
    public void before() {
        collaborativeMenu = new CollaborativeMenu();
    }

    @Test
    public void create_correctly_an_empty_collaborative_menu() {
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

}
