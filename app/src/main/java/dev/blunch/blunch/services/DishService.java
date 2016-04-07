package dev.blunch.blunch.services;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Dish Service Class
 * Created by albert on 7/04/16.
 */
public class DishService extends Service<Dish> {

    public DishService(Repository<Dish> repository) {
        super(repository);
    }

}
