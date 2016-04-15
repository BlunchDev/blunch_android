package dev.blunch.blunch.activity;

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
import java.util.List;
import java.util.Vector;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.utils.Entity;

public class CollaborativeMenuAnswerActivity extends AppCompatActivity {

    private CollaborativeMenuService collaborativeMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_menu_answer);
        collaborativeMenuService = new CollaborativeMenuService(
                new CollaborativeMenuRepository(getApplicationContext()),
                new DishRepository(getApplicationContext()),
                new CollaborativeMenuAnswerRepository(getApplicationContext()));

        //we add the onClick function to the button add
        makeProposalCreation();
    }

    private void makeProposalCreation() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.CollaborativeMenuAnswerBtnEnd);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);
                    if (linearLayout != null) {
                        for(int i = 0; i < linearLayout.getChildCount();++i){
                            LinearLayout tuple = (LinearLayout) linearLayout.getChildAt(i);
                            if(tuple != null) {
                                TextView suggestion = (TextView) tuple.getChildAt(0);
                                if (suggestion != null) s += " " + suggestion.getText();
                            }
                        }
                    }

                    Snackbar.make(view, "This are the suggestions: " + s, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    public void fillHostSuggestions(List<Dish> dishes){
        List<String> hostSuggestions = new ArrayList<>();
        for (Dish d : dishes) {
            hostSuggestions.add(d.getName());
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        if(linearLayout != null) {
            for (int i = 0; i < hostSuggestions.size(); ++i) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(hostSuggestions.get(i));
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                linearLayout.addView(checkBox);
            }
        }

    }

    public void addSuggestion(View view){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);
        EditText editText = (EditText) findViewById(R.id.CollaborativeMenuAnswerEt);
        if(editText != null && linearLayout != null){
            String suggestion = editText.getText().toString();
            if(!suggestion.equals("")) {

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

                //After adding suggestion we clear the editText for new suggestions
                editText.getText().clear();
            }
        }
    }


}
