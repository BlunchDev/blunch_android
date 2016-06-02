package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.view.MenuRecyclerView;

public class ListMenusByUserActivity extends AppCompatActivity {

    MenuService menuService;
    public static final String USER_ID_KEY = "userId";
    private static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_menus);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        if (getIntent().getStringExtra(USER_ID_KEY) != null) this.userId = getIntent().getStringExtra(USER_ID_KEY);

        menuService = ServiceFactory.getMenuService(getApplicationContext());

        setTitle("Menús de " + menuService.findUserByEmail(this.userId).getName());

        init("Todos");
        setAdapter();
    }

    private void setAdapter (){
        List<Menu> menuList = new ArrayList<>();
        //TODO menuService.getCurrentMenusByUser(userId)
        menuList = menuService.getMenusByUser(userId);

        View recyclerView2 = findViewById(R.id.menu_list);
        assert recyclerView2 != null;
        MenuRecyclerView menuRecyclerView = new MenuRecyclerView(getApplicationContext(),
                menuList, menuService.getUsers());
        ((RecyclerView) recyclerView2).setAdapter(menuRecyclerView);

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
    }


    private void init(String filter) {

        List<dev.blunch.blunch.domain.Menu> menuList = new ArrayList<>();

        switch (filter) {
            case "Todos":
                menuList.addAll(menuService.getMenusByUser(userId));
                break;
            case "Colaborativo":
                menuList.addAll(menuService.getCollaborativeMenusByUser(userId));
                break;
            case "De pago":
                menuList.addAll(menuService.getPaymentMenusByUser(userId));
                break;
            default:
                menuList.addAll(menuService.getMenusByUser(userId));
                break;
        }

        View recyclerView2 = findViewById(R.id.menu_list);
        assert recyclerView2 != null;
        MenuRecyclerView menuRecyclerView = new MenuRecyclerView(getApplicationContext(),
                menuList, menuService.getUsers());
        ((RecyclerView) recyclerView2).setAdapter(menuRecyclerView);

    }

}
