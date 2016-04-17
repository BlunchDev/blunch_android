package dev.blunch.blunch.services;


import android.content.Context;

import dev.blunch.blunch.repositories.CollaborativeMenuRepository;
import dev.blunch.blunch.repositories.DishRepository;

/**
 * Created by casassg on 17/04/16.
 *
 * @author casassg
 */
public final class ServiceFactory {

    public static CollaborativeMenuService getCollaborativeMenuService(Context context){
        return new CollaborativeMenuService(new CollaborativeMenuRepository(context), new DishRepository(context));
    }
}
