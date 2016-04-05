package view;

import android.content.Context;
import android.util.TypedValue;
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

        //Horizontal linear layout
        setOrientation(HORIZONTAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Suggestion TextView
        TextView suggest = new TextView(context);
        suggest.setText(suggestion);
        suggest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        LinearLayout.LayoutParams layoutParamsOfTextView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(suggest, layoutParamsOfTextView);

        /*
        suggest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(suggest);

        //Suggestion delete
        ImageButton delete = new ImageButton(context);
        //delete.setImageBitmap();
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(v);
            }
        });
        delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
*/


        /*LayoutInflater.from(getContext()).inflate(
                R.layout.card, this);*/
    }
}
