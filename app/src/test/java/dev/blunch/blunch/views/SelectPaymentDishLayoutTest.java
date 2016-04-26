package dev.blunch.blunch.views;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.view.SelectPaymentDishLayout;

import static org.junit.Assert.assertFalse;

/**
 * Collaborative Dish Layout Test
 * @author albert
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@SuppressWarnings("all")
public class SelectPaymentDishLayoutTest {

    private SelectPaymentDishLayout layout;
    private final String DISH_NAME = "Pescado";
    private final Double DISH_PRICE = 5.6;

    @Before
    public void setUp() {
        layout = new SelectPaymentDishLayout(RuntimeEnvironment.application, DISH_NAME, DISH_PRICE);
    }

    @Test
    public void test() throws Exception {
        assertFalse(layout.isChecked());
    }

}
