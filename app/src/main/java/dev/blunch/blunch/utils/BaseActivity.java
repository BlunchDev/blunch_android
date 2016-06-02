package dev.blunch.blunch.utils;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by casassg on 02/06/16.
 *
 * @author casassg
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Preferences.init(getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Preferences.init(getApplicationContext());
    }
}
