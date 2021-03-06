package dev.blunch.blunch.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.DietTags;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.BaseActivity;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Utils;
import dev.blunch.blunch.view.PaymentDishLayout;

@SuppressWarnings("all")
public class NewPaymentMenuActivity extends BaseActivity {

    private int day,month,year;

    private boolean vegetarianSelected, veganSelected, glutenfreeSelected;

    private int iHour,
            iMinute,
            fHour,
            fMinute;
    private Date start,
            finish;
    private List<ImageButton> idClose = new ArrayList<>();
    private EditText menuName;

    private PaymentMenuService paymentMenuService;

    protected ArrayList<PaymentDishLayout> myDishes = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment_menu_activity_reviewed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Crear menú de pago");
        paymentMenuService = ServiceFactory.getPaymentMenuService(getApplicationContext());
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

        menuName = (EditText) findViewById(R.id.nomMenu);

        final ImageButton moreDishes = (ImageButton) findViewById(R.id.moreDishes);
        final LinearLayout moreDishesLayout = (LinearLayout) findViewById(R.id.dishesLayout);

        moreDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PaymentDishLayout a = new PaymentDishLayout(NewPaymentMenuActivity.this);
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

        final ImageView vegetarian = (ImageView) findViewById(R.id.vegetarianIcon);
        final ImageView vegan = (ImageView) findViewById(R.id.veganIcon);
        final ImageView glutenfree = (ImageView) findViewById(R.id.glutenfreeIcon);

        final TextView vegetarianTag = (TextView) findViewById(R.id.vegetarianTag);
        final TextView veganTag = (TextView) findViewById(R.id.veganTag);
        final TextView glutenfreeTag = (TextView) findViewById(R.id.glutenfreeTag);

        final RelativeLayout vegetarianLayout = (RelativeLayout) findViewById(R.id.vegetarianLayout);
        final RelativeLayout veganLayout = (RelativeLayout) findViewById(R.id.veganLayout);
        final RelativeLayout glutenfreeLayout = (RelativeLayout) findViewById(R.id.glutenfreeLayout);

        vegetarianSelected = veganSelected = glutenfreeSelected = false;

        vegetarianLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vegetarianSelected) {
                    vegetarian.setImageDrawable(getResources().getDrawable(R.mipmap.test_vegetarian));
                    vegetarianTag.setTextColor(Color.RED);
                    vegetarianSelected = true;
                } else {
                    vegetarian.setImageDrawable(getResources().getDrawable(R.mipmap.test_vegetarian_unselect));
                    vegetarianTag.setTextColor(Color.GRAY);
                    vegetarianSelected = false;
                }
            }
        });
        veganLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!veganSelected) {
                    vegan.setImageDrawable(getResources().getDrawable(R.mipmap.test_vegan));
                    veganTag.setTextColor(Color.RED);
                    veganSelected = true;
                } else {
                    vegan.setImageDrawable(getResources().getDrawable(R.mipmap.test_vegan_unselect));
                    veganTag.setTextColor(Color.GRAY);
                    veganSelected = false;
                }
            }
        });
        glutenfreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!glutenfreeSelected) {
                    glutenfree.setImageDrawable(getResources().getDrawable(R.mipmap.test_glutenfree));
                    glutenfreeTag.setTextColor(Color.RED);
                    glutenfreeSelected = true;
                } else {
                    glutenfree.setImageDrawable(getResources().getDrawable(R.mipmap.test_glutenfree_unselect));
                    glutenfreeTag.setTextColor(Color.GRAY);
                    glutenfreeSelected = false;
                }
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
                createPaymentMenu();
            }
        });

    }

    private void isGlutenFree() {
        // TODO El menu es glutenfree
    }

    private void isVegetarian() {
        // TODO El menu es vegetaria
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
        title.setText("Hora Fin");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setCustomTitle(title);
        return a;
    }

    private void createPaymentMenu() {
        EditText cityEditText = (EditText) findViewById(R.id.city);
        EditText adressEditText = (EditText) findViewById(R.id.adress);
        EditText descriptionEditText = (EditText) findViewById(R.id.description);
        EditText menuNameEditText = (EditText) findViewById(R.id.nomMenu);

        String address = adressEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        final String localization = address + ", " + city;

        String menuNameString = menuNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        List<Dish> dishes = new ArrayList<>();

        boolean incorrectDishes = false;
        if (myDishes.isEmpty()) incorrectDishes = true;
        for (PaymentDishLayout p : myDishes) {
            if (p.getDishName().isEmpty() || p.getDishPrice().toString().isEmpty() || p.getDishPrice() == 0) incorrectDishes = true;
            else {
                Dish dish = new Dish(p.getDishName(), p.getDishPrice());
                dish.setAuthor(Preferences.getCurrentUserEmail());
                dishes.add(dish);
            }
        }

        LatLng posicion = Utils.getLocationFromAddress(localization,getApplicationContext());

        if (incorrectDishes){
            Toast.makeText(this, "Platos incorrectos",
                    Toast.LENGTH_LONG).show();
        }
        else if(menuNameString == null || menuNameString.isEmpty()){
            Toast.makeText(this, "Nombre de menú incorrecto",
                    Toast.LENGTH_LONG).show();
        }
        else if(description == null || description.isEmpty()){
            Toast.makeText(this, "Descripción de menú incorrecta",
                    Toast.LENGTH_LONG).show();
        }
        else if(posicion == null){
            Toast.makeText(this, "La dirección no es correcta",
                    Toast.LENGTH_LONG).show();
        }
        else if (start.before(new Date())){
            Toast.makeText(this, "Fecha de inicio anterior a fecha actual",
                    Toast.LENGTH_LONG).show();
        }
        else if (finish.before(new Date())){
            Toast.makeText(this, "Fecha de finalización anterior a fecha actual",
                    Toast.LENGTH_LONG).show();
        }
        else if(start.getTime()>=finish.getTime()){
            Toast.makeText(this, "Hora de inicio más pequeña o igual que hora final",
                    Toast.LENGTH_LONG).show();
        }
        else {

            PaymentMenu paymentMenu = new PaymentMenu(menuNameString,
                    Preferences.getCurrentUserEmail(),
                    description,
                    localization,
                    start,
                    finish);
            PaymentMenu saved = paymentMenuService.save(paymentMenu, dishes);
            paymentMenuService.resetMessageCountToActualUser(paymentMenu.getId());

            List<DietTags> dietTags = new ArrayList<>();
            if (vegetarianSelected) dietTags.add(DietTags.VEGETARIAN);
            if (veganSelected) dietTags.add(DietTags.VEGAN);
            if (glutenfreeSelected) dietTags.add(DietTags.GLUTEN_FREE);
            if (!dietTags.isEmpty()) paymentMenuService.addTags(saved, dietTags);

            Toast.makeText(this, "Menu de pago creado correctamente!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
