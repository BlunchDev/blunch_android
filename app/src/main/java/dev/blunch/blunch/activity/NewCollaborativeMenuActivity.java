package dev.blunch.blunch.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.menus.Menu;
import dev.blunch.blunch.repository.CollaborativeDishesRepository;
import dev.blunch.blunch.view.CollaborativeDishLayout;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    public static final String NAME_TAG = "name";
    public static final String DESCRIPTION_TAG = "description";
    public static final String ADDRESS_TAG = "address";
    public static final String SUGGESTED_TAG = "suggested";
    public static final String COLLABORATIVE_MENU_TAG = "collaborative_menus";
    public static final String COLLABORATIVE_DISHES_TAG = "collaborative_dishes";
    public static final String FIREBASE_URI = "https://blunch.firebaseio.com/";

    private CollaborativeDishesRepository collaborativeDishesRepository;
    private int iHour, iMinut, fHour, fMinut;
    private int numDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        collaborativeDishesRepository = new CollaborativeDishesRepository(getApplicationContext());
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {
        numDish=1;
        EditText menuName = (EditText) findViewById(R.id.nomMenu);
        ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);
        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDishesLayout.addView(new CollaborativeDishLayout(NewCollaborativeMenuActivity.this, ++numDish));

            }
        });
        ArrayList<TextView> dishes = new ArrayList<>();
        dishes.add((TextView) findViewById(R.id.dish1));

        ImageButton timeTable = (ImageButton) findViewById(R.id.timetablebutton);
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog amb time picker-> en tornar del dialog l'horari queda escrit a hora inici i hora fi
                showDialogTime();
            }
        });


        iHour= iMinut=fHour= fMinut= 0;

        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColaborativeMenu();
            }
        });


    }

    private void updateTime(int iHour, int iMinut, int fHour, int fMinut) {
        TextView mDateDisplay = (TextView) findViewById(R.id.timeText);
        mDateDisplay.setText(iHour + ":" + iMinut + " - " + fHour + ":" + fMinut);
    }

    private TimePickerDialog.OnTimeSetListener initialTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    iHour = hourOfDay;
                    iMinut = minute;
                }
            };

    private TimePickerDialog.OnTimeSetListener finalTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    fHour = hourOfDay;
                    fMinut = minute;
                }
            };

    private void showDialogTime() {
        showDialogInitialTime().show();
        showDialogFinalTime().show();
        updateTime(iHour,iMinut,fHour,fMinut);

    }

    private TimePickerDialog showDialogInitialTime() {
        return new TimePickerDialog(this, initialTimeSetListener, iHour, iMinut, false);
    }

    private TimePickerDialog showDialogFinalTime() {
        return new TimePickerDialog(this, finalTimeSetListener, fHour, fMinut, false);
    }

    private void addCollaborativeMenu(Menu menu) {

    }

    private void createColaborativeMenu() { //parlar amb albert sobre: menu unic?
        //create new menu
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);
        EditText adress = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        //Llegir tants plats com hi hagi!
        EditText dish1 = (EditText) findViewById(R.id.dish1);
        Switch who1 = (Switch) findViewById(R.id.switch1);

        //CollaborativeDish d1 = new CollaborativeDish(dish1.getText().toString());


    }
}
