package dev.blunch.blunch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Menu;

/**
 * Created by jmotger on 19/04/16.
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
        return Long.parseLong(menuList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.menu_item_list_layout, null);
        ImageView icon = (ImageView) vi.findViewById(R.id.icon);
        TextView title = (TextView) vi.findViewById(R.id.menu_name);
        TextView description = (TextView) vi.findViewById(R.id.menu_descr);
        title.setText(menuList.get(position).getName());
        icon.setImageResource(R.drawable.user);
        description.setText(menuList.get(position).getDescription());
        return vi;
    }
}
