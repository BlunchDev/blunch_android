package dev.blunch.blunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.Vector;

public class CollaborativeMenuAnswer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_menu_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}
