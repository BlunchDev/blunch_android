package dev.blunch.blunch.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.ServiceFactory;

public class CollaborativePetitionsListActivity extends AppCompatActivity {


    public static final String MENU_ID_KEY = "menuId";
    private static final String TAG = CollaborativePetitionsListActivity.class.getSimpleName();
    private CollaborativeMenuService service;
    private String idMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_proposal_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.proposal_list);
        assert recyclerView != null;
        ((RecyclerView) recyclerView).setAdapter(
                new SimpleItemRecyclerViewAdapter(getApplicationContext(),
                        new ArrayList<CollaborativeMenuAnswer>(),
                        new ArrayList<User>())
        );

        idMenu = getIntent().getStringExtra(MENU_ID_KEY);

        service = ServiceFactory.getCollaborativeMenuService(getApplicationContext());

        CollaborativeMenu menu = service.get(idMenu);
        if (menu != null && idMenu == null) {
            toolbar.setTitle("Answers for " + menu.getName());
            idMenu = menu.getId();
        }

        setTitle("Peticiones de " + menu.getName());

        View recyclerView2 = findViewById(R.id.proposal_list);
        assert recyclerView2 != null;
        setupRecyclerView((RecyclerView) recyclerView2, idMenu);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String id) {
        List<CollaborativeMenuAnswer> proposal = service.getProposal(id);
        Log.e(TAG, "" + proposal.size());
        Log.e(TAG, "" + id);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
                getApplicationContext(), proposal, service.getUsers()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<CollaborativeMenuAnswer> mValues;
        private final List<User> users;
        private final Context context;

        public SimpleItemRecyclerViewAdapter(Context context,
                                             List<CollaborativeMenuAnswer> items,
                                             List<User> users) {
            mValues = items;
            this.users = users;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.proposal_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            String result = "";
            for (String dish_key : holder.mItem.getOfferedDishes().keySet()) {
                Dish dish = service.getDish(dish_key);
                if (dish != null) {
                    result += "- " + dish.getName() + "\n";
                }
            }

            if (position == mValues.size() - 1) holder.divider.setVisibility(View.GONE);
            holder.mContentView.setText(result);

            User user = null;
            for (User u : users) if (u.getId().equals(holder.mItem.getGuest())) user = u;

            holder.titleView.setText("Propuesta de " + user.getName());
            try {
                holder.profilePic.setImageDrawable(user.getImageRounded(context.getResources()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.acceptView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service.acceptProposal(holder.mItem.getId());
                    removeItem(position);
                    Toast.makeText(getApplicationContext(), "Solicitud de participación aceptada", Toast.LENGTH_SHORT).show();
                }
            });

            holder.rejectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service.declineProposal(holder.mItem.getId());
                    removeItem(position);
                    Toast.makeText(getApplicationContext(), "Solicitud de participación rechazada", Toast.LENGTH_SHORT).show();
                }
            });
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
            public final ImageView acceptView;
            public final ImageView rejectView;
            public final ImageView profilePic;
            public final TextView titleView;
            public final ImageView divider;
            public CollaborativeMenuAnswer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                acceptView = (ImageView) view.findViewById(R.id.check);
                rejectView = (ImageView) view.findViewById(R.id.close);
                mContentView = (TextView) view.findViewById(R.id.contentDishes);
                profilePic = (ImageView) view.findViewById(R.id.profile_pic);
                titleView = (TextView) view.findViewById(R.id.comment);
                divider = (ImageView) view.findViewById(R.id.divider);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
