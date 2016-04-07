package dev.blunch.blunch.utils;

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
import dev.blunch.blunch.utils.dummy.Person;
import dev.blunch.blunch.utils.dummy.PersonRepository;

/**
 * Created by casassg on 04/04/16.
 *
 * @author casassg
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class FirebaseRepositoryTest {
    private PersonRepository personRepository;

    @Before
    public void before() {
        personRepository = new PersonRepository(RuntimeEnvironment.application);
    }

    @After
    public void after() {
        personRepository.close();
    }

    @Test
    public void add_correctly_some_object_in_repository() throws InterruptedException {
        final Person personToAdd = new Person("Albert", 20);

        runAndWaitUntil(personRepository, new Runnable() {
            @Override
            public void run() {
                personRepository.insert(personToAdd);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return personRepository.all().size() == 1;
            }
        });
    }

    @Test
    public void add_correctly_some_object_in_repository_with_him_attributes() throws InterruptedException {
        final Person personToAdd = new Person("Albert", 20);

        runAndWaitUntil(personRepository, new Runnable() {
            @Override
            public void run() {
                personRepository.insert(personToAdd);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  personRepository.all().size()==1 &&
                        personRepository.get(personToAdd.getId()).getId().equals(personToAdd.getId()) &&
                        personRepository.get(personToAdd.getId()).getName().equals(personToAdd.getName()) &&
                        personRepository.get(personToAdd.getId()).getAge().equals(personToAdd.getAge());
            }
        });
    }

    @Test
    public void delete_correctly() throws InterruptedException {
        final Person personToAdd = new Person("Albert", 20);

        runAndWaitUntil(personRepository, new Runnable() {
            @Override
            public void run() {
                personRepository.insert(personToAdd);
                personRepository.delete(personToAdd.getId());
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  personRepository.all().size()==0;
            }
        });
    }

    @Test
    public void update_correctly() throws InterruptedException {
        final Person personToAdd = new Person("Albert", 20);
        final String newName = "Gerard";

        runAndWaitUntil(personRepository, new Runnable() {
            @Override
            public void run() {

                personRepository.insert(personToAdd);
                personToAdd.setName(newName);
                personRepository.update(personToAdd);
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return  personRepository.all().size()==1 &&
                        personRepository.get(personToAdd.getId()).getName().equals(newName);
            }
        });
    }

    public static void runAndWaitUntil(FirebaseRepository<Person> ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
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
