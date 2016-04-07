package dev.blunch.blunch.repositories;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger on 6/04/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CollaborativeMenuRepositoryTest {

    private CollaborativeMenuRepository collaborativeMenuRepository;
    private DishRepository              dishRepository;

    private final String name = "Comida mexicana";
    private final String author = "Donald Trump";
    private final String description = "Ricos tacos y burritos de este hermoso país";
    private final String localization = "local_test";
    private final Date dateStart = Calendar.getInstance().getTime();
    private final Date dateEnd = Calendar.getInstance().getTime();
    private Set<String> offeredDishes;
    private Set<String> suggestedDishes;

    private final String dishName1 = "Burritos";
    private final String dishName2 = "Nachos";

    @Before
    public void before() {
        collaborativeMenuRepository = new CollaborativeMenuRepository(RuntimeEnvironment.application);
        dishRepository = new DishRepository(RuntimeEnvironment.application);

        final Dish dish1 = new Dish(dishName1);
        final Dish dish2 = new Dish(dishName2);

        offeredDishes = new HashSet<>();
        suggestedDishes = new HashSet<>();

        dishRepository.insert(dish1);
        dishRepository.insert(dish2);

        offeredDishes.add(dish1.getId());
        suggestedDishes.add(dish2.getId());
    }

    @After
    public void after() {
        collaborativeMenuRepository.close();
        dishRepository.close();
    }

    @Test
    public void add_new_collaborativeMenu_in_repository() throws InterruptedException {
        final CollaborativeMenu collaborativeMenu = new CollaborativeMenu(name, author,
                description, localization, dateStart, dateEnd, offeredDishes, suggestedDishes);

        runAndWaitUntil(collaborativeMenuRepository, new Runnable() {
            @Override
            public void run() {
                collaborativeMenuRepository.insert(collaborativeMenu);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return collaborativeMenuRepository.all().size() == 1 &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getId().equals(collaborativeMenu.getId()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getName().equals(collaborativeMenu.getName()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getDescription().equals(collaborativeMenu.getDescription()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getAuthor().equals(collaborativeMenu.getAuthor()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getLocalization().equals(collaborativeMenu.getLocalization()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getDateStart().equals(collaborativeMenu.getDateStart()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getDateEnd().equals(collaborativeMenu.getDateEnd()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getOfferedDishes().equals(collaborativeMenu.getOfferedDishes()) &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getSuggestedDishes().equals(collaborativeMenu.getSuggestedDishes());
            }
        });
    }

    public void delete_collaborativeMenu_from_repository() throws InterruptedException {
        final CollaborativeMenu collaborativeMenu = new CollaborativeMenu(name, author,
                description, localization, dateStart, dateEnd, offeredDishes, suggestedDishes);

        runAndWaitUntil(collaborativeMenuRepository, new Runnable() {
            @Override
            public void run() {
                collaborativeMenuRepository.insert(collaborativeMenu);
                collaborativeMenuRepository.delete(collaborativeMenu.getId());
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return collaborativeMenuRepository.all().size() == 0;
            }
        });
    }

    @Test
    public void update_collaborativeMenu_correctly() throws InterruptedException {
        final CollaborativeMenu collaborativeMenu = new CollaborativeMenu(name, author,
                description, localization, dateStart, dateEnd, offeredDishes, suggestedDishes);
        final String newName = "American breakfast";

        runAndWaitUntil(collaborativeMenuRepository, new Runnable() {
            @Override
            public void run() {

                collaborativeMenuRepository.insert(collaborativeMenu);
                collaborativeMenu.setName(newName);
                collaborativeMenuRepository.update(collaborativeMenu);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return collaborativeMenuRepository.all().size() == 1 &&
                        collaborativeMenuRepository.get(collaborativeMenu.getId()).getName().equals(newName);
            }
        });
    }

    public static void runAndWaitUntil(FirebaseRepository<CollaborativeMenu> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
        final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(1);
        semaphore.acquire();


        FirebaseRepository.OnChangedListener listener = new FirebaseRepository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                semaphore.release();
            }
        };


        ref.setOnChangedListener(listener);
        task.run();
        boolean isDone = false;
        boolean adquired = false;
        long startedAt = System.currentTimeMillis();
        while (!adquired && System.currentTimeMillis()-startedAt<2000) {
            ShadowApplication.getInstance().getBackgroundThreadScheduler().runOneTask();
            adquired = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            try {
                isDone = done.call();
            } catch (Exception e) {
                e.printStackTrace();
                // and we're not done
            }
        }
        if (!isDone) {
            throw new AssertionFailedError();
        }
        ref.setOnChangedListener(null);
    }
    
}
