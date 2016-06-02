package dev.blunch.blunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jmotger on 4/05/16.
 */
public class Preferences {

    private static final String PACKAGE_NAME = "dev.blunch.blunch";
    private static final String EMAIL_KEY = "user_email";

    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static String getCurrentUserEmail() {
        if (prefs == null){
            return "";
        }
        return prefs.getString(EMAIL_KEY, "");
    }

    public static void setCurrentUserEmail(String email) {
        prefs.edit().putString(EMAIL_KEY, email.split("\\.")[0]).apply();
    }

}
