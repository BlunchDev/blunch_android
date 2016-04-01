package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;

import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Plate;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    public static final String NAME_TAG = "name";
    public static final String DESCRIPTION_TAG = "description";
    public static final String ADDRESS_TAG = "address";
    public static final String PLATE_TAG = "plates";
    public static final String PRICE_TAG = "price";
    public static final String COLLABORATIVE_MENU_URI = "collaborative_menus/";
    public static final String PLATES_URI = "plates/";
    public static final String FIREBASE_URI = "https://blunch.firebaseio.com/";

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

        // TEST
        List<Plate> plates = new LinkedList<>();
        plates.add(new Plate("Fish", 2.0));
        plates.add(new Plate("Salad", 1.0));
        plates.add(new Plate("Fruit", 0.5));
        addCollaborativeMenu("Wakiki lunch", "Muy bueno", "C/Numancia", plates);
    }

    private void addCollaborativeMenu(String name, String description, String address, List<Plate> plates) {
        Firebase menuRef = new Firebase(FIREBASE_URI + COLLABORATIVE_MENU_URI);
        Firebase platesRef = new Firebase(FIREBASE_URI + PLATES_URI);

        menuRef = menuRef.push();

        for (Plate plate : plates) {
            Firebase platesRefInd = platesRef.push();
            plate.setId(platesRefInd.getKey());
            platesRefInd.child(NAME_TAG).setValue(plate.getName());
            platesRefInd.child(PRICE_TAG).setValue(plate.getPrice());
            platesRefInd.child(COLLABORATIVE_MENU_URI.substring(0, COLLABORATIVE_MENU_URI.length()-1)).
                                                                child(menuRef.getKey()).setValue(true);
        }

        menuRef.child(NAME_TAG).setValue(name);
        menuRef.child(DESCRIPTION_TAG).setValue(description);
        menuRef.child(ADDRESS_TAG).setValue(address);
        menuRef = menuRef.child(PLATES_URI.substring(0, PLATES_URI.length()-1));
        for (Plate plate : plates) menuRef.child(plate.getId()).setValue(true);
    }
}
