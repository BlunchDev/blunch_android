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

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.MockRepository;
import dev.blunch.blunch.utils.Repository;

import static org.junit.Assert.assertEquals;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class PaymentMenuServiceTest {

    private PaymentMenuService service, service2, service3;
    private MockRepository<PaymentMenu> repository;
    private PaymentMenu newMenu;
    private PaymentMenu oldMenu;
    private Repository.OnChangedListener.EventType lastChangedType;
    private Dish pernil;
    private Dish dorada;
    private MockRepository<PaymentMenuAnswer> answerRepository;

    @Before
    public void setUp() {
        repository = new MockRepository<>();
        answerRepository = new MockRepository<>();
        service = new PaymentMenuService(repository, new MockRepository<Dish>(), answerRepository, new MockRepository<User>());
        service2 = new PaymentMenuService(repository);
        service3 = new PaymentMenuService(repository,new MockRepository<Dish>());
        newMenu = new PaymentMenu(
                "Menu de micro de la FIB",
                "Encarna", "És un menu de micro de la FIB",
                "DA FIB", new Date(10), new Date(), null
        );
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Sopa", 2.0));
        oldMenu = new PaymentMenu(
                "Menu del vertex",
                "Victor", "Tinc micros tambe",
                "Vertex", new Date(1231), new Date(), null
        );
        service.save(oldMenu, dishes);
        lastChangedType = null;
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
        pernil = new Dish("Pernil", 15.0);
        dorada = new Dish("Dorada", 10.0);
    }

    public List<CollaborativeMenu> generateDummyData(int seed, int size) {
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
        service2.save(newMenu);


        assertEquals(2, repository.all().size());
    }

    @Test
    public void resizes(){
        assertEquals(1,service.getAmount());
        repository.insert(newMenu);
        assertEquals(2,service.getAmount());
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
        assertEquals(oldID, service.get(oldMenu.getId()).getId());
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
        assertEquals(1, service.getAmount());
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
    public void getDishesCorrect() {
        assertEquals(service.getDishes(repository.all().get(0).getId()).size(), 1);
        assertEquals(service.getDishes(repository.all().get(0).getId()).get(0).getName(), "Sopa");
        assertEquals(service.getDishes(repository.all().get(0).getId()).get(0).getPrice(), (Double)2.0);
    }

    @Test
    public void getPriceCorrect() {
        assertEquals(service.getTotalPrice(repository.all().get(0).getId()), (Double) 2.0);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(pernil);
        dishes.add(dorada);
        service.save(oldMenu, dishes);
        assertEquals(service.getDishes(repository.all().get(0).getId()).size(), 3);
        assertEquals(service.getTotalPrice(repository.all().get(0).getId()), (Double) 27.0);


    }


    @Test
    public void answerMenu() {
        List<Dish> dishList = new ArrayList<>();
        dishList.add(pernil);
        String guest = "Pepe";
        Date date = Calendar.getInstance().getTime();
        PaymentMenuAnswer answer = new PaymentMenuAnswer(oldMenu.getId(), guest, date, dishList);
        assertEquals(0, answerRepository.all().size());
        service.answer(oldMenu.getId(), answer);
        assertEquals(1, answerRepository.all().size());

    }


    @Test
    public void getAnswerMenu(){
        List<Dish> dishList = new ArrayList<>();
        dishList.add(pernil);
        String guest = "Pepe";
        Date date = Calendar.getInstance().getTime();
        PaymentMenuAnswer answer = new PaymentMenuAnswer(oldMenu.getId(), guest, date, dishList);
        answerRepository.insert(answer);
        assertEquals(1, answerRepository.all().size());
        List<PaymentMenuAnswer> lists = service.getAnswers(oldMenu.getId());
        assertEquals(1, lists.size());
        assertEquals(service.getAnswerDishes(answer.getId()).size(), 1);

    }

}
