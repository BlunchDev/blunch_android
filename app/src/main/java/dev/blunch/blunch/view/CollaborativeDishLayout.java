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
    private ImageButton close;


    public CollaborativeDishLayout(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
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
        dishName.setWidth(10);
        LinearLayout.LayoutParams nomPlatLayout = new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(dishName, nomPlatLayout);

        Space space5 = new Space(context);
        LinearLayout.LayoutParams space5Layout = new LinearLayout.LayoutParams(15, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(space5, space5Layout);

        close = new ImageButton(context);
        close.setBackgroundColor(getResources().getColor(R.color.white));
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

    }

    public String getDishName(){
        return dishName.getText().toString();
    }

    public ImageButton getClose() {
        return close;
    }

}
