package dev.blunch.blunch.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.Repository;

public class CollaborativeMenuAnswerActivity extends AppCompatActivity {

    private CollaborativeMenuService collaborativeMenuService;

    private List<Dish> hostSuggestions;
    private List<Dish> guestSuggestions;
    private List<String> guestNewSuggestions;

    final private String FAKE_GUEST = "Friedrich Nietzsche";

    private String menuID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_menu_answer);
        guestSuggestions = new ArrayList<>();
        guestNewSuggestions = new ArrayList<>();
        collaborativeMenuService = new CollaborativeMenuService(
                new CollaborativeMenuRepository(getApplicationContext()),
                new DishRepository(getApplicationContext()),
                new CollaborativeMenuAnswerRepository(getApplicationContext()));

        menuID = getIntent().getStringExtra("menuId");

        collaborativeMenuService.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                if (type.equals(EventType.Full)) {
                    CollaborativeMenu collaborativeMenu = collaborativeMenuService.get(menuID);
                    hostSuggestions = collaborativeMenuService.getSuggestedDishes(collaborativeMenu.getId());
                    makeProposalCreation();
                    fillHostSuggestions();
                }
            }
        });
    }

    private void makeProposalCreation() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.CollaborativeMenuAnswerBtnEnd);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        List<Dish> newSuggestions = new ArrayList<>();

                        for (String s : guestNewSuggestions) newSuggestions.add(new Dish(s, 0.));

                        collaborativeMenuService.reply(new CollaborativeMenuAnswer(FAKE_GUEST, menuID,
                                Calendar.getInstance().getTime(), guestSuggestions), newSuggestions);

                        Snackbar.make(view, "Created answer menu", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void fillHostSuggestions(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        linearLayout.removeAllViews();
        if(linearLayout != null) {
            for (int i = 0; i < hostSuggestions.size(); ++i) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(hostSuggestions.get(i).getName());
                checkBox.setTextColor(Color.GRAY);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox) v;
                        String s = ((CheckBox) v).getText().toString();
                        Dish dish = null;
                        for (Dish d : hostSuggestions) {
                            if (s == d.getName()) dish = d;
                        }
                        if (checkBox.isChecked()) {
                            guestSuggestions.add(dish);
                        } else {
                            guestSuggestions.remove(dish);
                        }
                    }
                });

                linearLayout.addView(checkBox);
            }
        }
    }

    public void addSuggestion(View view){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);
        EditText editText = (EditText) findViewById(R.id.CollaborativeMenuAnswerEt);
        if(editText != null && linearLayout != null){
            String suggestion = editText.getText().toString();
            boolean exists = false;
            for (Dish d : guestSuggestions) if (suggestion.equals(d.getName())) {exists = true; break;}
            if (exists || guestNewSuggestions.contains(suggestion)) {
                Snackbar.make(view, "This dish already exists in this menu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else if(!suggestion.equals("")) {

                //Creating text view containig proposal
                TextView textView = new TextView(this);
                textView.setText(suggestion);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,(float)0.8));

                //Creating image button to remove layout with proposal
                ImageButton button = new ImageButton(getApplicationContext());
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
                layout_in.addView(button);
                layout_in.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                //Adding the layout created before to the dynamic layout
                linearLayout.addView(layout_in);

                guestNewSuggestions.add(suggestion);

                //After adding suggestion we clear the editText for new suggestions
                editText.getText().clear();
            }
        }
    }


    //TESTING PURPOSES
    public List<String> getGuestNewSuggestions() {
        return guestNewSuggestions;
    }

}
