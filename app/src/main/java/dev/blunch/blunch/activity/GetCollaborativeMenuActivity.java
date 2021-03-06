package dev.blunch.blunch.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.DietTags;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.BaseActivity;

/**
 * GetMenuCollaborative Activity
 * @author albert
 */
@SuppressWarnings("all")
public class GetCollaborativeMenuActivity extends BaseActivity {

    public static final String MENU_ID_KEY = "menuId";
    private CollaborativeMenuService collaborativeMenuService;
    private MenuService menuService;
    private CollaborativeMenu collaborativeMenu;
    private List<Dish> suggestedDishes;
    private List<Dish> offeredDishes;
    private final String COMA = ",";
    private TextView userName, localization, hostDishes, suggestions, description, hour, valueCount;
    private ImageView userPic;
    private Button join;
    private RatingBar ratingBar;
    private Toolbar toolbar;

    private static String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_collaborative_menu);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getStringExtra(MENU_ID_KEY) != null) this.menuId = getIntent().getStringExtra(MENU_ID_KEY);

        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());
        menuService = ServiceFactory.getMenuService(getApplicationContext());

        collaborativeMenu = collaborativeMenuService.get(GetCollaborativeMenuActivity.this.menuId);
        suggestedDishes = collaborativeMenuService.getSuggestedDishes(collaborativeMenu.getId());
        offeredDishes = collaborativeMenuService.getOfferedDishes(collaborativeMenu.getId());
        initialize();
    }

    private void initialize() {
        userName = (TextView) findViewById(R.id.hostName_getCollaborative);
        localization = (TextView) findViewById(R.id.hostLocalization_getCollaborative);
        hostDishes = (TextView) findViewById(R.id.hostDishes_getCollaborative);
        suggestions = (TextView) findViewById(R.id.suggestions_getCollaborative);
        description = (TextView) findViewById(R.id.description_getCollaborative);
        hour = (TextView) findViewById(R.id.hour_getCollaborative);
        join = (Button) findViewById(R.id.join_getCollaborative);
        userPic = (ImageView) findViewById(R.id.user_icon);
        ratingBar = (RatingBar) findViewById(R.id.getValue);
        valueCount = (TextView) findViewById(R.id.valueCount);

        findViewById(R.id.valueLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCollaborativeMenuActivity.this, ValorationListActivity.class);
                intent.putExtra(ValorationListActivity.USER_ID, collaborativeMenu.getAuthor());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TextView messageCount = (TextView) findViewById(R.id.messagesCount);
        if(guest() || host()) {
            long count = menuService.getPendingMessagesCount(this.menuId);
            if (count > 0) {
                if (count < 100) messageCount.setText(String.valueOf(count));
                else messageCount.setText("+99");
            }
            else messageCount.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GetCollaborativeMenuActivity.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.MENU_ID, menuId);
                    startActivity(intent);
                }
            });
        }
        else{
            fab.setVisibility(View.GONE);
            messageCount.setVisibility(View.GONE);
        }
        userName.setText(obtainUserName());
        localization.setText(obtainAddress() + ", " + obtainCity());
        hostDishes.setText(obtainOfferedDishSingleString());
        if (!guest()){
            suggestions.setText(obtainSuggestedDishSingleString());}
        else {
            suggestions.setVisibility(View.GONE);
            LinearLayout sug = (LinearLayout) findViewById(R.id.SeSugiere);
            sug.setVisibility(View.GONE);
            TextView p = (TextView) findViewById(R.id.offeredTitle_getCollaborative);
            p.setText("Platos");

            Collection<Dish> m = collaborativeMenuService.getMySuggestedDishes(collaborativeMenu.getId());
            String sd = "";
            for (Dish d : m){
                sd += d.getName();
                if (d.getAuthor() != null || !d.getAuthor().isEmpty()) sd += " (" + menuService.findUserByEmail(d.getAuthor()).getName().split(" ")[0] + ")";
                sd += "\n";
            }
            hostDishes.setText(sd);
        }
        description.setText(obtainDescription());
        hour.setText(obtainHour());
        setRating();

        if(host()){
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GetCollaborativeMenuActivity.this, CollaborativePetitionsListActivity.class);
                    intent.putExtra("menuId", menuId);
                    startActivity(intent);
                }
            });
            join.setText("Peticiones");
        }
        else if(!guest()) {
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GetCollaborativeMenuActivity.this, CollaborativeMenuAnswerActivity.class);
                    intent.putExtra("menuId", menuId);
                    startActivity(intent);
                }
            });
        }
        else{
            join.setVisibility(View.GONE);
        }
        userPic.setImageDrawable(getUserPic());
        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCollaborativeMenuActivity.this, GetUserActivity.class);
                intent.putExtra(GetUserActivity.USER_ID, collaborativeMenu.getAuthor());
                startActivity(intent);
            }
        });
        //

        RelativeLayout vegetarianLayout = (RelativeLayout) findViewById(R.id.vegetarianLayout);
        RelativeLayout veganLayout = (RelativeLayout) findViewById(R.id.veganLayout);
        RelativeLayout glutenfreeLayout = (RelativeLayout) findViewById(R.id.glutenfreeLayout);

        boolean vegt, vegn, glutenfree;
        vegt = vegn = glutenfree = false;

        if (collaborativeMenu.retrievArrayOfTags() != null) {
            for (DietTags dietTags : collaborativeMenu.retrievArrayOfTags()) {
                switch(dietTags.toString()) {
                    case "VEGETARIAN":
                        vegt = true;
                        break;
                    case "VEGAN":
                        vegn = true;
                        break;
                    case "GLUTEN_FREE":
                        glutenfree = true;
                        break;
                    default:
                        break;
                }
            }
            if (!vegt) ((ViewManager)vegetarianLayout.getParent()).removeView(vegetarianLayout);
            if (!vegn) ((ViewManager)veganLayout.getParent()).removeView(veganLayout);
            if (!glutenfree) ((ViewManager)glutenfreeLayout.getParent()).removeView(glutenfreeLayout);
        } else  {
            ((ViewManager)vegetarianLayout.getParent()).removeView(vegetarianLayout);
            ((ViewManager)veganLayout.getParent()).removeView(veganLayout);
            ((ViewManager)glutenfreeLayout.getParent()).removeView(glutenfreeLayout);
        }

        toolbar.setTitle(obtainTitle());
        // TODO Set user image: toolbar.setLogo();
        setSupportActionBar(toolbar);
    }

    private void setRating() {
        User user = collaborativeMenuService.findUserByEmail(collaborativeMenu.getAuthor());
        ratingBar.setRating((float) user.getValorationAverage());
        Integer valueCount = user.getValorationNumber();
        if (valueCount == 1) this.valueCount.setText("(" + valueCount + " valoración)");
        else this.valueCount.setText("(" + valueCount + " valoraciones)");
    }

    private boolean host() { return collaborativeMenuService.imHost(collaborativeMenu.getId()); }

    private boolean guest() { return collaborativeMenuService.imGuest(collaborativeMenu.getId()); }

    private Drawable getUserPic() {
        try {
            return collaborativeMenuService.findUserByEmail(collaborativeMenu.getAuthor()).getImageRounded(getResources());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String obtainUserName() {
        return collaborativeMenuService.findUserByEmail(collaborativeMenu.getAuthor()).getName();
    }

    private String obtainTitle() {
        return collaborativeMenu.getName();
    }

    private String obtainAddress() {
        String[] parts = collaborativeMenu.getLocalization().split(COMA);
        String result = "";
        for (int i = 0; i < parts.length - 1; ++i) {
            result += parts[i];
        }
        return result;
    }

    private String obtainCity() {
        String[] parts = collaborativeMenu.getLocalization().split(COMA);
        return parts[parts.length - 1];
    }

    private List<String> obtainSuggestedDishNames() {
        List<String> list = new LinkedList<>();
        for (Dish dish : suggestedDishes) {
            list.add(dish.getName());
        }
        return list;
    }

    private String obtainSuggestedDishSingleString(){
        String sd = "";
        for (String s : obtainSuggestedDishNames()){
            sd += s + "\n";
        }
        if (obtainSuggestedDishNames().size() == 0) {
            sd = "No quedan platos sugeridos por el huésped";
            TextView textView = (TextView) findViewById(R.id.suggestions_getCollaborative);
            textView.setTypeface(null, Typeface.ITALIC);
        }
        return sd;
    }

    private List<String> obtainOfferedDishNames() {
        List<String> list = new LinkedList<>();
        for (Dish dish : offeredDishes) {
            String s = dish.getName();
            if (dish.getAuthor() != null || !dish.getAuthor().isEmpty()) s += " (" + menuService.findUserByEmail(dish.getAuthor()).getName().split(" ")[0] + ")";
            list.add(s);
        }
        return list;
    }

    private String obtainOfferedDishSingleString(){
        String od= "";
        for (String s : obtainOfferedDishNames()){
            od += s + "\n";
        }
        return od;
    }

    private String obtainDescription() {
        return "\"" + collaborativeMenu.getDescription() + "\"";
    }

    private String obtainHour() {
        String result = "";
        Integer hour, minute;
        Calendar calendar = Calendar.getInstance();
        Date dateStart = collaborativeMenu.getDateStart();
        Date dateEnd = collaborativeMenu.getDateEnd();
        calendar.setTime(dateStart);

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + day;
        if (calendar.get(Calendar.MONTH) < 10) month = "0" + month;

        result += " " + day + "/" + month + "/" + year + "\n";

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour + ":";
        if (minute < 10) result += "0";
        result += minute + " - ";
        calendar.setTime(dateEnd);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 10) result += "0";
        result += hour + ":";
        if (minute < 10) result += "0";
        result += minute;
        return result;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}