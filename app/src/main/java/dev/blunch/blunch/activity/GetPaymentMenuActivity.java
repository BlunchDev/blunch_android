package dev.blunch.blunch.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.view.GuestPaymentDishLayout;
import dev.blunch.blunch.view.HostPaymentDishLayout;
import dev.blunch.blunch.view.PaymentDishLayout;
import dev.blunch.blunch.view.SelectPaymentDishLayout;

@SuppressWarnings("all")
public class GetPaymentMenuActivity extends AppCompatActivity {
    public static final String MENU_ID_KEY = "menuId";
    private PaymentMenuService paymentMenuService;
    private MenuService menuService;
    private PaymentMenu paymentMenu;
    private PaymentMenuAnswer paymentMenuAnswer;
    private List<Dish> dishes;
    private final String COMA = ",";
    private final String FAKE_GUEST = "Platon";
    private TextView userName, localization, city, description, hour;
    private ImageView userPic;
    private Button join;
    private Toolbar toolbar;
    private LinearLayout dishesLayout;
    private TextView precio, valueCount;
    private RatingBar ratingBar;
    private ArrayList<SelectPaymentDishLayout> paymentDishesLayoutList;
    private List<Dish> answerDishes;

    private static String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment_menu);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getStringExtra(MENU_ID_KEY) != null) this.menuId = getIntent().getStringExtra(MENU_ID_KEY);

        paymentMenuService = ServiceFactory.getPaymentMenuService(getApplicationContext());
        menuService = ServiceFactory.getMenuService(getApplicationContext());
        paymentMenu = paymentMenuService.get(menuId);
        dishes = paymentMenuService.getDishes(paymentMenu.getId());
        initialize();
    }

    private void initialize() {
        userName = (TextView) findViewById(R.id.hostName);
        localization = (TextView) findViewById(R.id.hostLocalization);
        description = (TextView) findViewById(R.id.description);
        hour = (TextView) findViewById(R.id.hour);
        join = (Button) findViewById(R.id.join);
        dishesLayout = (LinearLayout) findViewById(R.id.checkboxDishesLayout);
        dishesLayout.removeAllViews();
        precio = (TextView) findViewById(R.id.precio);
        userPic = (ImageView) findViewById(R.id.user_icon);
        ratingBar = (RatingBar) findViewById(R.id.getValue);
        valueCount = (TextView) findViewById(R.id.valueCount);

        findViewById(R.id.valueLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetPaymentMenuActivity.this, ValorationListActivity.class);
                intent.putExtra(ValorationListActivity.USER_ID, paymentMenu.getAuthor());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TextView messageCount = (TextView) findViewById(R.id.messagesCount);

        if(guest() || host()) {
            int count = menuService.getPendingMessagesCount(this.menuId);
            if (count > 0) {
                if (count < 100) messageCount.setText(String.valueOf(count));
                else messageCount.setText("+99");
            }
            else messageCount.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GetPaymentMenuActivity.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.MENU_ID, menuId);
                    startActivity(intent);
                }
            });
        }
        else{
            fab.setVisibility(View.GONE);
        }

        userPic.setImageDrawable(obtainUserPic());
        userName.setText(obtainUserName());
        localization.setText(obtainAddress() + ", " + obtainCity());
        description.setText(obtainDescription());
        hour.setText(obtainHour());
        setRating();

        if(host()){
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GetPaymentMenuActivity.this, PaymentPetitionsListActivity.class);
                    intent.putExtra(PaymentPetitionsListActivity.ID_PAYMENT_MENU_KEY, menuId);
                    startActivity(intent);
                }
            });
            join.setText("Peticiones");
            //dishesLayout.setVisibility(View.GONE);
        }
        else if(!guest()) {
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answerDishes.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Debes seleccionar al menos un plato para realizar el pedido", Toast.LENGTH_SHORT).show();
                    } else {
                        paymentMenuAnswer = new PaymentMenuAnswer(paymentMenu.getId(), Preferences.getCurrentUserEmail(),
                                Calendar.getInstance().getTime(), answerDishes);
                        paymentMenuService.answer(paymentMenu.getId(), paymentMenuAnswer);

                        Toast.makeText(v.getContext(), "Menú solicitado correctamente!", Toast.LENGTH_LONG).show();
                        answerDishes = new ArrayList<Dish>();
                        onRestart();
                    }
                }
            });
        }
        else{
            join.setVisibility(View.GONE);
        }

        if(!host()){
            precio.setText("0 €");
        }
        else{
            TextView pt = (TextView) findViewById(R.id.precioTotal);
            pt.setVisibility(View.GONE);
        }

        toolbar.setTitle(obtainTitle());
        // TODO Set user image: toolbar.setLogo();
        setSupportActionBar(toolbar);

        paymentDishesLayoutList = new ArrayList<>();
        answerDishes = new ArrayList<Dish>();
        if(!host() && !guest()) {
            for (final Dish d : dishes) {
                SelectPaymentDishLayout n = new SelectPaymentDishLayout(getApplicationContext(), d.getName(), d.getPrice());
                dishesLayout.addView(n);
                paymentDishesLayoutList.add(n);
                n.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            String price = precio.getText().toString().split(" ")[0];
                            double a = Double.parseDouble(price);
                            a += d.getPrice();
                            precio.setText(String.valueOf(a) + " €");
                            answerDishes.add(d);
                        } else {
                            String price = precio.getText().toString().split(" ")[0];
                            double a = Double.parseDouble(price);
                            a -= d.getPrice();
                            precio.setText(String.valueOf(a) + " €");
                            //removeDish(d.getId());
                            answerDishes.remove(d);
                        }
                    }
                });
            }
        }
        else if(host()){
            for (final Dish d : dishes) {
                HostPaymentDishLayout n = new HostPaymentDishLayout(getApplicationContext(), d.getName(), d.getPrice());
                dishesLayout.addView(n);
                precio.setVisibility(View.GONE);
            }
        }
        else if (guest()){
            Double myTotalPrice = 0.0;
            Collection<Dish> m = paymentMenuService.getMySelectedDishes(paymentMenu.getId());
            for (Dish d :  m) {
                GuestPaymentDishLayout n = new GuestPaymentDishLayout(getApplicationContext(), d.getName(), d.getPrice());
                dishesLayout.addView(n);
                myTotalPrice+=d.getPrice();
            }
            precio.setText(myTotalPrice+" €");
        }
    }

    private void setRating() {
        User user = paymentMenuService.findUserByEmail(paymentMenu.getAuthor());
        ratingBar.setRating( (float) user.getValorationAverage() );
        Integer valueCount = user.getValorationNumber();
        if (valueCount == 1) this.valueCount.setText("(" + valueCount + " valoración)");
        else this.valueCount.setText("(" + valueCount + " valoraciones)");
    }

    private boolean host() {
        return paymentMenuService.imHost(paymentMenu.getId());
    }

    private boolean guest() {
        return paymentMenuService.imGuest(paymentMenu.getId());
    }

    private Drawable obtainUserPic() {
        try {
            return paymentMenuService.findUserByEmail(paymentMenu.getAuthor()).getImageRounded(getResources());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void removeDish(String id) {
        for (Dish d: answerDishes){
            if (d.getId().equals(id))
                answerDishes.remove(d);
        }
    }

    private String obtainUserName() {
        return paymentMenuService.findUserByEmail(paymentMenu.getAuthor()).getName();
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

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + day;
        if (calendar.get(Calendar.MONTH) < 10) month = "0" + month;

        result += " " + day + "/" + month + "/" + year + "\n";

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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}