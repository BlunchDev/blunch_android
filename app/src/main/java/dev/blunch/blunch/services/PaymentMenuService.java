package dev.blunch.blunch.services;

import java.util.ArrayList;
import java.util.List;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Payment Menu Service Class
 *
 * @author albert on 7/04/16.
 */
public class PaymentMenuService extends Service<PaymentMenu> {

    private final Repository<Dish> dishesRepository;
    private int loaded = 0;
    private int loadNeed = 1;
    private final Repository<PaymentMenuAnswer> answerRepository;

    public PaymentMenuService(Repository<PaymentMenu> repository) {
        super(repository);
        dishesRepository = null;
        answerRepository = null;
    }

    public PaymentMenuService(Repository<PaymentMenu> repository,Repository<Dish> dishRepository, Repository<PaymentMenuAnswer> answerRep) {
        super(repository);
        dishesRepository = dishRepository;
        answerRepository = answerRep;
    }

    public PaymentMenuService(Repository<PaymentMenu> repository, Repository<Dish> dishRepository) {
        super(repository);
        dishesRepository = dishRepository;
        answerRepository = null;
    }


    public PaymentMenu save(PaymentMenu item, List<Dish> dishes) {
        if (dishesRepository == null) {
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        for (Dish dish : dishes) {
            dishesRepository.insert(dish);
            item.addDish(dish.getId());
        }
        return repository.insert(item);
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

    public void answer(String menuKey, PaymentMenuAnswer answer) {
        answer.setIdMenu(menuKey);
        answerRepository.insert(answer);
    }

    public List<PaymentMenuAnswer> getAnswers(String menuKey){
        List<PaymentMenuAnswer> list = answerRepository.all();
        List<PaymentMenuAnswer> result = new ArrayList<>();
        for (PaymentMenuAnswer answer : list) {
            if (menuKey.equals(answer.getIdMenu())){
                result.add(answer);
            }
        }
        return result;
    }

    public List<Dish> getAnswerDishes(String answerKey) {
        PaymentMenuAnswer answer = answerRepository.get(answerKey);
        List<Dish> dishes = new ArrayList<>();
        for (String key : answer.getChoosenDishes().keySet()) {
            dishes.add(dishesRepository.get(key));
        }
        return dishes;
    }


    public void setPaymentMenuAnswerListener(Repository.OnChangedListener listener) {
        if (answerRepository != null) {
            answerRepository.setOnChangedListener(listener);
        }
    }

    @Override
    public void setOnChangedListener(final Repository.OnChangedListener listener) {
        repository.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                triggerListener(listener, type);
            }
        });
        if (dishesRepository != null) {
            loadNeed += 1;
            dishesRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
                }
            });
        }
        if (answerRepository!=null) {
            loadNeed+=1;
            answerRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener,type);
                }
            });
        }
    }

    private void triggerListener(Repository.OnChangedListener listener, Repository.OnChangedListener.EventType type) {
        if (type == Repository.OnChangedListener.EventType.Full) {
            loaded += 1;
        }
        if (loaded == loadNeed) {
            listener.onChanged(Repository.OnChangedListener.EventType.Full);
        } else {
            if (type == Repository.OnChangedListener.EventType.Full){
                type = Repository.OnChangedListener.EventType.Added;
            }
            listener.onChanged(type);
        }
    }
}
