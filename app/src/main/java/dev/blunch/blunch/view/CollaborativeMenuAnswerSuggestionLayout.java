package dev.blunch.blunch.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Víctor on 05/04/2016.
 */
public class CollaborativeMenuAnswerSuggestionLayout extends LinearLayout {

    public CollaborativeMenuAnswerSuggestionLayout(Context context, String suggestion){
        super(context);

        //layout Parameters
        setOrientation(HORIZONTAL);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView valueTV = new TextView(context);
        valueTV.setText("hola cocacola como estas");
        valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        ImageButton button = new ImageButton(context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) v.getParent();
                parent.removeAllViews();
            }
        });
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        addView(valueTV);
        addView(button);
    }
}
