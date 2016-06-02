package dev.blunch.blunch.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;

public class GetUserActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName;
    private RatingBar userValoration;
    private Button myMenus;
    private Button myValorations;

    public static final String USER_ID = "userId";

    public String userId;

    private MenuService menuService;

    @Override
    protected void onResume() {
        super.onResume();
        Preferences.init(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    void initialize(){
        userImage = (ImageView) findViewById(R.id.user_picture_get);
        userName = (TextView) findViewById(R.id.user_name_get);
        userValoration = (RatingBar) findViewById(R.id.user_rating_get);
        myMenus = (Button) findViewById(R.id.myMenus);
        myValorations = (Button) findViewById(R.id.myValorations);


        menuService = ServiceFactory.getMenuService(getApplicationContext());

        Intent intent = getIntent();
        if(intent.hasExtra(USER_ID)) {
            this.userId = intent.getStringExtra(USER_ID);
        }

        User user = menuService.findUserByEmail(this.userId);

        userImage.setImageDrawable(getUserPic());
        userName.setText(user.getName());
        setTitle(user.getName());
        userValoration.setRating((float) user.getValorationAverage());
        userValoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myValorations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetUserActivity.this, ValorationListActivity.class);
                intent.putExtra(ValorationListActivity.USER_ID, userId);
                startActivity(intent);
            }
        });
        myMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetUserActivity.this, ListMenusByUserActivity.class);
                intent.putExtra(ListMenusByUserActivity.USER_ID_KEY, userId);
                startActivity(intent);
            }
        });

    }

    private Drawable getUserPic() {
        try {
            return menuService.findUserByEmail(this.userId).getImageRounded(getResources());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
