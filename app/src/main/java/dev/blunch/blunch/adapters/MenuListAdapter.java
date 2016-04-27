package dev.blunch.blunch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Menu;

/**
 * Menu List Adapter Class
 * @author jmotger on 19/04/16.
 */
public class MenuListAdapter extends BaseAdapter {

    Context context;
    List<Menu> menuList;
    private static LayoutInflater inflater = null;

    public MenuListAdapter(Context context, List<Menu> data) {
        this.context = context;
        this.menuList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Menu getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.menu_item_list_layout, null);
        TextView title = (TextView) vi.findViewById(R.id.menu_name);
        TextView description = (TextView) vi.findViewById(R.id.menu_loc);
        TextView date = (TextView) vi.findViewById(R.id.menu_date);
        TextView time = (TextView) vi.findViewById(R.id.menu_time);
        TextView user = (TextView) vi.findViewById(R.id.user_name);
        ImageView type = (ImageView) vi.findViewById(R.id.menu_type);
        title.setText(menuList.get(position).getName());
        description.setText(menuList.get(position).getLocalization());

        String dateString = getDateString(menuList.get(position).getDateStart());
        String timeString = getTimeString(menuList.get(position).getDateStart(),
                menuList.get(position).getDateEnd());

        date.setText(dateString);
        time.setText(timeString);

        user.setText(menuList.get(position).getAuthor());
        if (CollaborativeMenu.class.isAssignableFrom(menuList.get(position).getClass())) {
            type.setImageResource(R.drawable.group);
        } else {
            type.setImageResource(R.drawable.euro);
        }
        return vi;
    }

    private String getTimeString(Date dateStart, Date dateEnd) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(dateStart.getTime());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(dateEnd.getTime());

        int startH = start.get(Calendar.HOUR_OF_DAY);
        int startM = start.get(Calendar.MINUTE);
        int endH = end.get(Calendar.HOUR_OF_DAY);
        int endM = end.get(Calendar.MINUTE);

        String s = ((startH > 9) ? startH : ("0" + startH)).toString()
                + ":" + ((startM > 9) ? startM : ("0" + startM)).toString()
                + " - " + ((endH > 9) ? endH : ("0" + endH)).toString()
                + ":" + ((endM > 9) ? endM : ("0" + endM)).toString();
        return s;
    }

    private String getDateString(Date dateStart) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateStart.getTime());
        String s = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        return s;
    }
}
