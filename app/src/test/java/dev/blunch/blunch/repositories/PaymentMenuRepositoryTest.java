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
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger on 6/04/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class PaymentMenuRepositoryTest {

    private PaymentMenuRepository       paymentMenuRepository;
    private DishRepository              dishRepository;

    private final String name = "Comida mexicana";
    private final String author = "Donald Trump";
    private final String description = "Ricos tacos y burritos de este hermoso país";
    private final String localization = "local_test";
    private final Date dateStart = Calendar.getInstance().getTime();
    private final Date dateEnd = Calendar.getInstance().getTime();
    private Set<String> dishes;

    private final String dishName1 = "Burritos";
    private final String dishName2 = "Nachos";

    @Before
    public void before() {
        paymentMenuRepository = new PaymentMenuRepository(RuntimeEnvironment.application);
        dishRepository = new DishRepository(RuntimeEnvironment.application);

        final Dish dish1 = new Dish(dishName1);
        final Dish dish2 = new Dish(dishName2);

        dishes = new HashSet<>();

        dishRepository.insert(dish1);
        dishRepository.insert(dish2);

        dishes.add(dish1.getId());
        dishes.add(dish2.getId());
    }

    @After
    public void after() {
        paymentMenuRepository.close();
        dishRepository.close();
    }

    @Test
    public void add_new_paymentMenu_in_repository() throws InterruptedException {
        final PaymentMenu paymentMenu = new PaymentMenu(name, author,
                description, localization, dateStart, dateEnd, dishes);

        runAndWaitUntil(paymentMenuRepository, new Runnable() {
            @Override
            public void run() {
                paymentMenuRepository.insert(paymentMenu);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return paymentMenuRepository.all().size() == 1 &&
                        paymentMenuRepository.get(paymentMenu.getId()).getId().equals(paymentMenu.getId()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getName().equals(paymentMenu.getName()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getDescription().equals(paymentMenu.getDescription()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getAuthor().equals(paymentMenu.getAuthor()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getLocalization().equals(paymentMenu.getLocalization()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getDateStart().equals(paymentMenu.getDateStart()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getDateEnd().equals(paymentMenu.getDateEnd()) &&
                        paymentMenuRepository.get(paymentMenu.getId()).getDishes().equals(paymentMenu.getDishes());
            }
        });
    }

    public void delete_paymentMenu_from_repository() throws InterruptedException {
        final PaymentMenu paymentMenu = new PaymentMenu(name, author,
                description, localization, dateStart, dateEnd, dishes);

        runAndWaitUntil(paymentMenuRepository, new Runnable() {
            @Override
            public void run() {
                paymentMenuRepository.insert(paymentMenu);
                paymentMenuRepository.delete(paymentMenu.getId());
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return paymentMenuRepository.all().size() == 0;
            }
        });
    }

    @Test
    public void insert_paymentMenu_with_defined_id_correctly() throws InterruptedException {
        final String TEST_KEY = "TEST_KEY";
        final PaymentMenu paymentMenu = new PaymentMenu(name, author,
                description, localization, dateStart, dateEnd, dishes);

        runAndWaitUntil(paymentMenuRepository, new Runnable() {
            @Override
            public void run() {
                paymentMenuRepository.insertWithId(paymentMenu, TEST_KEY);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return paymentMenuRepository.all().size() == 1 &&
                        paymentMenuRepository.get(TEST_KEY).getId().equals(TEST_KEY);
            }
        });
    }

    @Test
    public void update_paymentMenu_correctly() throws InterruptedException {
        final PaymentMenu paymentMenu = new PaymentMenu(name, author,
                description, localization, dateStart, dateEnd, dishes);
        final String newName = "American breakfast";

        runAndWaitUntil(paymentMenuRepository, new Runnable() {
            @Override
            public void run() {

                paymentMenuRepository.insert(paymentMenu);
                paymentMenu.setName(newName);
                paymentMenuRepository.update(paymentMenu);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return paymentMenuRepository.all().size() == 1 &&
                        paymentMenuRepository.get(paymentMenu.getId()).getName().equals(newName);
            }
        });
    }

    public static void runAndWaitUntil(FirebaseRepository<PaymentMenu> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
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
