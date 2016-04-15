package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.Repository;

/**
 * GetMenuCollaborative Activity
 * @author albert
 */
public class GetMenuCollaborativeActivity extends AppCompatActivity {

    private CollaborativeMenuService collaborativeMenuService;
    private CollaborativeMenu collaborativeMenu;
    private List<Dish> suggestedDishes;
    private List<Dish> offeredDishes;
    private final String COMA = ",";
    private TextView userName, localization, city, menuName, hostDishes, suggestions;
    private Button contact, join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_collaborative_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collaborativeMenuService = new CollaborativeMenuService(new CollaborativeMenuRepository(getApplicationContext()), new DishRepository(getApplicationContext()));
        collaborativeMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                List<CollaborativeMenu> list = collaborativeMenuService.getAll();
                collaborativeMenu = list.get(0);
                    suggestedDishes = collaborativeMenuService.getSuggestedDishes(collaborativeMenu.getId());
                    offeredDishes = collaborativeMenuService.getOfferedDishes(collaborativeMenu.getId());
                    initialize();
                }
        });
    }

    @SuppressWarnings("all")
    private void initialize() {
        userName = (TextView) findViewById(R.id.hostName);
        localization = (TextView) findViewById(R.id.hostLocalization);
        city = (TextView) findViewById(R.id.hostCity);
        menuName = (TextView) findViewById(R.id.menuName);
        hostDishes = (TextView) findViewById(R.id.hostDishes);
        suggestions = (TextView) findViewById(R.id.suggestions);

        contact = (Button) findViewById(R.id.contact);
        join = (Button) findViewById(R.id.join);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        userName.setText(obtainUserName());
        localization.setText(obtainAddress());
        city.setText(obtainCity());
        menuName.setText(obtainTitle());
        hostDishes.setText(obtainOfferedDishSingleString());
        suggestions.setText(obtainSuggestedDishSingleString());

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Aun no va pesaos",Toast.LENGTH_LONG).show();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "que he dicho que no!",Toast.LENGTH_LONG).show();
            }
        });

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
            sd = sd + "\n" + s;
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
            od = od + "\n" + s;
        }
        return od;
    }

}
