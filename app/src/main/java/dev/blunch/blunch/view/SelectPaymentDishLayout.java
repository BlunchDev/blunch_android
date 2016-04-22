package dev.blunch.blunch.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.blunch.blunch.R;

/**
 * Select Payment Dish Layout Class
 * @author albert
 */
public class SelectPaymentDishLayout extends LinearLayout {

    CheckBox checkBox;
    TextView dishNameText;
    TextView dishPriceText;

    @SuppressWarnings("all")
    public SelectPaymentDishLayout(Context context, String dishName, double price) {
        super(context);

        setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        setLayoutParams(mainParams);
        setGravity(Gravity.CENTER);

        checkBox = new CheckBox(context);
        //checkBox.setDrawingCacheBackgroundColor(getResources().getColor(R.color.black));
        //checkBox.setHintTextColor(getResources().getColor(R.color.black));
        int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        checkBox.setButtonDrawable(id);
        LinearLayout.LayoutParams checkLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(checkBox, checkLayout);

        dishNameText = new TextView(context);
        dishNameText.setText(dishName);
        dishNameText.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams dishTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(dishNameText, dishTextLayout);

        dishPriceText = new TextView(context);
        dishPriceText.setText(price + " €");
        dishPriceText.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams priceTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(dishPriceText, priceTextLayout);

    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

}
