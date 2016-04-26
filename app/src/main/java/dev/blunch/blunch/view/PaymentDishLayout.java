package dev.blunch.blunch.view;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import dev.blunch.blunch.R;

public class PaymentDishLayout extends LinearLayout{

    private EditText dishName;
    private EditText priceDish;
    private ImageButton close;
    private TextView eur;

    public PaymentDishLayout(Context context, int num) {
        super(context);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //H1

        final LinearLayout h1 = new LinearLayout(context);
        h1.setOrientation(HORIZONTAL);

        dishName = new EditText(context);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        dishName.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams nomPlatLayout = new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        h1.addView(dishName, nomPlatLayout);
        dishName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishName.getText().toString().equals("MENÚ")) {
                    dishName.setText("");
                    dishName.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        Space space5 = new Space(context);
        LinearLayout.LayoutParams space5Layout = new LinearLayout.LayoutParams(8, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space5, space5Layout);

        priceDish = new EditText(context);
        priceDish.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceDish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        priceDish.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams priceDishLayout = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT);

        h1.addView(priceDish, priceDishLayout);

        Space space6 = new Space(context);
        LinearLayout.LayoutParams space6Layout = new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space6, space6Layout);

        eur = new TextView(context);
        eur.setText("€");
        eur.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        eur.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams eurLayout = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);

        h1.addView(eur, eurLayout);

        Space space7 = new Space(context);
        LinearLayout.LayoutParams space7Layout = new LinearLayout.LayoutParams(18, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space7, space7Layout);

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
    }

    public String getDishName(){return dishName.getText().toString();}

    public Double getDishPrice(){return Double.parseDouble(priceDish.getText().toString());}

    public ImageButton getClose() {
        return close;
    }

}
