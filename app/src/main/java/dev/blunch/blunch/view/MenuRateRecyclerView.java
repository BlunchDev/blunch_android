package dev.blunch.blunch.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.activity.GetCollaborativeMenuActivity;
import dev.blunch.blunch.activity.GetPaymentMenuActivity;
import dev.blunch.blunch.activity.ValorationActivity;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.domain.Valoration;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.utils.Preferences;

/**
 * Created by jmotger on 13/05/16.
 */
public class MenuRateRecyclerView extends RecyclerView.Adapter<MenuRateRecyclerView.ViewHolder> {

    private final List<Menu> mValues;
    private final List<User> users;
    private final Context context;
    private final MenuService menuService;

    public ViewHolder holder;

    public MenuRateRecyclerView(Context context,
                            List<Menu> items,
                            List<User> users,
                            MenuService menuService) {
        mValues = items;
        this.users = users;
        this.context = context;
        this.menuService = menuService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
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
            holder.menuType.setImageResource(R.mipmap.collaborativeIcon);
        } else {
            holder.menuType.setImageResource(R.mipmap.paymentIcon);
        }
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
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String menuId = mItem.getId();
                    String userId = Preferences.getCurrentUserEmail();

                    if (!menuService.isValuedBy(menuId, userId)) {
                        Intent intent = new Intent(v.getContext(), ValorationActivity.class);
                        intent.putExtra(ValorationActivity.MENU_ID, menuId);
                        intent.putExtra(ValorationActivity.USER_ID, userId);
                        v.getContext().startActivity(intent);
                    }
                    else {
                        Valoration val = menuService.getValoration(menuId, userId);
                        boolean decimal = ((int) val.getPoints() < val.getPoints());
                        Snackbar.make(v.getRootView(), "Este menú ya fue valorado en " +
                                        (decimal ? (int) val.getPoints() + " estrellas." : (int) val.getPoints() + " estrellas y media."),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });

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
