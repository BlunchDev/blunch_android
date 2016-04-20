package dev.blunch.blunch.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
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
        newMenu = new CollaborativeMenu(
                "Menu de micro de la FIB",
                "Encarna", "És un menu de micro de la FIB",
                "DA FIB", new Date(10), new Date(), null, null
        );
        oldMenu = new CollaborativeMenu(
                "Menu del vertex",
                "Victor", "Tinc micros tambe",
                "Vertex", new Date(1231), new Date(), null,null
        );
        repository.insert(oldMenu);
        lastChangedType = null;
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
        assertEquals(oldID,service.get(oldMenu.getId()).getId());
        assertEquals(newAuthor,service.get(oldMenu.getId()).getAuthor());

    }

    @Test
    public void onAddExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
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
        assertEquals(1,service.getAmount());
        repository.simulateExternalDelete(oldMenu);
        assertEquals(0, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Removed,lastChangedType);
    }

}
