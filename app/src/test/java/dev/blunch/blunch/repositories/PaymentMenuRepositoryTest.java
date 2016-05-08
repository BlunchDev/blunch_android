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
public class PaymentMenuRepositoryTest {

    @Test
    public void check_object_reference(){
        PaymentMenuRepository repository = new PaymentMenuRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(), "paymentMenu");
    }

    @Test
    public void check_null_dataSnapshot(){
        PaymentMenuRepository repository = new PaymentMenuRepository(RuntimeEnvironment.application);
        assertNull(repository.convert(null));
    }

}
