package dev.blunch.blunch.services;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Payment Menu Service Class
 * @author albert on 7/04/16.
 */
public class PaymentMenuService extends Service<PaymentMenu> {

    private final Repository<Dish> dishesRepository;

    public PaymentMenuService(Repository<PaymentMenu> repository) {
        super(repository);
        dishesRepository = null;
    }

    @Override
    public PaymentMenu save(PaymentMenu item) {
        return super.save(item);
    }

    public PaymentMenu save(PaymentMenu item, List<Dish> dishes) {
        if (dishesRepository == null){
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        for (Dish dish : dishes) {
            dishesRepository.insert(dish);
            item.addDish(dish.getId());
        }
        return repository.insert(item);
    }

    public PaymentMenuService(Repository<PaymentMenu> repository, Repository<Dish> dishRepository) {
        super(repository);
        dishesRepository = dishRepository;
    }

    public List<Dish> getDishes(String key) {
        List<Dish> list = new ArrayList<>();
        PaymentMenu collaborativeMenu = get(key);
        for (String k : collaborativeMenu.getDishes().keySet()) {
            list.add(dishesRepository.get(k));
        }
        return list;
    }

    public Double getTotalPrice(String key) {
        List<Dish> dishes = getDishes(key);
        Double result = 0.0;
        for (Dish dish : dishes) {
            result += dish.getPrice();
        }
        return result;
    }

}
