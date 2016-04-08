package dev.blunch.blunch.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;
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
public class DishServiceTest {

    private DishService service;
    private MockRepository<Dish> repository;
    private Repository.OnChangedListener.EventType lastChangedType;
    private Dish newDish;
    private Dish oldDish;

    @Before
    public void setUp() {
        repository = new MockRepository<Dish>();
        service = new DishService(repository);
        newDish = new Dish("Patata");
        oldDish = new Dish("Batata");

        repository.insert(oldDish);
        lastChangedType = null;
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
    }

    @Test
    public void onSave() {
        service.save(newDish);

        assertEquals(2, repository.all().size());
    }

    @Test
    public void resizes(){
        assertEquals(1,service.getAmount());
        repository.insert(newDish);
        assertEquals(2,service.getAmount());
    }

    @Test
    public void onDelete() {
        assertEquals(1,service.getAmount());
        service.delete(oldDish.getId());
        assertEquals(0,service.getAmount());
    }

    @Test
    public void getMenu() {
        repository.insert(newDish);
        assertEquals(newDish,service.get(newDish.getId()));
    }


    @Test
    public void getUnexistingMenu() {
        assertEquals(null,service.get("lasdjalksdj"));
    }

    @Test
    public void onSaveExisting() {
        assertEquals("Batata", oldDish.getName());
        String newName = "Pilota";
        oldDish.setName(newName);
        String oldID = oldDish.getId();
        service.save(oldDish);
        assertEquals(oldID, oldDish.getId());
        assertEquals(oldID,service.get(oldDish.getId()).getId());
        assertEquals(newName,service.get(oldDish.getId()).getName());

    }

    @Test
    public void onAddExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        repository.simulateExternalAddition(newDish);
        assertEquals(2, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Added,lastChangedType);
    }

    @Test
    public void onMoveExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        repository.simulateExternalMove(oldDish);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Moved,lastChangedType);
    }

    @Test
    public void onChangeExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        assertEquals("Batata", oldDish.getName());
        String newName = "Pilota";
        oldDish.setName(newName);
        repository.simulateExternalChange(oldDish);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Changed,lastChangedType);
        assertEquals(newName,service.get(oldDish.getId()).getName());
    }

    @Test
    public void onDeleteExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        repository.simulateExternalDelete(oldDish);
        assertEquals(0, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Removed,lastChangedType);
    }


}