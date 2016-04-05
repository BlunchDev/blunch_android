package dev.blunch.blunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

import view.CollaborativeMenuAnswerSuggestionLayout;

public class CollaborativeMenuAnswer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_menu_answer);
        fillHostSuggestions();
    }

    private void fillHostSuggestions(){
        /*
        Accedir a BD i consultar les recomanacions del host o pasarles des de la
        pantalla anterior
         */
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerHostSuggestions);
        Vector<String> hostSuggestions = new Vector<String>();
        for(int i = 0; i < hostSuggestions.size();++i){
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(hostSuggestions.get(i));
            linearLayout.addView(checkBox);
        }

    }

    public void addSuggestion(View view){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.CollaborativeMenuAnswerGuestSuggestions);
        EditText editText = (EditText) findViewById(R.id.CollaborativeMenuAnswerEt);
        String suggestion = editText.getText().toString();
        if(suggestion != null && !suggestion.equals("")) {

            //CollaborativeMenuAnswerSuggestionLayout suggestion = new CollaborativeMenuAnswerSuggestionLayout(getApplicationContext(), editText.getText().toString());
            //linearLayout.addView(suggestion);

            TextView textView = new TextView(this);
            textView.setText(suggestion);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            ImageButton button = new ImageButton(getApplicationContext());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    parent.removeAllViews();
                }
            });
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout layout_in = new LinearLayout(getApplicationContext());
            layout_in.addView(textView);
            layout_in.addView(button);
            layout_in.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(layout_in);

            editText.getText().clear();
        }
    }

}
