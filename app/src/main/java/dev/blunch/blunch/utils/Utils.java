package dev.blunch.blunch.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import dev.blunch.blunch.R;

/**
 * Utils class
 * @author albert
 */
public class Utils {

    public static String generateId() {
        return new Firebase("https://blunch.firebaseio.com/").push().getKey();
    }

    public static void showLocationDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.location_request_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settings = new Intent("com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS");
                context.startActivity(settings);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setMessage(R.string.location_request_message);
        builder.create().show();
    }

    public static LatLng getLocationFromAddress(String address, Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> results = null;
        LatLng coordenades = null;
        try {
            try {
                results = coder.getFromLocationName(address, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (results == null) {
                return null;
            }
            Address location = results.get(0);
            coordenades = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordenades;
    }

}
