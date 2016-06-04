package dev.blunch.blunch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.view.MenuRateRecyclerView;
import dev.blunch.blunch.view.MenuRecyclerView;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MenuService menuService;
    CollaborativeMenuService collaborativeMenuService;
    PaymentMenuService paymentMenuService;

    NavigationView navigationView;

    private String email;
    private double num;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        TextView oldMenus;
        oldMenus = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.old_menus));
        initializeCountDrawer(oldMenus);

        setUserInfo(navigationView);

        initFragment(R.layout.content_list_menus);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Qué tipo de menú quieres dar de alta?")
                        .setPositiveButton("Menú colaborativo", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MainActivity.this, NewCollaborativeMenuActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Menú de pago", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MainActivity.this, NewPaymentMenuActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        initializeSearchMenus();
    }

    private void initializeCountDrawer(TextView oldMenus) {

        oldMenus.setGravity(Gravity.CENTER_VERTICAL);
        oldMenus.setTypeface(null, Typeface.BOLD);
        oldMenus.setTextColor(getResources().getColor(R.color.colorAccent));
        int count = menuService.getNonValuedCollaboratedMenusOf(email).size();
        if (count > 0) oldMenus.setText(String.valueOf(count));
        else oldMenus.setText("");
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
            initializeSearchMenus();
        }
        else if (id == R.id.map) {
            Intent intent = new Intent(MainActivity.this,MenusLocationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.my_menus) {
            initFragment(R.layout.content_list_menus);
            initializeMyMenus();
        }
        else if (id == R.id.collaborating_menus) {
            initFragment(R.layout.content_list_menus);
            initializeCollaboratingMenus();
        }
        else if (id == R.id.collaborating_menus) {
            initFragment(R.layout.content_list_menus);
            initializeCollaboratingMenus();
        }
        else if (id == R.id.valoration_menus) {
            initFragment(R.layout.content_list_menus);
            initializeValoration();
        }
        else if (id == R.id.old_menus) {
            initFragment(R.layout.content_list_old_menus);
            initializeOldMenus();
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

        setTitle("Buscar menús");

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

    private void initVal(String filter) {
        List<dev.blunch.blunch.domain.Menu> menuList = new ArrayList<>();
        switch(filter){
            case "0":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "0.5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "1":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "1.5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "2":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "2.5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "3":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "3.5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "4":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "4.5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
            case "5":
                menuList.addAll(menuService.getMenusOrderedByValoration(num));
                break;
        }


        View recyclerView2 = findViewById(R.id.menu_list);
        assert recyclerView2 != null;
        MenuRecyclerView menuRecyclerView = new MenuRecyclerView(getApplicationContext(),
                menuList, menuService.getUsers());
        ((RecyclerView) recyclerView2).setAdapter(menuRecyclerView);

    }

    private void init(String filter) {

        List<dev.blunch.blunch.domain.Menu> menuList = new ArrayList<>();

        switch (filter) {
            case "Todos":
                menuList.addAll(menuService.getMenusOrderedByDate());
                break;
            case "Todos mis menús":
                menuList.addAll(menuService.getMyMenusOrderedByDate());
                break;
            case "Menús con mi participación":
                menuList.addAll(menuService.getPMenusOrderedByDate());
                break;
            case "Colaborativo":
                menuList.addAll(menuService.getCollaborativeMenusOrderedByDate());
                break;
            case "Mis menús antiguos":
                menuList.addAll(menuService.getMyOutMenusOrderedByDate());
                break;
            case "Participación en menús colaborativos":
                menuList.addAll(menuService.getPCollaborativeMenusOrderedByDate());
                break;
            case "De pago":
                menuList.addAll(menuService.getPaymentMenusOrderedByDate());
                break;
            case "Mis menús activos":
                menuList.addAll(menuService.getMyCurrentMenusOrderedByDate());
                break;
            case "Participación en menús de pago":
                menuList.addAll(menuService.getPPaymentMenusOrderedByDate());
                break;
            default:
                menuList.addAll(menuService.getMenusOrderedByDate());
                break;
        }

        View recyclerView2 = findViewById(R.id.menu_list);
        assert recyclerView2 != null;
        MenuRecyclerView menuRecyclerView = new MenuRecyclerView(getApplicationContext(),
                menuList, menuService.getUsers());
        ((RecyclerView) recyclerView2).setAdapter(menuRecyclerView);

    }

    private MenuRecyclerView setupRecyclerView(@NonNull RecyclerView recyclerView, List<dev.blunch.blunch.domain.Menu> menus) {
        MenuRecyclerView menuRecyclerView = new MenuRecyclerView(getApplicationContext(),
                menus, menuService.getUsers());
        recyclerView.setAdapter(menuRecyclerView);
        return menuRecyclerView;
    }

    private void initializeCollaboratingMenus() {
        setTitle("Menús en colaboración");
        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.collaborative_menu_types, R.layout.spinner_item);
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
                    init("Menús con mi participación");
                }
            }
        });
    }

    private void initializeOldMenus() {

        setTitle("Valoración de menus");

        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.old_menu_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initOldMenus(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        menuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    initOldMenus("No valorados");
                }
            }
        });
    }

    private void initOldMenus(String filter) {
        List<dev.blunch.blunch.domain.Menu> menuList = new ArrayList<>();

        switch (filter) {
            case "No valorados":
                menuList.addAll(menuService.getNonValuedCollaboratedMenusOf(email));
                break;
            case "Valorados":
                menuList.addAll(menuService.getValuedCollaboratedMenusOf(email));
                break;
            case "Todos los menús":
                menuList.addAll(menuService.getCaducatedCollaboratedMenusOf(email));
                break;
            default:
                menuList.addAll(menuService.getNonValuedCollaboratedMenusOf(email));
                break;
        }

        View recyclerView2 = findViewById(R.id.menu_list);
        assert recyclerView2 != null;
        MenuRateRecyclerView menuRecyclerView = new MenuRateRecyclerView(getApplicationContext(),
                menuList, menuService.getUsers(), menuService);
        ((RecyclerView) recyclerView2).setAdapter(menuRecyclerView);

    }

    private void initializeMyMenus() {
        setTitle("Mis menús");

        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_menu_types, R.layout.spinner_item);
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
                    init("Todos mis menús");
                }
            }
        });
    }


    private void initializeValoration() {
        setTitle("Menús filtrados por nota");

        Spinner spinner = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Valoration, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = parent.getItemAtPosition(position).toString();
                num = Double.parseDouble(val);
                initVal(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        menuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    initVal(String.valueOf(num));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getTitle().equals("Buscar menús")) init("Todos");
        else if (getTitle().equals("Valoración de menus")) initOldMenus("No valorados");
        TextView oldMenus;
        oldMenus = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.old_menus));
        initializeCountDrawer(oldMenus);
    }
}
