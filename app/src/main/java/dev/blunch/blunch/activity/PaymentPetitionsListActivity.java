package dev.blunch.blunch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.BaseActivity;

@SuppressWarnings("all")
public class PaymentPetitionsListActivity extends BaseActivity {

    public static final String ID_PAYMENT_MENU_KEY = "menuId";
    private PaymentMenuService service;
    private String idMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_petitions_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        idMenu = null;
        if (intent.hasExtra(ID_PAYMENT_MENU_KEY)) {
            idMenu = intent.getStringExtra(ID_PAYMENT_MENU_KEY);
        }

        service = ServiceFactory.getPaymentMenuService(getApplicationContext());

        PaymentMenu menu = service.get(idMenu);
        assert menu !=null;
        assert toolbar != null;
        setTitle("Peticiones de " + menu.getName());

        View recyclerView2 = findViewById(R.id.petitions_list);
        assert recyclerView2 != null;
        setupRecyclerView((RecyclerView) recyclerView2, idMenu);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String id) {
        List<PaymentMenuAnswer> petitions = service.getAnswers(id);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getApplicationContext(),
                petitions, service.getUsers()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<PaymentMenuAnswer> mValues;
        private final List<User> users;
        private final Context context;

        public SimpleItemRecyclerViewAdapter(Context context,
                                             List<PaymentMenuAnswer> items,
                                             List<User> users) {
            mValues = items;
            this.users = users;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.petitions_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            String result = "";
            int total = 0;
            List <Dish> petition = service.getAnswerDishes(holder.mItem.getId());
            for (Dish d : petition){
                if (d != null) {
                    result +="- "+ d.getName() + "\n";
                    total += d.getPrice();
                }
            }
            if (position == mValues.size() - 1) holder.divider.setVisibility(View.GONE);
            holder.mContentView.setText(result);
            holder.totalView.setText(String.valueOf(total));

            User user = null;
            for(User u : users) if (u.getId().equals(holder.mItem.getGuest())) user = u;

            if (user == null){
                MenuService menuService = ServiceFactory.getMenuService(context);
                user = menuService.findUserByEmail(holder.mItem.getGuest());
            }

            try {
                holder.commentView.setText("Petición de "+ user.getName());
                holder.profilePic.setImageDrawable(user.getImageRounded(context.getResources()));
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
            public final TextView totalView;
            public final TextView commentView;
            public final TextView euro;
            public final ImageView profilePic;
            public final ImageView divider;
            public PaymentMenuAnswer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.contentDishes);
                totalView = (TextView) view.findViewById(R.id.total);
                commentView = (TextView) view.findViewById(R.id.comment);
                profilePic = (ImageView) view.findViewById(R.id.profile_pic);
                divider = (ImageView) view.findViewById(R.id.divider);
                euro = (TextView) view.findViewById(R.id.euro);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
