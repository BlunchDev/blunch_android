package dev.blunch.blunch.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
public class PaymentDishLayout extends LinearLayout{

    private EditText dishName;
    private EditText priceDish;
    private ImageButton close;

    public PaymentDishLayout(Context context, int num) {
        super(context);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //H1
        final LinearLayout h1 = new LinearLayout(context);
        h1.setOrientation(HORIZONTAL);

        Space space1 = new Space(context);
        LinearLayout.LayoutParams spaceLayout = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space1, spaceLayout);

        dishName = new EditText(context);
        dishName.setText("Plato " + num);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        dishName.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams nomPlatLayout = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT);

        h1.addView(dishName, nomPlatLayout);
        /*dishName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishName.getText().toString().equals("MENÚ")) {
                    dishName.setText("");
                    dishName.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });*/
        priceDish = new EditText(context);
        priceDish.setText("Precio");
        priceDish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        priceDish.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams priceDishLayout = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT);

        h1.addView(dishName, priceDishLayout);

        Space space5 = new Space(context);
        LinearLayout.LayoutParams space5Layout = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space5, space5Layout);

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
               PaymentDishLayout.this.setVisibility(GONE);
            }
        });
        h1.addView(close, closeLayout);



        LinearLayout.LayoutParams h1Layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(h1, h1Layout);
        /*
        //H2
        LinearLayout h2 = new LinearLayout(context);
        h2.setOrientation(HORIZONTAL);

        Space space2 = new Space(context);
        LinearLayout.LayoutParams space2Layout = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        h2.addView(space2, space2Layout);

        Space space3 = new Space(context);
        LinearLayout.LayoutParams space3Layout = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        h2.addView(space3, space3Layout);

        Space space4 = new Space(context);
        LinearLayout.LayoutParams space4Layout = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        h2.addView(space4, space4Layout);

        LinearLayout.LayoutParams h2Layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        h2Layout.gravity = Gravity.RIGHT;
        this.addView(h2, h2Layout);
        */
    }

    public String getDishName(){
        return dishName.getText().toString();
    }

    public ImageButton getClose() {
        return close;
    }

}
