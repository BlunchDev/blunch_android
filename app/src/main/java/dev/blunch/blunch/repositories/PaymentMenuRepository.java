package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    public PaymentMenu convert(DataSnapshot data) {
        PaymentMenu paymentMenu = new PaymentMenu();
        paymentMenu.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                paymentMenu.setName(d.getValue().toString());
            } else if (d.getKey().equals("author")) {
                paymentMenu.setAuthor(d.getValue().toString());
            } else if (d.getKey().equals("description")) {
                paymentMenu.setDescription(d.getValue().toString());
            } else if (d.getKey().equals("localization")) {
                paymentMenu.setLocalization(d.getValue().toString());
            } else if (d.getKey().equals("dateStart")) {
                paymentMenu.setDateStart(new Date(Long.parseLong(d.getValue().toString())));
            } else if (d.getKey().equals("dateEnd")) {
                paymentMenu.setDateEnd(new Date(Long.parseLong(d.getValue().toString())));
            } else if (d.getKey().equals("dishes")) {
                Set<String> dishes = new HashSet<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getValue().toString());
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

    public void close() {
        firebase.child(getObjectReference()).removeValue();
    }
}
