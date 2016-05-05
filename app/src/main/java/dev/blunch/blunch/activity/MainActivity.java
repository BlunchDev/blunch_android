package dev.blunch.blunch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.adapters.MenuListAdapter;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Repository;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MenuService menuService;
    CollaborativeMenuService collaborativeMenuService;
    PaymentMenuService paymentMenuService;

    private String email;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = Preferences.getCurrentUserEmail();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Buscar menús");

        menuService = ServiceFactory.getMenuService(getApplicationContext());
        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());
        paymentMenuService = ServiceFactory.getPaymentMenuService(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        setUserInfo(navigationView);

        initFragment(R.layout.content_list_menus);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenusLocationActivity.class);
                startActivity(intent);
            }
        });

        initializeSearchMenus();
    }

    private void setUserInfo(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        User user = menuService.findUserByEmail(email);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name_nav);

        try {
            userName.setText(user.getName());
            ImageView userPhoto = (ImageView) headerView.findViewById(R.id.user_picture_nav);
            userPhoto.setImageDrawable(user.getImageRounded(getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search_menus) {
            initFragment(R.layout.content_list_menus);
            Toast.makeText(getApplicationContext(), "Search menus", Toast.LENGTH_SHORT).show();
            initializeSearchMenus();
        }
        else if (id == R.id.my_menus) {
            initFragment(-1);
            Toast.makeText(getApplicationContext(), "My menus", Toast.LENGTH_SHORT).show();
            initializeMyMenus();
        }
        else if (id == R.id.collaborating_menus) {
            initFragment(-1);
            Toast.makeText(getApplicationContext(), "Collaborating menus", Toast.LENGTH_SHORT).show();
            initializeCollaboratingMenus();
        }
        else if (id == R.id.payment_menus) {
            initFragment(-1);
            Toast.makeText(getApplicationContext(), "Payed menus", Toast.LENGTH_SHORT).show();
            initializePaymentmenus();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFragment(int id) {
        LayoutInflater inflater = getLayoutInflater();
        View v;
        if (id != -1) v = inflater.inflate(id, null);
        else v = new View(getApplicationContext());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_frame_layout);
        frameLayout.removeAllViews();
        frameLayout.addView(v);
    }

    private void initializeSearchMenus() {

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

        menuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    init("Todos");
                }
            }
        });
    }

    private void init(String filter) {

        List<dev.blunch.blunch.domain.Menu> menuList = new ArrayList<>();

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
                menuList,
                menuService.getUsers());

        ListView listView = (ListView) findViewById(R.id.menu_list);
        listView.setAdapter(menuListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dev.blunch.blunch.domain.Menu menu = menuListAdapter.getItem(position);

                String s = menu.getClass().getSimpleName();

                switch (s) {
                    case "CollaborativeMenu":
                        Intent intent = new Intent(MainActivity.this, GetCollaborativeMenuActivity.class);
                        intent.putExtra(GetCollaborativeMenuActivity.MENU_ID_KEY, menu.getId());
                        startActivity(intent);
                        break;
                    case "PaymentMenu":
                        Intent intent2 = new Intent(MainActivity.this, GetPaymentMenuActivity.class);
                        intent2.putExtra(GetPaymentMenuActivity.MENU_ID_KEY, menu.getId());
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initializeCollaboratingMenus() {
        //TODO fill view
        setTitle("Menús en colaboración");
    }

    private void initializePaymentmenus() {
        //TODO fill view
        setTitle("Menús comprados");
    }

    private void initializeMyMenus() {
        //TODO fill view
        setTitle("Mis menús");
    }

    @Override
    protected void onResume() {
        super.onResume();
        init("Todos");
    }
}
