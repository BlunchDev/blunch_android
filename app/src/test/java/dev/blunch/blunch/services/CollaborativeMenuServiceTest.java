package dev.blunch.blunch.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.MockRepository;
import dev.blunch.blunch.utils.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CollaborativeMenuServiceTest {

    private CollaborativeMenuService service;
    private MockRepository<CollaborativeMenu> repository;
    private CollaborativeMenu newMenu;
    private CollaborativeMenu oldMenu;
    private Repository.OnChangedListener.EventType lastChangedType;

    @Before
    public void setUp() {
        repository = new MockRepository<>();
        service = new CollaborativeMenuService(repository);
        service = new CollaborativeMenuService(repository, new MockRepository<Dish>());
        service = new CollaborativeMenuService(repository, new MockRepository<Dish>(),
                new MockRepository<CollaborativeMenuAnswer>(), new MockRepository<User>());
        newMenu = new CollaborativeMenu(
                "Menu de micro de la FIB",
                "Encarna", "És un menu de micro de la FIB",
                "DA FIB", new Date(10), new Date(), null, null
        );
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Tacos", 0.));
        List<Dish> offeredDishes = new ArrayList<>();
        offeredDishes.add(new Dish("Langosta", 0.));
        oldMenu = new CollaborativeMenu(
                "Menu del vertex",
                "Victor", "Tinc micros tambe",
                "Vertex", new Date(1231), new Date(), new ArrayList<Dish>(), new ArrayList<Dish>()
        );
        service.save(oldMenu, offeredDishes, dishes);
        lastChangedType = null;
        service.setCollaborativeMenuAnswerListener(null);
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
    }

    public List<CollaborativeMenu> generatDummyData(int seed, int size) {
        List<CollaborativeMenu> menus = new ArrayList<>();
        for (int i = seed; i < seed + size; ++i) {
            CollaborativeMenu menu = new CollaborativeMenu("test" + i, "test" + i, "BCN", "BCN", new Date(19 + i), new Date(), null, null);
            menus.add(menu);
        }
        return menus;
    }

    @Test
    public void onSave() {
        service.save(newMenu);

        assertEquals(2, repository.all().size());
    }

    @Test
    public void resizes(){
        assertEquals(1,service.getAmount());
        repository.insert(newMenu);
        assertEquals(2, service.getAmount());
        assertEquals(2, service.getAll().size());
    }

    @Test
    public void onDelete() {
        assertEquals(1,service.getAmount());
        service.delete(oldMenu.getId());
        assertEquals(0,service.getAmount());
    }

    @Test
    public void getMenu() {
        repository.insert(newMenu);
        assertEquals(newMenu,service.get(newMenu.getId()));
    }


    @Test
    public void getUnexistingMenu() {
        assertEquals(null,service.get("lasdjalksdj"));
    }

    @Test
    public void onSaveExisting() {
        assertEquals("Victor", oldMenu.getAuthor());
        String newAuthor = "Manuel";
        oldMenu.setAuthor(newAuthor);
        String oldID = oldMenu.getId();
        service.save(oldMenu);
        assertEquals(oldID, oldMenu.getId());
        assertEquals(oldID,service.get(oldMenu.getId()).getId());
        assertEquals(newAuthor,service.get(oldMenu.getId()).getAuthor());

    }

    @Test
    public void onAddExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1, service.getAmount());
        repository.simulateExternalAddition(newMenu);
        assertEquals(2, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Added,lastChangedType);
    }

    @Test
    public void onMoveExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        repository.simulateExternalMove(oldMenu);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Moved,lastChangedType);
    }

    @Test
    public void onChangeExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        assertEquals("Victor", oldMenu.getAuthor());
        String newAuthor = "Manuel";
        oldMenu.setAuthor(newAuthor);
        repository.simulateExternalChange(oldMenu);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Changed,lastChangedType);
        assertEquals(newAuthor,service.get(oldMenu.getId()).getAuthor());
    }

    @Test
    public void onDeleteExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1, service.getAmount());
        repository.simulateExternalDelete(oldMenu);
        assertEquals(0, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Removed,lastChangedType);
    }

    @Test
    public void reply() {
        try {
            String id = repository.all().get(0).getId();
            Date date = Calendar.getInstance().getTime();
            CollaborativeMenuAnswer collaborativeMenuAnswer = new CollaborativeMenuAnswer("Pedro",
                    id, date, service.getSuggestedDishes(id));
            List<Dish> dishes = new ArrayList<>();
            dishes.add(new Dish("Patatas a la riojana", 0.));
            CollaborativeMenuAnswer newCollaborativeMenuAnswer = service.reply(collaborativeMenuAnswer, dishes);

            assertEquals("Pedro", newCollaborativeMenuAnswer.getGuest());
            assertEquals(id, newCollaborativeMenuAnswer.getId());
            assertEquals(date, newCollaborativeMenuAnswer.getDate());
            assertEquals(2, newCollaborativeMenuAnswer.getOfferedDishes().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDishes() {
        assertEquals(service.getOfferedDishes(repository.all().get(0).getId()).size(), 1);
        assertEquals(service.getOfferedDishes(repository.all().get(0).getId()).get(0).getName(), "Langosta");
        assertEquals(service.getSuggestedDishes(repository.all().get(0).getId()).size(), 1);
        assertEquals(service.getSuggestedDishes(repository.all().get(0).getId()).get(0).getName(), "Tacos");
        String tacosID = service.getSuggestedDishes(repository.all().get(0).getId()).get(0).getId();
        assertEquals(service.getDish(tacosID).getName(), "Tacos");
    }


    @Test
    public void getActiveProposals(){
        List<Dish> disheshost = new ArrayList<>();
        CollaborativeMenuAnswer answer = new CollaborativeMenuAnswer("Guest",oldMenu.getId(),new Date(10),disheshost);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Pollo"));
        try {
            service.reply(answer, dishes);
        }catch(Exception e){
            //mmm...hola!
        }
        List<CollaborativeMenuAnswer> answers = service.getProposal(oldMenu.getId());
        assertNotNull(answers);
        assertEquals(answers.size(), 1);
        assertEquals(answer.getId(), answers.get(0).getId());
        assertEquals(answer.getMenuId(),answers.get(0).getMenuId());
        assertEquals(answer.getGuest(),answers.get(0).getGuest());
        assertEquals(answer.getDate(),answers.get(0).getDate());
    }

    @Test
    public void deleteProposalOnDecline(){
        List<Dish> disheshost = new ArrayList<>();
        CollaborativeMenuAnswer answer = new CollaborativeMenuAnswer("Guest",oldMenu.getId(),new Date(10),disheshost);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Pollo"));
        try {
            service.reply(answer, dishes);
        }catch(Exception e){
            //mmm...hola!
        }
        service.declineProposal(answer.getId());
        List<CollaborativeMenuAnswer> answers = service.getProposal(oldMenu.getId());
        assertEquals(answers.size(),0);
    }

    @Test
    public void modifyCollaborativeMenuOnAccept(){
        List<Dish> disheshost = new ArrayList<>();
        CollaborativeMenuAnswer answer = new CollaborativeMenuAnswer("Guest",oldMenu.getId(),new Date(10),disheshost);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("POLLO"));
        for (Map.Entry<String, Object> entry : oldMenu.getSuggestedDishes().entrySet()) {
            dishes.add(new Dish(entry.getKey()));
        }
        try {
            service.reply(answer, dishes);
        }catch(Exception e){
            //mmm...hola!
        }
        service.acceptProposal(answer.getId());
        List<CollaborativeMenuAnswer> answers = service.getProposal(oldMenu.getId());
        assertEquals(answers.size(),0);
        CollaborativeMenu menuHost = service.get(oldMenu.getId());
        for (Dish d : dishes){
            assertTrue(menuHost.containsOfferedDish(d.getId()));
            assertFalse(menuHost.containsSuggestedDish(d.getId()));
        }
    }


//    @Test
//    public void onSave

}
