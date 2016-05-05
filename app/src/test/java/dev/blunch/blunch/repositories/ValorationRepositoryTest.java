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
 * Created by albert on 5/05/16.
 */
@SuppressWarnings("all")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ValorationRepositoryTest {

    @Test
    public void check_object_reference(){
        ValorationRepository repository = new ValorationRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(), "valoration");
    }

    @Test
    public void check_null_dataSnapshot(){
        ValorationRepository repository = new ValorationRepository(RuntimeEnvironment.application);
        assertNull(repository.convert(null));
    }

}
