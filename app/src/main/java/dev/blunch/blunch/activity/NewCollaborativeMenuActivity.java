package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;

import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.dishes.CollaborativeDish;
import dev.blunch.blunch.domain.dishes.Dish;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    public static final String NAME_TAG = "name";
    public static final String DESCRIPTION_TAG = "description";
    public static final String ADDRESS_TAG = "address";
    public static final String SUGGESTED_TAG = "suggested";
    public static final String COLLABORATIVE_MENU_TAG = "collaborative_menus";
    public static final String COLLABORATIVE_DISHES_TAG = "collaborative_dishes";
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
        List<Dish> dishs = new LinkedList<>();
        dishs.add(new CollaborativeDish("Fish", false));
        dishs.add(new CollaborativeDish("Salad", true));
        dishs.add(new CollaborativeDish("Fruit", true));
        addCollaborativeMenu(new Menu("Wakiki lunch", "Muy bueno", "C/Numancia", dishs));
    }

    private void addCollaborativeMenu(Menu menu) {
        Firebase menuRef = new Firebase(FIREBASE_URI).child(COLLABORATIVE_MENU_TAG);
        Firebase dishesRef = new Firebase(FIREBASE_URI).child(COLLABORATIVE_DISHES_TAG);

        menuRef = menuRef.push();
        menu.setId(menuRef.getKey());

        for (Dish dish : menu.getDishs()) {
            CollaborativeDish cDish = (CollaborativeDish) dish;
            Firebase dishesRefInd = dishesRef.push();
            dish.setId(dishesRefInd.getKey());
            dishesRefInd.child(NAME_TAG).setValue(cDish.getName());
            dishesRefInd.child(SUGGESTED_TAG).setValue(cDish.isSuggested());
            dishesRefInd.child(COLLABORATIVE_MENU_TAG).child(menu.getId()).setValue(true);
        }

        menuRef.child(NAME_TAG).setValue(menu.getName());
        menuRef.child(DESCRIPTION_TAG).setValue(menu.getDescription());
        menuRef.child(ADDRESS_TAG).setValue(menu.getAddress());
        menuRef = menuRef.child(COLLABORATIVE_DISHES_TAG);
        for (Dish dish : menu.getDishs()) menuRef.child(dish.getId()).setValue(true);
    }
}
