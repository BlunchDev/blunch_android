package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.BaseActivity;

public class ValorationActivity extends BaseActivity {

    private RatingBar valoration;
    private EditText comment;

    public static final String USER_ID = "userId";
    public static final String MENU_ID = "menuId";

    private String idMenu;
    private String guest;

    private MenuService menuService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoration);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Valoracion");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        menuService = ServiceFactory.getMenuService(getApplicationContext());

        valoration = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.valorationComment);

        idMenu = getIntent().getStringExtra(MENU_ID);
        guest = getIntent().getStringExtra(USER_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.answer_menu) {
            try {
                click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void click() {
        if (valoration.getRating() == 0) {
            Snackbar.make(findViewById(R.id.coordinator_layout), "Puntúa para finalizar", Snackbar.LENGTH_LONG).show();
            return;
        }
        else {
            createMenu();
        }
    }

    private void createMenu() {
        Menu m = menuService.getMenu(idMenu);
        menuService.value(idMenu, valoration.getRating(), comment.getText().toString().trim(), m.getAuthor(), guest);
        Toast.makeText(this, "Valoración realizada", Toast.LENGTH_LONG).show();
        finish();
    }
}
