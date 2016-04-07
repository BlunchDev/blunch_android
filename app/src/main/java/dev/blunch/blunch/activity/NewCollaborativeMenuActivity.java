package dev.blunch.blunch.activity;

import android.app.TimePickerDialog;
import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.view.CollaborativeDishLayout;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    private int iHour, iMinut, fHour, fMinut;
    private int numDish;
    private Date start, finish;
    private ArrayList<CollaborativeDishLayout> myDishes = new ArrayList<>();
    private List<ImageButton> idClose = new ArrayList<>();

    private DishRepository dishRepository;
    private CollaborativeMenuRepository collaborativeMenuRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dishRepository = new DishRepository(getApplicationContext());
        collaborativeMenuRepository = new CollaborativeMenuRepository(getApplicationContext());
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {
        iHour = iMinut = fHour = fMinut = 0;
        numDish = 1;

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

        iHour = iMinut = fHour = fMinut = 0;

        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollaborativeMenu();
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
        // TODO El menu es glutenfree
    }

    private void isVegetarian() {
        // TODO El menu es vegetaria
    }

    private void updateTime(int iHour, int iMinut, int fHour, int fMinut) {
        TextView mDateDisplay = (TextView) findViewById(R.id.timeText);
        if(iHour % 10 >= 1) {
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
                    iHour = hourOfDay; iMinut = minute;
                }
            };

    private TimePickerDialog.OnTimeSetListener finalTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    fHour = hourOfDay; fMinut = minute;
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

    private void createCollaborativeMenu() {

        final String author = "Admin";
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);

        EditText address = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        final String localization = address.getText().toString() + ", " + city.getText().toString();

        EditText description = (EditText) findViewById(R.id.description);

        // TODO Hacer una funcion que convierta a Date las fechas.
        Date startDate = new Date();
        Date endDate = new Date();

        Set<String> dishKeys = new LinkedHashSet<>();

        EditText dish1 = (EditText) findViewById(R.id.dish1);
        Switch who1 = (Switch) findViewById(R.id.switch1);

        // TODO Pillar el resultado de cada switch y separar por offered and suggested.

        Dish firstDish = new Dish(dish1.getText().toString(), 0.0);
        dishRepository.insert(firstDish);
        dishKeys.add(firstDish.getId());

        for (CollaborativeDishLayout d : myDishes) {
            Dish dish = new Dish(d.getNomPlat(), 0.0);
            dishRepository.insert(dish);
            dishKeys.add(dish.getId());
        }

        CollaborativeMenu collaborativeMenu = new CollaborativeMenu(    nameMenu.getText().toString(),
                                                                        author,
                                                                        description.getText().toString(),
                                                                        localization,
                                                                        startDate,
                                                                        endDate,
                                                                        dishKeys,
                                                                        dishKeys);
        collaborativeMenuRepository.insert(collaborativeMenu);
    }
}
