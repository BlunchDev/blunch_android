package dev.blunch.blunch.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.User;

/**
 * Created by jmotger on 13/05/16.
 */
public class MenuRecyclerView extends RecyclerView.Adapter<MenuRecyclerView.ViewHolder> {

    private final List<Menu> mValues;
    private final List<User> users;
    private final Context context;

    public MenuRecyclerView(Context context,
                                         List<Menu> items,
                                         List<User> users) {
        mValues = items;
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.menuName.setText(holder.mItem.getName());
        holder.menuLocation.setText(holder.mItem.getLocalization());

        String dateString = getDateString(holder.mItem.getDateStart());
        String timeString = getTimeString(holder.mItem.getDateStart(),
                holder.mItem.getDateEnd());

        holder.menuDate.setText(dateString);
        holder.menuTime.setText(timeString);

        User userEnt = null;
        for (User u : users) {
            if (u.getId().equals(holder.mItem.getAuthor())) userEnt = u;
        }

        holder.userName.setText(userEnt.getName().split(" ")[0]);
        try {
            holder.userIcon.setImageDrawable(userEnt.getImageRounded(context.getResources()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CollaborativeMenu.class.isAssignableFrom(holder.mItem.getClass())) {
            holder.menuType.setImageResource(R.drawable.group);
        } else {
            holder.menuType.setImageResource(R.drawable.euro);
        }
    }

    private void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Menu mItem;
        public ImageView userIcon;
        public ImageView menuType;
        public TextView userName;
        public TextView menuName;
        public TextView menuLocation;
        public TextView menuDate;
        public TextView menuTime;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            userIcon = (ImageView) view.findViewById(R.id.user_icon);
            menuType = (ImageView) view.findViewById(R.id.menu_type);
            userName = (TextView) view.findViewById(R.id.user_name);
            menuName = (TextView) view.findViewById(R.id.menu_name);
            menuLocation = (TextView) view.findViewById(R.id.menu_loc);
            menuDate = (TextView) view.findViewById(R.id.menu_date);
            menuTime = (TextView) view.findViewById(R.id.menu_time);

        }

        @Override
        public String toString() {
            return super.toString();
        }
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
