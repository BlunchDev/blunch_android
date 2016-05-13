package dev.blunch.blunch.services;


import android.content.Context;

import dev.blunch.blunch.repositories.ChatMessageRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuAnswerRepository;
import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;
import dev.blunch.blunch.repositories.PaymentMenuAnswerRepository;
import dev.blunch.blunch.repositories.PaymentMenuRepository;
import dev.blunch.blunch.repositories.UserRepository;
import dev.blunch.blunch.repositories.ValorationRepository;

/**
 * Created by casassg on 17/04/16.
 *
 * @author casassg
 */
public final class ServiceFactory {

    private static CollaborativeMenuService collaborativeMenuService;
    private static PaymentMenuService paymentMenuService;
    private static MenuService menuService;

    public static CollaborativeMenuService getCollaborativeMenuService(Context context){
        if (collaborativeMenuService == null)
            collaborativeMenuService = new CollaborativeMenuService(
                    new CollaborativeMenuRepository(context),
                    new DishRepository(context),
                    new CollaborativeMenuAnswerRepository(context),
                    new UserRepository(context));
        return collaborativeMenuService;
    }

    public static PaymentMenuService getPaymentMenuService(Context context) {
        if (paymentMenuService == null)
            paymentMenuService = new PaymentMenuService(
                    new PaymentMenuRepository(context),
                    new DishRepository(context),
                    new PaymentMenuAnswerRepository(context),
                    new UserRepository(context));
        return paymentMenuService;
    }

    public static MenuService getMenuService(Context context) {
        if (menuService == null)
            menuService = new MenuService(
                    new CollaborativeMenuRepository(context),
                    new PaymentMenuRepository(context),
                    new ValorationRepository(context),
                    new UserRepository(context),
                    new ChatMessageRepository(context));
        return menuService;
    }

}
