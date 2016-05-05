package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;

public class ValorationActivity extends AppCompatActivity {

    private RatingBar valoration;
    private EditText comment;
    private ImageButton done;

    public static final String USER_ID = "userId";
    public static final String MENU_ID = "menuId";

    private String idMenu;
    private String guest;

    private MenuService menuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoration);

        menuService = ServiceFactory.getMenuService(getApplicationContext());

        valoration = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.valorationComment);
        done = (ImageButton) findViewById(R.id.valorationDone);

        idMenu = getIntent().getStringExtra(MENU_ID);
        guest = getIntent().getStringExtra(USER_ID);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    private void click(){
        if(valoration.getRating() == 0){
            Snackbar.make(findViewById(R.id.valorationDone), "Puntúa para finalizar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            Menu m = menuService.getMenu(idMenu);
            menuService.value(idMenu, valoration.getRating(), comment.getText().toString(), m.getAuthor(), guest);
            Toast.makeText(this, "Valoración realizada",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
