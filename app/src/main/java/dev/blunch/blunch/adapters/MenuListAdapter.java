package dev.blunch.blunch.adapters;

import android.content.Context;
import android.content.res.Resources;
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
import dev.blunch.blunch.domain.User;

/**
 * Menu List Adapter Class
 * @author jmotger on 19/04/16.
 */
public class MenuListAdapter extends BaseAdapter {

    Context context;
    List<Menu> menuList;
    List<User> userList;
    private static LayoutInflater inflater = null;

    public MenuListAdapter(Context context, List<Menu> data, List<User> users) {
        this.context = context;
        this.menuList = data;
        this.userList = users;
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
        ImageView userPic = (ImageView) vi.findViewById(R.id.user_icon);
        title.setText(menuList.get(position).getName());
        description.setText(menuList.get(position).getLocalization());

        String dateString = getDateString(menuList.get(position).getDateStart());
        String timeString = getTimeString(menuList.get(position).getDateStart(),
                menuList.get(position).getDateEnd());

        date.setText(dateString);
        time.setText(timeString);

        User userEnt = null;
        for (User u : userList) {
            if (u.getId().equals(menuList.get(position).getAuthor())) userEnt = u;
        }

        user.setText(userEnt.getName().split(" ")[0]);
        try {
            userPic.setImageDrawable(userEnt.getImageRounded(context.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        return  ((startH > 9) ? startH : ("0" + startH)).toString()
                + ":" + ((startM > 9) ? startM : ("0" + startM)).toString()
                + " - " + ((endH > 9) ? endH : ("0" + endH)).toString()
                + ":" + ((endM > 9) ? endM : ("0" + endM)).toString();
    }

    private String getDateString(Date dateStart) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateStart.getTime());
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + day;
        if (cal.get(Calendar.MONTH) + 1 < 10) month = "0" + month;
        return day + "/" + month + "/" + cal.get(Calendar.YEAR);
    }
}
