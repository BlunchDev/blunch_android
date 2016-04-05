package dev.blunch.blunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
        CollaborativeMenuAnswerSuggestionLayout suggestion = new CollaborativeMenuAnswerSuggestionLayout(getApplicationContext(), editText.getText().toString());
        linearLayout.addView(suggestion);
    }

}
