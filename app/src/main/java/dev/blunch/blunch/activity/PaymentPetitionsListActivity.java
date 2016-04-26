package dev.blunch.blunch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.services.CollaborativeMenuService;
import dev.blunch.blunch.services.PaymentMenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Repository;

public class PaymentPetitionsListActivity extends AppCompatActivity {

    public static final String ID_PAYMENT_MENU_KEY = "payment_menu_key";
    private PaymentMenuService service;
    private String idMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_petitions_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View recyclerView = findViewById(R.id.petitions_list);
        assert recyclerView != null;
        ((RecyclerView) recyclerView).setAdapter(
                new SimpleItemRecyclerViewAdapter(new ArrayList<PaymentMenuAnswer>())
        );

        Intent intent = getIntent();

        idMenu = null;
        if (intent.hasExtra(ID_PAYMENT_MENU_KEY)) {
            idMenu = intent.getStringExtra(ID_PAYMENT_MENU_KEY);
        }

        service = ServiceFactory.getPaymentMenuService(getApplicationContext());
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                //TODO: posar aixo be quan es tinguin les ids dels menus via intent
//                PaymentMenu menu = service.get(idMenu);
                PaymentMenu menu = service.getAll().get(0);
                if (menu != null && idMenu==null) {
                    assert toolbar != null;
                    toolbar.setTitle("Petitions for "+menu.getName());
                    idMenu = menu.getId();
                }
            }
        });

        service.setPaymentMenuAnswerListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                View recyclerView = findViewById(R.id.petitions_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView, idMenu);
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String id) {
        List<PaymentMenuAnswer> petitions = service.getAnswers(id);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(petitions));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<PaymentMenuAnswer> mValues;

        public SimpleItemRecyclerViewAdapter(List<PaymentMenuAnswer> items) {
            mValues = items;
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
                    result += d.getName() + "\n";
                    total += d.getPrice();
                }
            }

            holder.mContentView.setText(result);
            holder.totalView.setText(total+"€");
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
            public PaymentMenuAnswer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.contentDishes);
                totalView = (TextView) view.findViewById(R.id.total);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
