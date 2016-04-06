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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger on 6/04/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DishRepositoryTest {

    private DishRepository dishRepository;

    @Before
    public void before() {
        dishRepository = new DishRepository(RuntimeEnvironment.application);
    }

    @After
    public void after() {
        dishRepository.close();
    }
    
    @Test
    public void add_new_dish_in_repository() throws InterruptedException {
        final Dish dish = new Dish("Macarrones a la putanesca", 5.0);

        runAndWaitUntil(dishRepository, new Runnable() {
            @Override
            public void run() {
                dishRepository.insert(dish);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return dishRepository.all().size() == 1 &&
                        dishRepository.get(dish.getId()).getId().equals(dish.getId()) &&
                        dishRepository.get(dish.getId()).getName().equals(dish.getName());
            }
        });
    }

    public void delete_dish_from_repository() throws InterruptedException {
        final Dish dish = new Dish("Macarrones a la putanesca", 5.0);

        runAndWaitUntil(dishRepository, new Runnable() {
            @Override
            public void run() {
                dishRepository.insert(dish);
                dishRepository.delete(dish.getId());
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  dishRepository.all().size()==0;
            }
        });
    }

    @Test
    public void insert_dish_with_defined_id_correctly() throws InterruptedException {
        final String TEST_KEY = "TEST_KEY";
        final Dish dish = new Dish("Macarrones a la putanesca", 5.0);

        runAndWaitUntil(dishRepository, new Runnable() {
            @Override
            public void run() {
                dishRepository.insertWithId(dish, TEST_KEY);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  dishRepository.all().size()==1 &&
                        dishRepository.get(TEST_KEY).getId().equals(TEST_KEY);
            }
        });
    }

    @Test
    public void update_dish_correctly() throws InterruptedException {
        final Dish dish = new Dish("Macarrones a la putanesca", 5.0);
        final String newName = "Gerard";

        runAndWaitUntil(dishRepository, new Runnable() {
            @Override
            public void run() {

                dishRepository.insert(dish);
                dish.setName(newName);
                dishRepository.update(dish);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  dishRepository.all().size()==1 &&
                        dishRepository.get(dish.getId()).getName().equals(newName);
            }
        });
    }
    
    public static void runAndWaitUntil(FirebaseRepository<Dish> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
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
