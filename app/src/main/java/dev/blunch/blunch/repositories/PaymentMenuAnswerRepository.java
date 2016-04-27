package dev.blunch.blunch.repositories;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by casassg on 22/04/16.
 *
 * @author casassg
 */
public class PaymentMenuAnswerRepository extends FirebaseRepository<PaymentMenuAnswer> {
    /**
     * Constructor class
     *
     * @param context
     */
    public PaymentMenuAnswerRepository(Context context) {
        super(context);
    }

    @Override
    public String getObjectReference() {
        return "paymentMenuAnswer";
    }

    @Override
    protected PaymentMenuAnswer convert(DataSnapshot data) {
        PaymentMenuAnswer answer = new PaymentMenuAnswer();
        answer.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("idMenu"))
                answer.setIdMenu(d.getValue(String.class));
            else if (d.getKey().equals("guest"))
                answer.setGuest(d.getValue(String.class));
            else if (d.getKey().equals("choosenDishes")) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                answer.setChoosenDishesKeys(dishes);
            }
        }
        return answer;
    }

}


