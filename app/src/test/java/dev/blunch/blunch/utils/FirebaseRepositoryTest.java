package dev.blunch.blunch.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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
    public void add_correctly_some_object_in_repository(){
        Person personToAdd = new Person("Albert", 20);
        personRepository.insert(personToAdd);
    }
}
