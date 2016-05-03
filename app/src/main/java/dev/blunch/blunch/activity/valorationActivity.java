package dev.blunch.blunch.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import dev.blunch.blunch.R;

public class valorationActivity extends AppCompatActivity {

    private RatingBar valoration;
    private EditText comment;
    private ImageButton done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoration);

        valoration = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.valorationComment);
        done = (ImageButton) findViewById(R.id.valorationDone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
