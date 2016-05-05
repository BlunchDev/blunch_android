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
 * Created by pere on 4/25/16.
 */
@SuppressWarnings("all")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class PaymentMenuAnswerRepositoryTest {
    @Test
    public void check_object_reference(){
        PaymentMenuAnswerRepository repository = new PaymentMenuAnswerRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(),"paymentMenuAnswer");
    }

    @Test
    public void check_null_dataSnapshot(){
        PaymentMenuAnswerRepository repository = new PaymentMenuAnswerRepository(RuntimeEnvironment.application);
        assertNull(repository.convert(null));
    }

}
