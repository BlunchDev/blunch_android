package dev.blunch.blunch.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.MockRepository;
import dev.blunch.blunch.utils.Repository;

import static org.junit.Assert.assertEquals;

/**
 * Created by jmotger on 26/04/16.
 */
public class MenuServiceTest {

    private MenuService service;
    private MockRepository<CollaborativeMenu> collaborativeMenuMockRepository;
    private MockRepository<PaymentMenu> paymentMenuMockRepository;
    private Repository.OnChangedListener.EventType lastChangedType;
    private CollaborativeMenu collaborativeMenu;
    private PaymentMenu paymentMenu;

    @Before
    public void setUp() {
        collaborativeMenuMockRepository = new MockRepository<>();
        paymentMenuMockRepository = new MockRepository<>();
        service = new MenuService(collaborativeMenuMockRepository, paymentMenuMockRepository);
        collaborativeMenu = new CollaborativeMenu(
                "Menu de micro de la FIB",
                "Encarna", "És un menu de micro de la FIB",
                "DA FIB", new Date(10), new Date(), null, null
        );
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Tacos", 0.));
        List<Dish> offeredDishes = new ArrayList<>();
        offeredDishes.add(new Dish("Langosta", 0.));

        paymentMenu = new PaymentMenu(
                "Menu del vertex",
                "Victor", "Tinc micros tambe",
                "Vertex", new Date(1231), new Date()
        );

        collaborativeMenuMockRepository.insert(collaborativeMenu);
        paymentMenuMockRepository.insert(paymentMenu);

        lastChangedType = null;
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
    }

    @Test
    public void listCollaborativeMenus() {
        List<CollaborativeMenu> menus = service.getCollaborativeMenus();
        assertEquals(1, menus.size());
        for (CollaborativeMenu collaborativeMenu : menus) {
            assertEquals("Menu de micro de la FIB", collaborativeMenu.getName());
        }
    }

    @Test
    public void listPaymentMenus() {
        List<PaymentMenu> menus = service.getPaymentMenus();
        assertEquals(1, menus.size());
        for (PaymentMenu paymentMenu : menus) {
            assertEquals("Menu del vertex", paymentMenu.getName());
        }
    }

    @Test
    public void listMenus() {
        List<Menu> menus = service.getMenus();
        assertEquals(2, menus.size());
        for (Menu menu : menus) {
            if (menu.getClass().getSimpleName().equals("CollaborativeMenu"))
                assertEquals("Menu de micro de la FIB", menu.getName());
            else
                assertEquals("Menu del vertex", menu.getName());
        }
    }
}
