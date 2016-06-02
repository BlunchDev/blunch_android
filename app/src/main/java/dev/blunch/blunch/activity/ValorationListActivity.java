package dev.blunch.blunch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.domain.Valoration;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;

/**
 * Valoration List Activity Class
 * @author albert
 */
@SuppressWarnings("all")
public class ValorationListActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";
    private MenuService service;
    private String userId;

    @Override
    protected void onResume() {
        super.onResume();
        Preferences.init(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoration_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_valoration);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        userId = null;
        if (intent.hasExtra(USER_ID)) {
            userId = intent.getStringExtra(USER_ID);
        }

        service = ServiceFactory.getMenuService(getApplicationContext());

        User user = service.findUserByEmail(userId);
        assert user !=null;
        assert toolbar != null;
        setTitle("Valoraciones de " + user.getName());

        View recyclerView2 = findViewById(R.id.valoration_list);
        assert recyclerView2 != null;
        setupRecyclerView((RecyclerView) recyclerView2, userId);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String id) {
        List<Valoration> valorationList = service.getValorationsTo(id);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getApplicationContext(),
                valorationList, service.getUsers()));

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Valoration> mValues;
        private final List<User> users;
        private final Context context;

        public SimpleItemRecyclerViewAdapter(Context context,
                                             List<Valoration> items,
                                             List<User> users) {
            mValues = items;
            this.users = users;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.valoration_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);

            User guest = null;
            for (User u : users) if (u.getId().equals(holder.mItem.getGuest())) guest = u;

            if (position == mValues.size() - 1) holder.divider.setVisibility(View.GONE);
            holder.mContentView.setText("Valoración de " + guest.getName());

            holder.commentView.setText(holder.mItem.getComment());

            holder.ratingBar.setRating((float) holder.mItem.getPoints());

            try {
                holder.profilePic.setImageDrawable(guest.getImageRounded(context.getResources()));
            } catch (Exception e) {
                e.printStackTrace();
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
            public final TextView mContentView;
            public final TextView commentView;
            public final ImageView profilePic;
            public final ImageView divider;
            public final RatingBar ratingBar;
            public Valoration mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.valueBy);
                commentView = (TextView) view.findViewById(R.id.comment);
                profilePic = (ImageView) view.findViewById(R.id.profile_pic);
                divider = (ImageView) view.findViewById(R.id.divider);
                ratingBar = (RatingBar) view.findViewById(R.id.getValue);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
