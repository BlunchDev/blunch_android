package dev.blunch.blunch.view;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Select Payment Dish Layout Class
 * @author albert
 */
public class SelectPaymentDishLayout extends LinearLayout {

    CheckBox checkBox;
    TextView dishNameText;
    TextView dishPriceText;

    @SuppressWarnings("all")
    public SelectPaymentDishLayout(Context context, String dishName, Integer price) {
        super(context);

        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);

        checkBox = new CheckBox(context);
        LinearLayout.LayoutParams checkLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(checkBox, checkLayout);

        dishNameText = new TextView(context);
        dishNameText.setText(dishName);
        LinearLayout.LayoutParams dishTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(dishNameText, dishTextLayout);

        dishPriceText = new TextView(context);
        dishPriceText.setText(price + " €");
        LinearLayout.LayoutParams priceTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(dishPriceText, priceTextLayout);

    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

}
