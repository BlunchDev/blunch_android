package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Plate;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    public static final String NAME_TAG = "name";
    public static final String DESCRIPTION_TAG = "description";
    public static final String ADDRESS_TAG = "address";
    public static final String PLATE_TAG = "plates";
    public static final String PRICE_TAG = "price";
    public static final String COLLABORATIVE_MENU_TAG = "collaborative_menus/";
    public static final String FIREBASE_URI = "https://blunch.firebaseio.com/";

    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef = new Firebase(FIREBASE_URI);

        // TEST
        List<Plate> plates = new LinkedList<>();
        plates.add(new Plate("Fish", 2.0));
        plates.add(new Plate("Salad", 1.0));
        plates.add(new Plate("Fruit", 0.5));
        addCollaborativeMenu("Wakiki lunch", "Muy bueno", "C/Numancia", plates);
    }

    private void addCollaborativeMenu(String name, String description, String address, List<Plate> plates) {
        String addCollaborativeMenuURI = FIREBASE_URI + COLLABORATIVE_MENU_TAG;
        mRef = new Firebase(addCollaborativeMenuURI);

        mRef = mRef.push();
        mRef.child(NAME_TAG).setValue(name);
        mRef.child(DESCRIPTION_TAG).setValue(description);
        mRef.child(ADDRESS_TAG).setValue(address);
        mRef = mRef.child(PLATE_TAG);
        for (Plate plate : plates) {
            Firebase mRefPlate = mRef.push();
            mRefPlate.child(NAME_TAG).setValue(plate.getName());
            mRefPlate.child(PRICE_TAG).setValue(plate.getPrice());
        }
    }
}
