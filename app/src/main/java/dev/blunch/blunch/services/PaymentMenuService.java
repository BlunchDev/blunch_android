package dev.blunch.blunch.services;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.PaymentMenuAnswer;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.Preferences;
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
    private final Repository<User> userRepository;

    public PaymentMenuService(Repository<PaymentMenu> repository) {
        super(repository);
        dishesRepository = null;
        answerRepository = null;
        this.userRepository = null;
    }

    public PaymentMenuService(Repository<PaymentMenu> repository,Repository<Dish> dishRepository,
                              Repository<PaymentMenuAnswer> answerRep, Repository<User> userRepository) {
        super(repository);
        dishesRepository = dishRepository;
        answerRepository = answerRep;
        this.userRepository = userRepository;
    }

    public PaymentMenuService(Repository<PaymentMenu> repository, Repository<Dish> dishRepository) {
        super(repository);
        dishesRepository = dishRepository;
        answerRepository = null;
        this.userRepository = null;
    }

    public List<User> getUsers() { return userRepository.all(); }

    public User findUserByEmail(String email) {
        return userRepository.get(email);
    }

    public PaymentMenu save(PaymentMenu item, List<Dish> dishes) {
        if (dishesRepository == null) {
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        for (Dish dish : dishes) {
            dishesRepository.insert(dish);
            item.addDish(dish.getId());
        }
        PaymentMenu menu =  repository.insert(item);
        if (userRepository.exists(menu.getAuthor())) {
            User user = userRepository.get(menu.getAuthor());
            user.addNewMyMenu(menu);
            userRepository.update(user);
        }
        return menu;
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
        PaymentMenuAnswer paymentMenuAnswer = answerRepository.insert(answer);
        if (userRepository.exists(paymentMenuAnswer.getGuest())) {
            User user = userRepository.get(paymentMenuAnswer.getGuest());
            user.addNewParticipatedMenu(repository.get(menuKey));
            userRepository.update(user);
        }
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
        if (userRepository != null) {
            loadNeed += 1;
            userRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
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

    public boolean imGuest(String idMenu) {
        return findUserByEmail(Preferences.getCurrentUserEmail()).imGuest(idMenu);
    }

    public boolean imHost(String idMenu) {
        return findUserByEmail(Preferences.getCurrentUserEmail()).imHost(idMenu);
    }

    public Collection<Dish> getMySelectedDishes(String idMenu) {
        Set<String> a = null;
        for(PaymentMenuAnswer m: answerRepository.all()){
            if(m.getGuest().equals(Preferences.getCurrentUserEmail()) && m.getIdMenu().equals(idMenu)){
                 a = m.getChoosenDishes().keySet();
            }
        }
        Collection<Dish> dishes = null;
        for(String d : a){
            dishes.add(dishesRepository.get(d));
        }
        return dishes;
    }


}
