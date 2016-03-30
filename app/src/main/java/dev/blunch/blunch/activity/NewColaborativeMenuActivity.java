package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;

import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Plate;

public class NewColaborativeMenuActivity extends AppCompatActivity {

    public static final String COLLABORATIVE_MENU_TAG = "payment_menus/";
    private static final String FIREBASE_URI = "https://blunch.firebaseio.com/";

    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef = new Firebase(FIREBASE_URI);

    }

    private void addCollaborativeMenu(String name, String description, String address, List<Plate> plates) {
        String addCollaborativeMenuURI = FIREBASE_URI + COLLABORATIVE_MENU_TAG;
        mRef = new Firebase(addCollaborativeMenuURI);
    }
}
