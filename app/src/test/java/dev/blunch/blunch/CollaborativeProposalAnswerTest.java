package dev.blunch.blunch;

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

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.DishService;
import dev.blunch.blunch.utils.FirebaseRepository;
import dev.blunch.blunch.utils.MockRepository;

/**
 * Created by jmotger on 6/04/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CollaborativeProposalAnswerTest {

    private MockRepository<Dish> repositoryDish;
    private MockRepository<CollaborativeMenu> repositoryMenu;
    private MockRepository<CollaborativeMenuAnswer> repositoryMenuAnswer;
    private DishService dishService;
    private CollaborativeMenuService collaborativeMenuService;

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
        repositoryDish = new MockRepository<Dish>();
        dishService= new DishService(repositoryDish);


        final Dish dish1 = new Dish(dishName1);
        final Dish dish2 = new Dish(dishName2);

        offeredDishes = new HashSet<>();
        suggestedDishes = new HashSet<>();

        repositoryDish.insert(dish1);
        repositoryDish.insert(dish2);

        offeredDishes.add(dish1.getId());
        suggestedDishes.add(dish2.getId());

        final CollaborativeMenu collaborativeMenu = new CollaborativeMenu(name, author, description,
                localization, dateStart, dateEnd, offeredDishes, suggestedDishes);
    }

    @After
    public void after() {
        //collaborativeMenuRepository.close();
        //dishRepository.close();
    }

    @Test
    public void answer_to_collaborativeMenu() {

    }

    @Test
    public void error_if_collaborativeMenu_does_not_exist() {

    }

    @Test
    public void answer_to_collaborativeMenuAnswer_with_suggested_dishes() {

    }

    @Test
    public void answer_to_collaborativeMenuAnswer_with_new_dishes() {

    }

    public static void runAndWaitUntil(FirebaseRepository<CollaborativeMenuAnswer> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
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
