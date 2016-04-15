package dev.blunch.blunch.activity;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

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
import dev.blunch.blunch.repositories.PaymentMenuRepository;

public class paymentMenuActivity extends AppCompatActivity {


    private int iHour, iMinut, fHour;
    private int fMinut;
    private int numDish;
    private Date start, finish;
    private List<ImageButton> idClose = new ArrayList<>();
    private EditText menuName;

    private DishRepository dishRepository;
    private PaymentMenuRepository paymentMenuRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paymentmenu_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dishRepository = new DishRepository(getApplicationContext());
        paymentMenuRepository = new PaymentMenuRepository(getApplicationContext());
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
        EditText nameMenu = (EditText) findViewById(R.id.nomMenu);
        EditText address = (EditText) findViewById(R.id.adress);
        EditText city = (EditText) findViewById(R.id.city);
        final String localization = address.getText().toString() + ", " + city.getText().toString();
        EditText description = (EditText) findViewById(R.id.description);
        Set<String> DishKeys = new LinkedHashSet<>();
        EditText dish1 = (EditText) findViewById(R.id.dish1);
        EditText dish2 = (EditText) findViewById(R.id.dish2);
        EditText dish3 = (EditText) findViewById(R.id.dish3);
        EditText price1 = (EditText) findViewById(R.id.precio);
        price1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText price2 = (EditText) findViewById(R.id.precio2);
        price2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText price3 = (EditText) findViewById(R.id.precio3);
        price3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        String p1 =  price1.getText().toString();
        String p2 =  price2.getText().toString();
        String p3 =  price3.getText().toString();

        Dish firstDish = new Dish(dish1.getText().toString(), Double.parseDouble(p1));
        Dish secondDish = new Dish(dish2.getText().toString(), Double.parseDouble(p2));
        Dish thirdDish = new Dish(dish3.getText().toString(), Double.parseDouble(p3));

        dishRepository.insert(firstDish);
        dishRepository.insert(secondDish);
        dishRepository.insert(thirdDish);

        DishKeys.add(firstDish.getId());
        DishKeys.add(secondDish.getId());
        DishKeys.add(thirdDish.getId());

        PaymentMenu PaymentMenu = new PaymentMenu(    nameMenu.getText().toString(),
                author,
                description.getText().toString(),
                localization,
                start,
                finish,
                DishKeys);
        paymentMenuRepository.insert(PaymentMenu);
    }
}