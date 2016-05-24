package dev.blunch.blunch.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;

public class GetUserActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName;
    private RatingBar userValoration;

    public static final String USER_ID = "userId";

    private MenuService menuService;

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

        User user = menuService.findUserByEmail(getIntent().getStringExtra(USER_ID));

        userImage.setImageDrawable(getUserPic());
        userName.setText(user.getName());
        userValoration.setRating((float) user.getValorationAverage());
    }

    private Drawable getUserPic() {
        try {
            return menuService.findUserByEmail(getIntent().getStringExtra(USER_ID)).getImageRounded(getResources());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
