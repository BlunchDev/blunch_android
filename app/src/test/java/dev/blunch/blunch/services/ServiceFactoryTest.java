package dev.blunch.blunch.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertNotNull;

/**
 * Service Factory Test
 * @author albert
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ServiceFactoryTest {

    @Test
    public void test() throws Exception {
        assertNotNull(ServiceFactory.getCollaborativeMenuService(RuntimeEnvironment.application));
        assertNotNull(ServiceFactory.getPaymentMenuService(RuntimeEnvironment.application));
    }

}
