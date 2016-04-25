package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.adapters.MenuListAdapter;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.repositories.PaymentMenuRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.utils.Repository;

public class ListMenusActivity extends AppCompatActivity {

    CollaborativeMenuService collaborativeMenuService;
    PaymentMenuService paymentMenuService;
    boolean colReady;
    boolean payReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colReady = false;
        payReady = false;
        setContentView(R.layout.activity_list_menus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ofertas de menú");

        DishRepository dishRepository = new DishRepository(getApplicationContext());

        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                init(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        collaborativeMenuService = new CollaborativeMenuService(
                new CollaborativeMenuRepository(getApplicationContext()),
                dishRepository,
                new CollaborativeMenuAnswerRepository(getApplicationContext()));

        paymentMenuService = new PaymentMenuService(
                new PaymentMenuRepository(getApplicationContext()),
                dishRepository
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        collaborativeMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    colReady = true;
                    if (payReady & colReady)
                        init("All");
                }
            }
        });

        paymentMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    payReady = true;
                    if (payReady & colReady)
                        init("All");
                }
            }
        });

    }

    private void init(String filter) {

        List<Menu> menuList = new ArrayList<>();

        switch (filter) {
            case "Todos":
                menuList.addAll(collaborativeMenuService.getAll());
                menuList.addAll(paymentMenuService.getAll());
                break;
            case "Colaborativo":
                menuList.addAll(collaborativeMenuService.getAll());
                break;
            case "De pago":
                menuList.addAll(paymentMenuService.getAll());
                break;
            default:
                menuList.addAll(collaborativeMenuService.getAll());
                menuList.addAll(paymentMenuService.getAll());
                break;
        }

        final MenuListAdapter menuListAdapter = new MenuListAdapter(
                getApplicationContext(),
                menuList);

        ListView listView = (ListView) findViewById(R.id.menu_list);
        listView.setAdapter(menuListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu menu = menuListAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),menu.getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
