package dev.blunch.blunch.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by jmotger on 6/04/16.
 */
@SuppressWarnings("all")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DishRepositoryTest {

    @Test
    public void check_object_reference(){
        DishRepository repository = new DishRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(), "dish");
    }

    @Test
    public void check_null_dataSnapshot(){
        DishRepository repository = new DishRepository(RuntimeEnvironment.application);
        assertNull(repository.convert(null));
    }

}
