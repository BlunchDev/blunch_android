package dev.blunch.blunch.utils;

import android.content.Context;
import android.content.Intent;

import dev.blunch.blunch.activity.GetCollaborativeMenuActivity;
import dev.blunch.blunch.activity.GetPaymentMenuActivity;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.PaymentMenu;

/**
 * Created by casassg on 13/05/16.
 *
 * @author casassg
 */
public final class IntentUtils {
    public static Intent getMenuDetailIntent(Menu menu, Context context){
        Class activity;
        if (menu instanceof PaymentMenu)
            activity = GetPaymentMenuActivity.class;
        else
            activity = GetCollaborativeMenuActivity.class;
        return new Intent(context,activity);
    }
}
