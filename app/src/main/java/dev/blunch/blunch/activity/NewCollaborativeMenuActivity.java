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

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.dishes.CollaborativeDish;
import dev.blunch.blunch.domain.dishes.Dish;
import dev.blunch.blunch.domain.menus.CollaborativeMenu;
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
    private Date start, finish;
    private ArrayList<CollaborativeDishLayout> myDishes = new ArrayList<>();
    //private ArrayList<CollaborativeDishLayout> suggDishes = new ArrayList<>();
    private List<ImageButton> idClose = new ArrayList<>();

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
        iHour= iMinut=fHour= fMinut= 0;
        numDish=1;

        final EditText menuName = (EditText) findViewById(R.id.nomMenu);
        menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuName.getText().equals("MENÚ")) {
                    menuName.setText("");
                }
            }
        });

        final ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);
        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollaborativeDishLayout a = new CollaborativeDishLayout(NewCollaborativeMenuActivity.this, ++numDish);
                myDishes.add(a);
                moreDishesLayout.addView(a);
                ImageButton close = (ImageButton) findViewById(a.getId());
                idClose.add(close);
                /*close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreDishesLayout.removeView(a);
                    }
                });*/


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

        final ImageButton vegetarian = (ImageButton) findViewById(R.id.vegetarian);
        vegetarian.setColorFilter(R.color.black);
        vegetarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVegetarian();
                if(vegetarian.getColorFilter().equals(R.color.green)){
                    vegetarian.setColorFilter(R.color.colorAccent);
                }
                else{
                    vegetarian.setColorFilter(R.color.green);
                }
            }
        });

        final ImageButton glutenfree = (ImageButton) findViewById(R.id.glutenfree);
        glutenfree.setColorFilter(R.color.black);
        glutenfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGlutenFree();
                if(glutenfree.getColorFilter().equals(R.color.green)){
                    glutenfree.setColorFilter(R.color.colorAccent);
                }
                else{
                    glutenfree.setColorFilter(R.color.green);
                }
            }
        });
    }

    private void isGlutenFree() {
        //el menu es glutenfree
    }

    private void isVegetarian() {
        //el menu es vegetaria
    }

    private void updateTime(int iHour, int iMinut, int fHour, int fMinut) {
        TextView mDateDisplay = (TextView) findViewById(R.id.timeText);
        if(iHour%10>=1) {
            mDateDisplay.setText(iHour + ":" + iMinut + "h - " + fHour + ":" + fMinut+"h");
        }
        else{
            mDateDisplay.setText("0"+iHour + ":" + iMinut + "h - " + fHour + ":" + fMinut+"h");

        }
        Calendar cStart = Calendar.getInstance();
        cStart.set(Calendar.HOUR_OF_DAY,iHour);
        cStart.set(Calendar.MINUTE, iMinut);
        start = cStart.getTime();

        Calendar cFinish = Calendar.getInstance();
        cFinish.set(Calendar.HOUR_OF_DAY,fHour);
        cFinish.set(Calendar.MINUTE, fMinut);
        finish = cFinish.getTime();
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
                    updateTime(iHour, iMinut, fHour, fMinut);
                }
            };

    private void showDialogTime() {
        showDialogFinalTime().show();
        showDialogInitialTime().show();

    }

    private TimePickerDialog showDialogInitialTime() {
        TimePickerDialog a = new TimePickerDialog(this, initialTimeSetListener, iHour, iMinut, false);
        TextView title = new TextView(this);
        title.setText("Hora Inicio");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setCustomTitle(title);
        return a;
    }

    private TimePickerDialog showDialogFinalTime() {
        TimePickerDialog a = new TimePickerDialog(this, finalTimeSetListener, fHour, fMinut, false);
        TextView title = new TextView(this);
        title.setText("Hora Fin");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setCustomTitle(title);
        return a;
    }

    private void addCollaborativeMenu(Menu menu) {

    }

    private void createColaborativeMenu() {
        //create new menu
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);
        EditText adress = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);

        //Llegir tants plats com hi hagi!
        EditText dish1 = (EditText) findViewById(R.id.dish1);
        Switch who1 = (Switch) findViewById(R.id.switch1);

        for(CollaborativeDishLayout d: myDishes){ //llegir plats del Layout
            System.out.println("nom plat: "+ d.getNomPlat());
        }

        /*CollaborativeDish d1 = new CollaborativeDish(); //canviar plat
        d1.setName(dish1.getText().toString());
        d1.setSuggested(!who1.getShowText());


        CollaborativeMenu m = new CollaborativeMenu();
        m.setName(nameMenu.getText().toString());
        m.setAddress(adress.getText().toString()+", "+city.getText().toString());
        //falta descripcio
*/


    }
}
