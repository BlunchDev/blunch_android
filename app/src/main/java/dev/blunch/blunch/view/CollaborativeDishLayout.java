package dev.blunch.blunch.view;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import dev.blunch.blunch.R;

/**
 * Collaborative Dish Layout Class
 * @author pere
 */
public class CollaborativeDishLayout extends LinearLayout{

    private EditText dishName;
    private Switch switch1;
    private ImageButton close;


    public CollaborativeDishLayout(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Space space1 = new Space(context);
        LinearLayout.LayoutParams spaceLayout = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space1, spaceLayout);

        dishName = new EditText(context);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        dishName.setTextColor(getResources().getColor(R.color.black));
        dishName.setHintTextColor(getResources().getColor(R.color.gray));
        dishName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        dishName.setHint("Nombre");
        LinearLayout.LayoutParams nomPlatLayout = new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(dishName, nomPlatLayout);

        Space space5 = new Space(context);
        LinearLayout.LayoutParams space5Layout = new LinearLayout.LayoutParams(15, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space5, space5Layout);

        close = new ImageButton(context);
        close.setBackgroundColor(getResources().getColor(R.color.background));
        close.setImageResource(R.drawable.cross);
        close.setScaleType(ImageView.ScaleType.FIT_XY);
        close.setScaleX(0.5f);
        close.setScaleY(0.5f);
        close.setId((int) Math.random());

        LinearLayout.LayoutParams closeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CollaborativeDishLayout.this.setVisibility(GONE);
            }
        });
        addView(close, closeLayout);

        Space space6 = new Space(context);
        LinearLayout.LayoutParams space6Layout = new LinearLayout.LayoutParams(15, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space6, space6Layout);

        TextView me = new TextView(context);
        me.setText("Yo");
        me.setTextColor(getResources().getColor(R.color.gray));
        LinearLayout.LayoutParams meLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(me, meLayout);

        Space space2 = new Space(context);
        LinearLayout.LayoutParams space2Layout = new LinearLayout.LayoutParams(25, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space2, space2Layout);

        switch1 = new Switch(context);
        switch1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        switch1.setChecked(false);
        LinearLayout.LayoutParams switchLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(switch1, switchLayout);

        Space space3 = new Space(context);
        LinearLayout.LayoutParams space3Layout = new LinearLayout.LayoutParams(25, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space3, space3Layout);

        TextView sug = new TextView(context);
        sug.setText("Sug");
        sug.setTextColor(getResources().getColor(R.color.gray));
        addView(sug, meLayout);
    }

    public String getDishName(){
        return dishName.getText().toString();
    }

    public Boolean isSuggest() {
        return switch1.isChecked();
    }

    public ImageButton getClose() {
        return close;
    }

}
