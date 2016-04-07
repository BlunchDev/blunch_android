package dev.blunch.blunch.activity;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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
    private EditText menuName;

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
        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollaborativeDishLayout a = new CollaborativeDishLayout(NewCollaborativeMenuActivity.this, ++numDish);
                myDishes.add(a);
                moreDishesLayout.addView(a);
                ImageButton close = (ImageButton) findViewById(a.getId());
                idClose.add(close);

            }
        });

        ArrayList<TextView> dishes = new ArrayList<>();
        dishes.add((TextView) findViewById(R.id.dish1));

        ImageButton timeTable = (ImageButton) findViewById(R.id.timetablebutton);
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTime();
            }
        });

        iHour = iMinut = fHour = fMinut = 0;

        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createCollaborativeMenu();
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCollaborativeMenu() {

        final String author = "Admin";
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);

        EditText address = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        final String localization = address.getText().toString() + ", " + city.getText().toString();

        EditText description = (EditText) findViewById(R.id.description);

        Set<String> offeredDishKeys = new LinkedHashSet<>();
        Set<String> suggestedDishKeys = new LinkedHashSet<>();

        EditText dish1 = (EditText) findViewById(R.id.dish1);
        Switch who1 = (Switch) findViewById(R.id.switch1);

        Dish firstDish = new Dish(dish1.getText().toString(), 0.0);
        dishRepository.insert(firstDish);
        if(!who1.getShowText()) {
            offeredDishKeys.add(firstDish.getId());
        }
        else{
            suggestedDishKeys.add(firstDish.getId());
        }

        int n = 2;
        for (CollaborativeDishLayout d : myDishes) {
            if(!d.getMenuName().equals("Plato "+n)) {
                Dish dish = new Dish(d.getMenuName(), 0.0);
                dishRepository.insert(dish);
                if(!d.getSuggerencia().toString().equals("Sugerencia")) {
                    offeredDishKeys.add(dish.getId());
                }
                else{
                    suggestedDishKeys.add(dish.getId());
                }
            }
            n++;
        }

        CollaborativeMenu collaborativeMenu = new CollaborativeMenu(    nameMenu.getText().toString(),
                                                                        author,
                                                                        description.getText().toString(),
                                                                        localization,
                                                                        start,
                                                                        finish,
                                                                        offeredDishKeys,
                                                                        suggestedDishKeys);
        collaborativeMenuRepository.insert(collaborativeMenu);
    }
}
