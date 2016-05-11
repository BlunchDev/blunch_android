package dev.blunch.blunch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;

/**
 * Valoration List Activity Class
 * @author albert
 */
@SuppressWarnings("all")
public class ValorationListActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";
    private MenuService service;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_petitions_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        userId = null;
        if (intent.hasExtra(USER_ID)) {
            userId = intent.getStringExtra(USER_ID);
        }

        service = ServiceFactory.getMenuService(getApplicationContext());

        User user = service.findUserByEmail(userId);
        setTitle("Valoraciones de " + user.getName());

        View recyclerView2 = findViewById(R.id.valoration_list);
        setupRecyclerView((RecyclerView) recyclerView2, userId);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String id) {

    }

}
