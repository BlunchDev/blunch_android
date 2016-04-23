package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
                        init();
                }
            }
        });

        paymentMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    payReady = true;
                    if (payReady & colReady)
                        init();
                }
            }
        });

    }

    private void init() {

        List<Menu> menuList = new ArrayList<>();
        menuList.addAll(collaborativeMenuService.getAll());
        menuList.addAll(paymentMenuService.getAll());

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
