package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.repositories.PaymentMenuAnswerRepository;
import dev.blunch.blunch.repositories.PaymentMenuRepository;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.view.SelectPaymentDishLayout;

@SuppressWarnings("all")
public class GetPaymentMenuActivity extends AppCompatActivity {


    private PaymentMenuService paymentMenuService;
    private PaymentMenu paymentMenu;
    private PaymentMenuAnswer paymentMenuAnswer;
    private List<Dish> dishes;
    private final String COMA = ",";
    private final String FAKE_GUEST = "Platon";
    private TextView userName, localization, city, description, hour;
    private Button join;
    private Toolbar toolbar;
    private LinearLayout dishesLayout;
    private TextView precio;
    private ArrayList<SelectPaymentDishLayout> paymentDishesLayoutList;
    private List<Dish> answerDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        paymentMenuService = new PaymentMenuService(new PaymentMenuRepository(getApplicationContext()),
                                                    new DishRepository(getApplicationContext()),
                                                    new PaymentMenuAnswerRepository(getApplicationContext()));
        paymentMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    List<PaymentMenu> list = paymentMenuService.getAll();
                    paymentMenu = list.get(0);
                    dishes = paymentMenuService.getDishes(paymentMenu.getId());
                    initialize();
                }
            }
        });
    }

    private void initialize() {
        userName = (TextView) findViewById(R.id.hostName);
        localization = (TextView) findViewById(R.id.hostLocalization);
        city = (TextView) findViewById(R.id.hostCity);
        description = (TextView) findViewById(R.id.description);
        hour = (TextView) findViewById(R.id.hour);
        join = (Button) findViewById(R.id.join);
        dishesLayout = (LinearLayout) findViewById(R.id.checkboxDishesLayout);
        precio = (TextView) findViewById(R.id.precio);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "NO VAAAA", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        userName.setText(obtainUserName());
        localization.setText(obtainAddress());
        city.setText(obtainCity());
        description.setText(obtainDescription());
        hour.setText(obtainHour());
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMenuAnswer = new PaymentMenuAnswer(paymentMenu.getId(), FAKE_GUEST, Calendar.getInstance().getTime(), answerDishes);
                paymentMenuService.answer(paymentMenu.getId(), paymentMenuAnswer);

                Toast.makeText(v.getContext(), "OK", Toast.LENGTH_LONG).show();
                answerDishes = new ArrayList<Dish>();
            }
        });
        precio.setText("0 €");

        toolbar.setTitle(obtainTitle());
        // TODO Set user image: toolbar.setLogo();
        setSupportActionBar(toolbar);

        paymentDishesLayoutList = new ArrayList<>();
        answerDishes = new ArrayList<Dish>();

        for (final Dish d : dishes){
            SelectPaymentDishLayout n = new SelectPaymentDishLayout(getApplicationContext(), d.getName(), d.getPrice());
            dishesLayout.addView(n);
            paymentDishesLayoutList.add(n);
            n.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        String price = precio.getText().toString().split(" ")[0];
                        double a = Double.parseDouble(price);
                        a += d.getPrice();
                        precio.setText(String.valueOf(a) + " €");
                        answerDishes.add(d);
                    }
                    else {
                        String price = precio.getText().toString().split(" ")[0];
                        double a = Double.parseDouble(price);
                        a -= d.getPrice();
                        precio.setText(String.valueOf(a) + " €");
                        removeDish(d.getId());
                    }
                }
            });
        }
    }

    private void removeDish(String id) {
        for (Dish d: answerDishes){
            if (d.getId().equals(id))
                answerDishes.remove(d);
        }
    }

    private String obtainUserName() {
        return paymentMenu.getAuthor();
    }

    private String obtainTitle() {
        return paymentMenu.getName();
    }

    private String obtainAddress() {
        String[] parts = paymentMenu.getLocalization().split(COMA);
        String result = "";
        for (int i = 0; i < parts.length - 1; ++i) {
            result += parts[i];
        }
        return result;
    }

    private String obtainCity() {
        String[] parts = paymentMenu.getLocalization().split(COMA);
        return parts[parts.length - 1];
    }

    private String obtainDescription() {
        return paymentMenu.getDescription();
    }

    private String obtainHour() {
        String result = "";
        Integer hour, minute;
        Calendar calendar = Calendar.getInstance();
        Date dateStart = paymentMenu.getDateStart();
        Date dateEnd = paymentMenu.getDateEnd();
        calendar.setTime(dateStart);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour + ":";
        if (minute < 10) result += "0";
        result += minute + " - ";
        calendar.setTime(dateEnd);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour + ":";
        if (minute < 10) result += "0";
        result += minute;
        return result;
    }

}
