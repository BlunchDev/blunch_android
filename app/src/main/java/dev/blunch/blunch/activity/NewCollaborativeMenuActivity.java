package dev.blunch.blunch.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.view.CollaborativeDishLayout;

@SuppressWarnings("all")
public class NewCollaborativeMenuActivity extends AppCompatActivity {

    private final static String SERVICE = "CollaborativeMenuService";

    private int     iHour,
                    iMinute,
                    fHour,
                    fMinute,
                    numDish;
    private Date    start,
                    finish;
    protected ArrayList<CollaborativeDishLayout> myDishes = new ArrayList<>();

    private CollaborativeMenuService collaborativeMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collaborativeMenuService = new CollaborativeMenuService(new CollaborativeMenuRepository(getApplicationContext()), new DishRepository(getApplicationContext()));
        collaborativeMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    initialize();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {
        iHour = iMinute = fHour = fMinute = 0;
        numDish = 0;
        updateTime(0, 0, 0, 0);

        final EditText menuName = (EditText) findViewById(R.id.nomMenu);
        menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuName.getText().toString().equals("MENÚ")) {
                    menuName.setText("");
                    menuName.setTextColor(getResources().getColor(R.color.black));
                } else if (menuName.getText().toString().equals("")) {
                    menuName.setText("MENÚ");
                    menuName.setTextColor(getResources().getColor(R.color.colorEdit));
                }
            }
        });

        final ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);
        final CollaborativeDishLayout menu = new CollaborativeDishLayout(getApplicationContext(), numDish);
        myDishes.add(menu);
        moreDishesLayout.addView(menu);
        ++numDish;
        menu.getClose().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDishesLayout.removeView(menu);
                myDishes.remove(menu);
            }
        });

        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollaborativeDishLayout a = new CollaborativeDishLayout(NewCollaborativeMenuActivity.this, ++numDish);
                myDishes.add(a);
                moreDishesLayout.addView(a);
                ImageButton close = a.getClose();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreDishesLayout.removeView(a);
                        myDishes.remove(a);
                    }
                });
            }
        });


        ImageButton timeTable = (ImageButton) findViewById(R.id.timetablebutton);
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTime();
            }
        });

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

        final ImageButton glutenFree = (ImageButton) findViewById(R.id.glutenfree);
        glutenFree.setColorFilter(R.color.black);
        glutenFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGlutenFree();
                if (glutenFree.getColorFilter().equals(R.color.green)) {
                    glutenFree.setColorFilter(R.color.colorAccent);
                } else {
                    glutenFree.setColorFilter(R.color.green);
                }
            }
        });
    }

    private void isGlutenFree() {
        // TODO El menu es glutenFree
    }

    private void isVegetarian() {
        // TODO El menu es vegetarian
    }

    private void updateTime(int iHour, int iMinut, int fHour, int fMinut) {
        TextView mDateDisplay = (TextView) findViewById(R.id.timeText);
        String iniH, iniMin, finH, finMin;
        if(iHour < 10) iniH = "0"+iHour;
        else iniH = iHour+"";
        if(iMinut < 10) iniMin = "0"+iMinut;
        else iniMin = iMinut+"";
        if(fHour < 10) finH = "0"+fHour;
        else finH = fHour+"";
        if(fMinut < 10) finMin = "0"+fMinut;
        else finMin = fMinut+"";

        mDateDisplay.setText(iniH + ":" + iniMin + "h - " + finH + ":" + finMin+"h");
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
                    iHour = hourOfDay; iMinute = minute;
                }
            };

    private TimePickerDialog.OnTimeSetListener finalTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    fHour = hourOfDay; fMinute = minute;
                    updateTime(iHour, iMinute, fHour, fMinute);
                }
            };

    private void showDialogTime() {
        showDialogFinalTime().show();
        showDialogInitialTime().show();

    }

    private TimePickerDialog showDialogInitialTime() {
        TimePickerDialog a = new TimePickerDialog(this, initialTimeSetListener, iHour, iMinute, false);
        TextView title = new TextView(this);
        title.setText("Hora Inicio");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setCustomTitle(title);
        return a;
    }

    private TimePickerDialog showDialogFinalTime() {
        TimePickerDialog a = new TimePickerDialog(this, finalTimeSetListener, fHour, fMinute, false);
        TextView title = new TextView(this);
        title.setText("Hora Fin");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setCustomTitle(title);
        return a;
    }

    private void createCollaborativeMenu() {

        EditText cityEditText = (EditText) findViewById(R.id.city);
        EditText adressEditText = (EditText) findViewById(R.id.adress);
        EditText descriptionEditText = (EditText) findViewById(R.id.description);
        EditText menuNameEditText = (EditText) findViewById(R.id.nomMenu);

        final String author = "Admin";
        String address = adressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        final String localization = address + ", " + city;


        String menuNameString = menuNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if(isIncomplete(address, city, menuNameString, description)){
            Toast.makeText(this, "Campos incompletos",
                    Toast.LENGTH_LONG).show();
        }
        else if(start.getTime()>=finish.getTime()){
            Toast.makeText(this, "Hora de inicio más pequeña o igual que hora final",
                    Toast.LENGTH_LONG).show();
        }
        else {

            List<Dish> offeredDish = new ArrayList<>();
            List<Dish> suggestedDish = new ArrayList<>();

            int n = 1;
            for (CollaborativeDishLayout dishLayout : myDishes) {
                if (!dishLayout.getDishName().equals("Plato " + n)) {
                    Dish dish = new Dish(dishLayout.getDishName());
                    if (!dishLayout.isSuggest()) offeredDish.add(dish);
                    else suggestedDish.add(dish);
                }
                n++;
            }

            CollaborativeMenu collaborativeMenu = new CollaborativeMenu(menuNameString,
                                                                        author,
                                                                        description,
                                                                        localization,
                                                                        start,
                                                                        finish);
            collaborativeMenuService.save(collaborativeMenu, offeredDish, suggestedDish);
            Toast.makeText(this, "Añadido correctamente",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isIncomplete(String address, String city, String menuNameString, String description) {
        return menuNameString.equals("") || address.equals("")
                || address.equals("") || address.equals("Tu dirección")
                || city.equals("") || city.equals("Tu ciudad")
                || description.equals("") || description.equals("descripción");
    }
}