package dev.blunch.blunch.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.view.CollaborativeDishLayout;

@SuppressWarnings("all")
public class NewCollaborativeMenuActivity extends AppCompatActivity {

    private final static String SERVICE = "CollaborativeMenuService";

    private int day,month,year;

    private int     iHour,
            iMinute,
            fHour,
            fMinute;
    private Date start,
            finish;
    protected ArrayList<CollaborativeDishLayout> myDishes = new ArrayList<>();
    protected ArrayList<CollaborativeDishLayout> suggestedDishes = new ArrayList<>();

    private CollaborativeMenuService collaborativeMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collaborative_menu_activity_reviewed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());
        setTitle("Crear menú colaborativo");
        initialize();

    }

    private void initialize() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);
        iHour = now.get(Calendar.HOUR_OF_DAY);
        iMinute = now.get(Calendar.MINUTE);
        fHour = now.get(Calendar.HOUR_OF_DAY);
        fMinute = now.get(Calendar.MINUTE);
        updateTime(0, 0, 0, 0, year, month, day);

        final EditText menuName = (EditText) findViewById(R.id.nomMenu);
        final ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final ImageButton moreSuggestedDishes = (ImageButton) findViewById(R.id.moreSuggestdDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);
        final LinearLayout moreSuggestedDishesLayout = (LinearLayout) findViewById(R.id.suggestedDishesLayout);
        /*final CollaborativeDishLayout menu = new CollaborativeDishLayout(getApplicationContext());
        myDishes.add(menu);
        moreDishesLayout.addView(menu);
        menu.getClose().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDishes.size() > 1) {
                    moreDishesLayout.removeView(menu);
                    myDishes.remove(menu);
                }
            }
        });*/

        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollaborativeDishLayout a = new CollaborativeDishLayout(NewCollaborativeMenuActivity.this);
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

        moreSuggestedDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollaborativeDishLayout a = new CollaborativeDishLayout(NewCollaborativeMenuActivity.this);
                suggestedDishes.add(a);
                moreSuggestedDishesLayout.addView(a);
                ImageButton close = a.getClose();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreSuggestedDishesLayout.removeView(a);
                        suggestedDishes.remove(a);
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

    }

    private void isGlutenFree() {
        // TODO El menu es glutenFree
    }

    private void isVegetarian() {
        // TODO El menu es vegetarian
    }

    private void updateTime(int iHour, int iMinut, int fHour, int fMinut, int year, int month, int day) {
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

        Calendar cStart = Calendar.getInstance();
        cStart.set(Calendar.HOUR_OF_DAY,iHour);
        cStart.set(Calendar.MINUTE, iMinut);
        cStart.set(Calendar.MONTH, month);
        cStart.set(Calendar.YEAR, year);
        cStart.set(Calendar.DAY_OF_MONTH, day);
        start = cStart.getTime();

        String dayS = String.valueOf(this.day);
        String monthS = String.valueOf(this.month + 1);
        String yearS = String.valueOf(this.year);
        if (this.day < 10) dayS = "0" + dayS;
        if (this.month < 10) monthS = "0" + monthS;

        mDateDisplay.setText("   " + dayS + "/" + monthS + "/" + yearS + "\n"
                + iniH + ":" + iniMin + "h - " + finH + ":" + finMin + "h");

        Calendar cFinish = Calendar.getInstance();
        cFinish.set(Calendar.HOUR_OF_DAY,fHour);
        cFinish.set(Calendar.MINUTE, fMinut);
        if (fHour < iHour) {
            cFinish.set(Calendar.MONTH, month);
            cFinish.set(Calendar.YEAR, year);
            cFinish.set(Calendar.DAY_OF_MONTH, day);
            cFinish.add(Calendar.DAY_OF_MONTH, 1);
        }
        else {
            cFinish.set(Calendar.MONTH, month);
            cFinish.set(Calendar.YEAR, year);
            cFinish.set(Calendar.DAY_OF_MONTH, day);
        }
        finish = cFinish.getTime();
    }

    private TimePickerDialog.OnTimeSetListener initialTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    iHour = hourOfDay; iMinute = minute;
                }
            };

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int yearOf, int monthOfYear, int dayOfMonth) {
                    year = yearOf;
                    month = monthOfYear;
                    day = dayOfMonth;
                }
            };

    private TimePickerDialog.OnTimeSetListener finalTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    fHour = hourOfDay; fMinute = minute;
                    updateTime(iHour, iMinute, fHour, fMinute, year, month, day);

                }
            };

    private void showDialogTime() {
        showDialogFinalTime().show();
        showDialogInitialTime().show();
        showDialogDate().show();
    }

    private DatePickerDialog showDialogDate() {
        DatePickerDialog a = new DatePickerDialog(this, dateSetListener, year, month, day);
        return a;
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
        title.setText("Hora Final");
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
        String address = adressEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        final String localization = address + ", " + city;


        String menuNameString = menuNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

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

            for (CollaborativeDishLayout dishLayout : myDishes) {
                if (!dishLayout.getDishName().equals("")) {
                    Dish dish = new Dish(dishLayout.getDishName());
                    offeredDish.add(dish);
                }
            }

            for (CollaborativeDishLayout dishLayout : suggestedDishes) {
                if (!dishLayout.getDishName().equals("")) {
                    Dish dish = new Dish(dishLayout.getDishName());
                    suggestedDish.add(dish);
                }
            }

            if (offeredDish.size() == 0) {
                Toast.makeText(getApplicationContext(), "Debes ofrecer al menos un plato!", Toast.LENGTH_SHORT).show();
            }

            else {
                CollaborativeMenu collaborativeMenu = new CollaborativeMenu(menuNameString,
                        Preferences.getCurrentUserEmail(),
                        description,
                        localization,
                        start,
                        finish);
                collaborativeMenuService.save(collaborativeMenu, offeredDish, suggestedDish);
                Toast.makeText(this, "Menú colaborativo creado correctamente!",
                        Toast.LENGTH_LONG).show();
                collaborativeMenuService.setActualDateToMenuChat(collaborativeMenu.getId());
                finish();
            }
        }
    }

    private boolean isIncomplete(String address, String city, String menuNameString, String description) {
        return menuNameString.length() > 0 || menuNameString.equals("MENÚ")
                || address.length() > 0 || address.equals("Tu dirección")
                || city.length() > 0 || city.equals("Tu ciudad")
                || description.length() > 0 || description.equals("descripción");
    }

}
