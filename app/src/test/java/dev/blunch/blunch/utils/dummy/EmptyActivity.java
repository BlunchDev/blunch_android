package dev.blunch.blunch.utils.dummy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dev.blunch.blunch.R;

/**
 * Empty Activity class
 * @author albert
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);
    }

}
