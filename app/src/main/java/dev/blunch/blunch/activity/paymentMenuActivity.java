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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.DishService;
import dev.blunch.blunch.repositories.PaymentMenuRepository;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.view.PaymentDishLayout;

import static junit.framework.Assert.assertNotNull;

public class paymentMenuActivity extends AppCompatActivity {


    private int iHour, iMinut, fHour;
    private int fMinut;
    private int numDish;
    private Date start, finish;
    private List<ImageButton> idClose = new ArrayList<>();
    private EditText menuName;
    private DishService dishService;
    private PaymentMenuService paymentMenuService;

    protected ArrayList<PaymentDishLayout> myDishes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paymentmenu_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dishService = new DishService(new DishRepository(getApplicationContext()));
        paymentMenuService = new PaymentMenuService(new PaymentMenuRepository(getApplicationContext()));
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
        assertNotNull(moreDishes);
        assertNotNull(moreDishesLayout);
        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PaymentDishLayout a = new PaymentDishLayout(paymentMenuActivity.this, ++numDish);
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
                createPaymentMenu();
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
    private void createPaymentMenu() {
        final String author = "Admin";
        Set<String> DishKeys = new LinkedHashSet<>();
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);
        EditText dish1 = (EditText) findViewById(R.id.dish1);
        EditText address = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        EditText price = (EditText) findViewById(R.id.precio);
        final String localization = address.getText().toString() + ", " + city.getText().toString();
        EditText description = (EditText) findViewById(R.id.description);
        if(nameMenu.getText().toString().equals("")
                || price.getText().toString().equals("") || price.getText().toString().equals("Precio")
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
            double firstPrice = Double.parseDouble(price.getText().toString());
            Dish firstDish = new Dish(dish1.getText().toString(), firstPrice);
            dishService.save(firstDish);
            int n = 2;
            for (PaymentDishLayout d : myDishes) {
                if (!d.getDishName().equals("Plato " + n)) {
                    Dish dish = new Dish(d.getDishName());
                    dishService.save(dish);
                    DishKeys.add(dish.getId());
                }
                n++;
            }

            PaymentMenu PaymentMenu = new PaymentMenu(    nameMenu.getText().toString(),
                    author,
                    description.getText().toString(),
                    localization,
                    start,
                    finish,
                    DishKeys);
            paymentMenuService.save(PaymentMenu);
        }
    }
}