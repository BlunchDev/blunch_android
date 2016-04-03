package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.firebase.client.Firebase;
import java.util.ArrayList;
import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.menus.Menu;
import dev.blunch.blunch.repository.CollaborativeDishesRepository;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    public static final String NAME_TAG = "name";
    public static final String DESCRIPTION_TAG = "description";
    public static final String ADDRESS_TAG = "address";
    public static final String SUGGESTED_TAG = "suggested";
    public static final String COLLABORATIVE_MENU_TAG = "collaborative_menus";
    public static final String COLLABORATIVE_DISHES_TAG = "collaborative_dishes";
    public static final String FIREBASE_URI = "https://blunch.firebaseio.com/";

    private CollaborativeDishesRepository collaborativeDishesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        collaborativeDishesRepository = new CollaborativeDishesRepository();
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {
        TextView menuName = (TextView) findViewById(R.id.nomMenu);
        ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ArrayList<TextView> dishes = new ArrayList<>();
        dishes.add((TextView) findViewById(R.id.dish1));

        ImageButton timeTable = (ImageButton) findViewById(R.id.timetablebutton);
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog amb time picker-> en tornar del dialog l'horari queda escrit a hora inici i hora fi
            }
        });

    }

    private void addCollaborativeMenu(Menu menu) {

    }
}
