package dev.blunch.blunch.utils;

import com.firebase.client.Firebase;

/**
 * Utils class
 * @author albert
 */
public class Utils {

    public static String generateId() {
        return new Firebase("https://blunch.firebaseio.com/").push().getKey();
    }

}
