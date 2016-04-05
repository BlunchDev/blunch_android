package dev.blunch.blunch.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import dev.blunch.blunch.R;

/**
 * Created by pere on 4/5/16.
 */
public class CollaborativeDishLayout extends LinearLayout{

    public CollaborativeDishLayout(Context context, int num) {
        super(context);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //H1
        LinearLayout h1 = new LinearLayout(context);
        h1.setOrientation(HORIZONTAL);

        Space space1 = new Space(context);
        LinearLayout.LayoutParams spaceLayout = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        h1.addView(space1, spaceLayout);

        EditText nomPlat = new EditText(context);
        nomPlat.setText("Plato " + num);
        nomPlat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        nomPlat.setTextColor(getResources().getColor(R.color.colorEdit));
        LinearLayout.LayoutParams nomPlatLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        h1.addView(nomPlat, nomPlatLayout);

        LinearLayout.LayoutParams h1Layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(h1, h1Layout);

        //H2
        LinearLayout h2 = new LinearLayout(context);
        h2.setOrientation(HORIZONTAL);
        h2.setHorizontalGravity(FOCUS_RIGHT);

        TextView me = new TextView(context);
        me.setText("Yo");
        LinearLayout.LayoutParams meLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        h2.addView(me, meLayout);

        Space space2 = new Space(context);
        LinearLayout.LayoutParams space2Layout = new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT);
        h2.addView(space2, space2Layout);

        Switch switch1 = new Switch(context);
        switch1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        switch1.setChecked(false);
        LinearLayout.LayoutParams switchLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        h2.addView(switch1, switchLayout);

        Space space3 = new Space(context);
        LinearLayout.LayoutParams space3Layout = new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT);
        h2.addView(space3, space3Layout);

        TextView sug = new TextView(context);
        sug.setText("Sugerencia");
        h2.addView(sug, meLayout);

        LinearLayout.LayoutParams h2Layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(h2, h2Layout);

    }


}
