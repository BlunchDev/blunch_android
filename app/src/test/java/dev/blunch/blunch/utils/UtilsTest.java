package dev.blunch.blunch.utils;

import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;

import static org.junit.Assert.assertFalse;

/**
 * Created by casassg on 04/04/16.
 *
 * @author casassg
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsTest {

    @Before
    public void before() {
        Firebase.setAndroidContext(RuntimeEnvironment.application);
    }

    @Test
    public void generate_different_keys() throws Exception {
        String key1 = Utils.generateId();
        String key2 = Utils.generateId();
        assertFalse("The generated keys by FirebaseRepository need to be different", key1.equals(key2));
    }
}
