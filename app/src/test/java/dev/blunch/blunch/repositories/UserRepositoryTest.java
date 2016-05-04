package dev.blunch.blunch.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertEquals;

/**
 * Created by jmotger on 3/05/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserRepositoryTest {

    @Test
    public void check_object_reference(){
        UserRepository repository = new UserRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(), "user");
    }

}
