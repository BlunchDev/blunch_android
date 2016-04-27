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

        int margin10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int margin50 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());


        setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.setMargins(margin50, 0, 0, margin10);
        mainParams.gravity = Gravity.CENTER;
        setLayoutParams(mainParams);

        checkBox = new CheckBox(context);
        int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        checkBox.setButtonDrawable(id);
        LinearLayout.LayoutParams checkLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(checkBox, checkLayout);

        dishNameText = new TextView(context);
        dishNameText.setText(dishName);
        dishNameText.setTextColor(getResources().getColor(R.color.gray));
        dishNameText.setTextSize(18);
        LinearLayout.LayoutParams dishTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(dishNameText, dishTextLayout);

        dishPriceText = new TextView(context);
        dishPriceText.setText("(" + price + " €)");
        dishPriceText.setTextColor(getResources().getColor(R.color.gray));
        dishPriceText.setTextSize(18);
        LinearLayout.LayoutParams priceTextLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        priceTextLayout.setMargins(margin10, 0, 0, 0);
        addView(dishPriceText, priceTextLayout);

    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

}
