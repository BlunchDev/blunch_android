package dev.blunch.blunch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.adapters.MenuListAdapter;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Repository;

@SuppressWarnings("all")
public class ListMenusActivity extends AppCompatActivity {

    MenuService menuService;
    CollaborativeMenuService collaborativeMenuService;
    PaymentMenuService paymentMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BLUNCH");

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

        menuService = ServiceFactory.getMenuService(getApplicationContext());
        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());
        paymentMenuService = ServiceFactory.getPaymentMenuService(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListMenusActivity.this);
                builder.setMessage("Qué tipo de menú quieres dar de alta?")
                        .setCancelable(false)
                        .setPositiveButton("Menú colaborativo", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ListMenusActivity.this, NewCollaborativeMenuActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Menú de pago", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ListMenusActivity.this, NewPaymentMenuActivityReviewed.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        menuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    init("All");
                }
            }
        });
    }

    private void init(String filter) {

        List<Menu> menuList = new ArrayList<>();

        switch (filter) {
            case "Todos":
                menuList.addAll(menuService.getMenusOrderedByDate());
                break;
            case "Colaborativo":
                menuList.addAll(menuService.getCollaborativeMenusOrderedByDate());
                break;
            case "De pago":
                menuList.addAll(menuService.getPaymentMenusOrderedByDate());
                break;
            default:
                menuList.addAll(menuService.getMenusOrderedByDate());
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

                String s = menu.getClass().getSimpleName();

                switch (s) {
                    case "CollaborativeMenu":
                        Intent intent = new Intent(ListMenusActivity.this, GetCollaborativeMenuActivity.class);
                        intent.putExtra(GetCollaborativeMenuActivity.MENU_ID_KEY, menu.getId());
                        startActivity(intent);
                        break;
                    case "PaymentMenu":
                        Intent intent2 = new Intent(ListMenusActivity.this, GetPaymentMenuActivity.class);
                        intent2.putExtra(GetPaymentMenuActivity.MENU_ID_KEY, menu.getId());
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });


    }

}
