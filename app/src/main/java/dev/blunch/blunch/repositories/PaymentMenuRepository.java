package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
public class PaymentMenuRepository extends FirebaseRepository<PaymentMenu> {

    public PaymentMenuRepository(Context context) {
        super(context);
    }

    @Override
    protected PaymentMenu convert(DataSnapshot data) {
        PaymentMenu paymentMenu = new PaymentMenu();
        paymentMenu.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                paymentMenu.setName(d.getValue(String.class));
            } else if (d.getKey().equals("author")) {
                paymentMenu.setAuthor(d.getValue(String.class));
            } else if (d.getKey().equals("description")) {
                paymentMenu.setDescription(d.getValue(String.class));
            } else if (d.getKey().equals("localization")) {
                paymentMenu.setLocalization(d.getValue(String.class));
            } else if (d.getKey().equals("dateStart")) {
                paymentMenu.setDateStart(d.getValue(Date.class));
            } else if (d.getKey().equals("dateEnd")) {
                paymentMenu.setDateEnd(d.getValue(Date.class));
            } else if (d.getKey().equals("dishes")) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                paymentMenu.setDishes(dishes);
            }
        }
        return paymentMenu;
    }

    @Override
    public String getObjectReference() {
        return "paymentMenu";
    }
}
