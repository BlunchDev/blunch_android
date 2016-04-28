package dev.blunch.blunch.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Repository;

/**
 * GetMenuCollaborative Activity
 * @author albert
 */
public class GetCollaborativeMenuActivity extends AppCompatActivity {

    public static final String MENU_ID_KEY = "menuId";
    private CollaborativeMenuService collaborativeMenuService;
    private CollaborativeMenu collaborativeMenu;
    private List<Dish> suggestedDishes;
    private List<Dish> offeredDishes;
    private final String COMA = ",";
    private TextView userName, localization, city, hostDishes, suggestions, description, hour;
    private Button join;
    private Toolbar toolbar;

    private String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_collaborative_menu);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.menuId = getIntent().getStringExtra(MENU_ID_KEY);

        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());

        collaborativeMenu = collaborativeMenuService.get(GetCollaborativeMenuActivity.this.menuId);
        suggestedDishes = collaborativeMenuService.getSuggestedDishes(collaborativeMenu.getId());
        offeredDishes = collaborativeMenuService.getOfferedDishes(collaborativeMenu.getId());
        initialize();
    }

    @SuppressWarnings("all")
    private void initialize() {
        userName = (TextView) findViewById(R.id.hostName_getCollaborative);
        localization = (TextView) findViewById(R.id.hostLocalization_getCollaborative);
        hostDishes = (TextView) findViewById(R.id.hostDishes_getCollaborative);
        suggestions = (TextView) findViewById(R.id.suggestions_getCollaborative);
        description = (TextView) findViewById(R.id.description_getCollaborative);
        hour = (TextView) findViewById(R.id.hour_getCollaborative);
        join = (Button) findViewById(R.id.join_getCollaborative);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetCollaborativeMenuActivity.this, ProposalListActivity.class);
                intent.putExtra(MENU_ID_KEY, menuId);
                startActivity(intent);
            }
        });
        userName.setText(obtainUserName());
        localization.setText(obtainAddress() + ", " + obtainCity());
        hostDishes.setText(obtainOfferedDishSingleString());
        suggestions.setText(obtainSuggestedDishSingleString());
        description.setText(obtainDescription());
        hour.setText(obtainHour());
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCollaborativeMenuActivity.this, CollaborativeMenuAnswerActivity.class);
                intent.putExtra("menuId", menuId);
                startActivity(intent);
            }
        });
        //

        toolbar.setTitle(obtainTitle());
        // TODO Set user image: toolbar.setLogo();
        setSupportActionBar(toolbar);
    }

    private String obtainUserName() {
        return collaborativeMenu.getAuthor();
    }

    private String obtainTitle() {
        return collaborativeMenu.getName();
    }

    private String obtainAddress() {
        String[] parts = collaborativeMenu.getLocalization().split(COMA);
        String result = "";
        for (int i = 0; i < parts.length - 1; ++i) {
            result += parts[i];
        }
        return result;
    }

    private String obtainCity() {
        String[] parts = collaborativeMenu.getLocalization().split(COMA);
        return parts[parts.length - 1];
    }

    private List<String> obtainSuggestedDishNames() {
        List<String> list = new LinkedList<>();
        for (Dish dish : suggestedDishes) {
            list.add(dish.getName());
        }
        return list;
    }

    private String obtainSuggestedDishSingleString(){
        String sd = "";
        for (String s : obtainSuggestedDishNames()){
            sd += s + "\n";
        }
        if (obtainSuggestedDishNames().size() == 0) {
            sd = "No quedan platos sugeridos por el huésped";
            TextView textView = (TextView) findViewById(R.id.suggestions_getCollaborative);
            textView.setTypeface(null, Typeface.ITALIC);
        }
        return sd;
    }

    private List<String> obtainOfferedDishNames() {
        List<String> list = new LinkedList<>();
        for (Dish dish : offeredDishes) {
            list.add(dish.getName());
        }
        return list;
    }

    private String obtainOfferedDishSingleString(){
        String od= "";
        for (String s : obtainOfferedDishNames()){
            od += s + "\n";
        }
        return od;
    }

    private String obtainDescription() {
        return "\"" + collaborativeMenu.getDescription() + "\"";
    }

    private String obtainHour() {
        String result = "";
        Integer hour, minute;
        Calendar calendar = Calendar.getInstance();
        Date dateStart = collaborativeMenu.getDateStart();
        Date dateEnd = collaborativeMenu.getDateEnd();
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

}