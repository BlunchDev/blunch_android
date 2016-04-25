package dev.blunch.blunch.services;


import android.content.Context;

import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.repositories.PaymentMenuAnswerRepository;
import dev.blunch.blunch.repositories.PaymentMenuRepository;

/**
 * Created by casassg on 17/04/16.
 *
 * @author casassg
 */
public final class ServiceFactory {

    public static CollaborativeMenuService getCollaborativeMenuService(Context context){
        return new CollaborativeMenuService(new CollaborativeMenuRepository(context), new DishRepository(context), new CollaborativeMenuAnswerRepository(context));
    }

    public static PaymentMenuService getPaymentMenuService(Context context) {
        return new PaymentMenuService(new PaymentMenuRepository(context),new DishRepository(context),new PaymentMenuAnswerRepository(context));
    }
}
