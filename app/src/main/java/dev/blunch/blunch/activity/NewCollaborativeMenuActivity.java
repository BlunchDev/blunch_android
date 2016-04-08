package dev.blunch.blunch.activity;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.os.Build;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.DishService;
import dev.blunch.blunch.view.CollaborativeDishLayout;

import static junit.framework.Assert.assertNotNull;

public class NewCollaborativeMenuActivity extends AppCompatActivity {

    private int iHour, iMinute, fHour, fMinute;
    private int numDish;
    private Date start, finish;
    protected ArrayList<CollaborativeDishLayout> myDishes = new ArrayList<>();
    private EditText menuName;

    private DishService dishService;
    private CollaborativeMenuService collaborativeMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dishService = new DishService(new DishRepository(getApplicationContext()));
        collaborativeMenuService = new CollaborativeMenuService(new CollaborativeMenuRepository(getApplicationContext()));
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {
        iHour = iMinute = fHour = fMinute = 0;
        numDish = 1;
        updateTime(0,0,0,0);

        menuName = (EditText) findViewById(R.id.nomMenu);
        menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuName.getText().toString().equals("MENÚ")) {
                    menuName.setText("");
                    menuName.setTextColor(getResources().getColor(R.color.black));
                }
                else if(menuName.getText().toString().equals("")){
                    menuName.setText("MENÚ");
                    menuName.setTextColor(getResources().getColor(R.color.colorEdit));
                }
            }
        });

        final ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);
        assertNotNull(moreDishes);
        assertNotNull(moreDishesLayout);
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

        ArrayList<TextView> dishes = new ArrayList<>();
        dishes.add((TextView) findViewById(R.id.dish1));

        ImageButton timeTable = (ImageButton) findViewById(R.id.timetablebutton);
        assertNotNull(timeTable);
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTime();
            }
        });

        Button publish = (Button) findViewById(R.id.publish);
        assertNotNull(publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollaborativeMenu();
            }
        });

        final ImageButton vegetarian = (ImageButton) findViewById(R.id.vegetarian);
        assertNotNull(vegetarian);
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
        assertNotNull(glutenFree);
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
        mDateDisplay.setText(iHour + ":" + iMinut + "h - " + fHour + ":" + fMinut+"h");
        /**if(iHour % 10 >= 1) {
            if(iMinute % 10 >=1){
                if(fHour % 10 >= 1){
                    if(fMinute % 10 >= 1){
                        mDateDisplay.setText(iHour + ":" + iMinute + "h - " + fHour + ":" + fMinute+"h");
                    }
                    else{
                        mDateDisplay.setText(iHour + ":" + iMinute + "h - " + fHour + ":0" + fMinute+"h");
                    }
                }
                else{
                    if(fMinute % 10 >= 1){
                        mDateDisplay.setText(iHour + ":" + iMinute + "h - 0" + fHour + ":" + fMinute+"h");
                    }
                    else{
                        mDateDisplay.setText(iHour + ":" + iMinute + "h - 0" + fHour + ":0" + fMinute+"h");
                    }
                }
            }
            else {
                if (fHour % 10 >= 1) {
                    if (fMinute % 10 >= 1) {
                        mDateDisplay.setText(iHour + ":0" + iMinute + "h - " + fHour + ":" + fMinute+"h");
                    }
                    else {
                        mDateDisplay.setText(iHour + ":0" + iMinute + "h - " + fHour + ":0" + fMinute+"h");
                    }
                } else {
                    if (fMinute % 10 >= 1) {
                        mDateDisplay.setText(iHour + ":0" + iMinute + "h - 0" + fHour + ":" + fMinute+"h");

                    } else {
                        mDateDisplay.setText(iHour + ":0" + iMinute + "h - 0" + fHour + ":0" + fMinute+"h");

                    }
                }
            }
        }
        else{
            if(iMinute % 10 >=1){
                if(fHour % 10 >= 1){
                    if(fMinute % 10 >= 1){
                        mDateDisplay.setText("0"+iHour + ":" + iMinute + "h - " + fHour + ":" + fMinute+"h");
                    }
                    else{
                        mDateDisplay.setText("0"+iHour + ":" + iMinute + "h - " + fHour + ":0" + fMinute+"h");
                    }
                }
                else{
                    if(fMinute % 10 >= 1){
                        mDateDisplay.setText("0"+iHour + ":" + iMinute + "h - 0" + fHour + ":" + fMinute+"h");
                    }
                    else{
                        mDateDisplay.setText("0"+iHour + ":" + iMinute + "h - 0" + fHour + ":0" + fMinute+"h");
                    }
                }
            }
            else {
                if (fHour % 10 >= 1) {
                    if (fMinute % 10 >= 1) {
                        mDateDisplay.setText("0"+iHour + ":0" + iMinute + "h - " + fHour + ":" + fMinute+"h");
                    } else {
                        mDateDisplay.setText("0"+iHour + ":0" + iMinute + "h - " + fHour + ":0" + fMinute+"h");
                    }
                } else {
                    if (fMinute % 10 >= 1) {
                        mDateDisplay.setText("0"+iHour + ":0" + iMinute + "h - 0" + fHour + ":" + fMinute+"h");
                    } else {
                        mDateDisplay.setText("0"+iHour + ":0" + iMinute + "h - 0" + fHour + ":0" + fMinute+"h");
                    }
                }
            }
        }*/
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCollaborativeMenu() {

        final String author = "Admin";
        EditText address = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        assertNotNull(address);
        assertNotNull(city);
        final String localization = address.getText().toString() + ", " + city.getText().toString();
        EditText description = (EditText) findViewById(R.id.description);
        assertNotNull(description);

        Set<String> offeredDishKeys = new LinkedHashSet<>();
        Set<String> suggestedDishKeys = new LinkedHashSet<>();

        EditText dish1 = (EditText) findViewById(R.id.dish1);
        assertNotNull(dish1);
        Switch who1 = (Switch) findViewById(R.id.switch1);
        assertNotNull(who1);

        if(menuName.getText().toString().equals("") || address.getText().toString().equals("")
                || address.getText().toString().equals("") || address.getText().toString().equals("Tu dirección")
                || city.getText().toString().equals("") || city.getText().toString().equals("Tu ciudad")
                || description.getText().toString().equals("") || description.getText().toString().equals("descripción")
                || dish1.getText().toString().equals("") || dish1.getText().toString().equals("Plato 1")){

            Toast.makeText(this, "Campos incompletos",
                    Toast.LENGTH_LONG).show();
        }
        else if(start.getTime()>=finish.getTime()){
            Toast.makeText(this, "Hora de inicio más pequeña o igual que hora final",
                    Toast.LENGTH_LONG).show();
        }
        else {

            Dish firstDish = new Dish(dish1.getText().toString(), 0.0);
            dishService.save(firstDish);
            if (!who1.getShowText()) {
                offeredDishKeys.add(firstDish.getId());
            } else {
                suggestedDishKeys.add(firstDish.getId());
            }

            int n = 2;
            for (CollaborativeDishLayout d : myDishes) {
                if (!d.getDishName().equals("Plato " + n)) {
                    Dish dish = new Dish(d.getDishName());
                    dishService.save(dish);
                    if (!d.getSuggerenciaName().toString().equals("Sugerencia")) {
                        offeredDishKeys.add(dish.getId());
                    } else {
                        suggestedDishKeys.add(dish.getId());
                    }
                }
                n++;
            }

            CollaborativeMenu collaborativeMenu = new CollaborativeMenu(
                    menuName.getText().toString(),
                    author,
                    description.getText().toString(),
                    localization,
                    start,
                    finish,
                    offeredDishKeys,
                    suggestedDishKeys);
            collaborativeMenuService.save(collaborativeMenu);
        }
    }
}
