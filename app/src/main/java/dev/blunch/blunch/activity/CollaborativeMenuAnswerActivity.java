package dev.blunch.blunch.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.BaseActivity;
import dev.blunch.blunch.utils.Preferences;

public class CollaborativeMenuAnswerActivity extends BaseActivity {

    private CollaborativeMenuService collaborativeMenuService;

    private List<Dish> hostSuggestions;
    private List<Dish> guestSuggestions;
    private List<String> guestNewSuggestions;

    private String menuID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_menu_answer);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Suggerencias");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        guestSuggestions = new ArrayList<>();
        guestNewSuggestions = new ArrayList<>();
        collaborativeMenuService = ServiceFactory.getCollaborativeMenuService(getApplicationContext());

        menuID = getIntent().getStringExtra("menuId");

        CollaborativeMenu collaborativeMenu = collaborativeMenuService.get(menuID);
        hostSuggestions = collaborativeMenuService.getSuggestedDishes(collaborativeMenu.getId());
        fillHostSuggestions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.answer_menu) {
            try {

                List<Dish> newSuggestions = new ArrayList<>();

                for (String s : guestNewSuggestions) {
                    Dish dish = new Dish(s, 0.);
                    dish.setAuthor(Preferences.getCurrentUserEmail());
                    newSuggestions.add(dish);
                }

                collaborativeMenuService.reply(new CollaborativeMenuAnswer(
                        Preferences.getCurrentUserEmail(), menuID,
                        Calendar.getInstance().getTime(), guestSuggestions), newSuggestions);

                Toast.makeText(getApplicationContext(),"Respuesta añadida", Toast.LENGTH_SHORT).show();

                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void fillHostSuggestions(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        if(linearLayout != null) {
            linearLayout.removeAllViews();
            for (int i = 0; i < hostSuggestions.size(); ++i) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(hostSuggestions.get(i).getName());
                checkBox.setTextColor(Color.GRAY);
                int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                checkBox.setButtonDrawable(id);
                checkBox.setHighlightColor(getResources().getColor(R.color.colorPrimary));
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox) v;
                        String s = ((CheckBox) v).getText().toString();
                        Dish dish = null;
                        for (Dish d : hostSuggestions) {
                            if (s.equals(d.getName())) dish = d;
                        }
                        if (checkBox.isChecked()) {
                            guestSuggestions.add(dish);
                        } else {
                            guestSuggestions.remove(dish);
                        }
                    }
                });

                linearLayout.addView(checkBox);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(60,0,0,0 );
                linearLayout.setLayoutParams(params);
            }
        }
    }

    public void addSuggestion(View view) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);
        linearLayout.setHorizontalGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        EditText editText = (EditText) findViewById(R.id.CollaborativeMenuAnswerEt);
        if (editText.getText().toString().trim().length() > 0 && linearLayout != null) {
            String suggestion = editText.getText().toString().trim();
            boolean exists = false;
            for (Dish d : guestSuggestions) {
                if (suggestion.equals(d.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                for (Dish d : hostSuggestions) {
                    if (suggestion.equals(d.getName())) {
                        exists = true;
                        break;
                    }
                }
            }
            if (exists || guestNewSuggestions.contains(suggestion)) {
                Snackbar.make(view, "El plato ya existe en este menú", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (!suggestion.equals("")) {

                //Creating text view containig proposal
                TextView textView = new TextView(this);
                textView.setTextSize(20);
                textView.setText(suggestion);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.8);
                p.setMargins(0, 12, 0, 0);
                textView.setLayoutParams(p);

                Space s = new Space(this);
                s.setLayoutParams(new ViewGroup.LayoutParams(10, LinearLayout.LayoutParams.WRAP_CONTENT));

                //Creating image button to remove layout with proposal
                ImageButton button = new ImageButton(getApplicationContext());
                button.setImageResource(R.drawable.close);
                button.setBackgroundColor(1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup parent = (ViewGroup) v.getParent();

                        String dish = ((TextView) parent.getChildAt(0)).getText().toString();

                        guestNewSuggestions.remove(dish);

                        ViewGroup linearLayout = (ViewGroup) parent.getParent();
                        linearLayout.removeView(parent);
                    }
                });
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                //Creating layout which contains textview and button
                LinearLayout layout_in = new LinearLayout(getApplicationContext());
                layout_in.addView(textView);
                layout_in.addView(s);
                layout_in.addView(button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 0, 0, 0);
                layout_in.setLayoutParams(params);

                //Adding the layout created before to the dynamic layout
                linearLayout.addView(layout_in);

                guestNewSuggestions.add(suggestion);

                //After adding suggestion we clear the editText for new suggestions
                editText.getText().clear();
            }
        }
    }


        //TESTING PURPOSES
        public List<String> getGuestNewSuggestions () {
            return guestNewSuggestions;
        }

}
