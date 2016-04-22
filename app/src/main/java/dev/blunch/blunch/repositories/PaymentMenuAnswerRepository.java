package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

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
        return data.getValue(PaymentMenuAnswer.class);
    }
}


