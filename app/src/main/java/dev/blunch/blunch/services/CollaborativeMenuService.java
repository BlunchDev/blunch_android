package dev.blunch.blunch.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.DietTags;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
public class CollaborativeMenuService extends Service<CollaborativeMenu> {

    private static final String TAG = CollaborativeMenuService.class.getSimpleName();
    private final Repository<Dish> dishesRepository;
    private final Repository<CollaborativeMenuAnswer> collaborativeMenuAnswerRepository;
    private final Repository<User> userRepository;
    private int loaded = 0;
    private int loadNeed = 1;

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository, Repository<Dish> repoDishes,
                                    Repository<CollaborativeMenuAnswer> collaborativeMenuAnswerRepository,
                                    Repository<User> userRepository) {
        super(repository);
        this.dishesRepository = repoDishes;
        this.collaborativeMenuAnswerRepository = collaborativeMenuAnswerRepository;
        this.userRepository = userRepository;
    }

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository, Repository<Dish> repoDishes) {
        super(repository);
        dishesRepository = repoDishes;
        this.collaborativeMenuAnswerRepository = null;
        this.userRepository = null;
    }

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository) {
        super(repository);
        dishesRepository = null;
        collaborativeMenuAnswerRepository = null;
        this.userRepository = null;
    }

    @Override
    public CollaborativeMenu save(CollaborativeMenu item) {
        CollaborativeMenu menu = super.save(item);
        if (userRepository.exists(item.getAuthor())) {
            User user = userRepository.get(item.getAuthor());
            user.addNewMyMenu(menu);
            userRepository.update(user);
        }
        return menu;
    }

    public CollaborativeMenu save(CollaborativeMenu item, List<Dish> offeredDishes, List<Dish> suggestedDishes) {
        if (dishesRepository == null) {
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        for (Dish dish : offeredDishes) {
            dishesRepository.insert(dish);
            item.addOfferedDish(dish.getId());
        }
        for (Dish dish : suggestedDishes) {
            dishesRepository.insert(dish);
            item.addSuggestedDish(dish.getId());
        }
        CollaborativeMenu menu = repository.insert(item);
        Log.d("Menu", menu.getId());
        Log.d("User", item.getAuthor());
        Log.d("Size", userRepository.all().size()+"");
        if (userRepository.exists(item.getAuthor())) {
            User user = userRepository.get(item.getAuthor());
            user.addNewMyMenu(menu);
            userRepository.update(user);
        }
        return menu;
    }

    public CollaborativeMenu addTags(CollaborativeMenu item, List<DietTags> dietTags) {
        item.addDietTags(dietTags);
        return repository.update(item);
    }

    public List<User> getUsers() { return userRepository.all(); }

    public User findUserByEmail(String email) {
        return userRepository.get(email);
    }


    /**
     *
     * Instance of collaborativeMenuAnswer contains dishes offered by the guest that
     * have been accepted by the host
     * newOfferedDishes is a list with new dishes offered by the host that needs to be
     * added in the repository
     */
    public CollaborativeMenuAnswer reply(CollaborativeMenuAnswer collaborativeMenuAnswer,
                                         List<Dish> newOfferedDishes) throws Exception {
        List<CollaborativeMenu> collaborativeMenus = repository.all();
        boolean exists = false;
        for (CollaborativeMenu menu : collaborativeMenus) {
            if (menu.getId().equals(collaborativeMenuAnswer.getMenuId())) exists = true;
        }
        if (!exists) throw new Exception("El menu seleccionat no existeix");
        else if (collaborativeMenuAnswer.getOfferedDishes().size() == 0 && newOfferedDishes.size() == 0)
            throw new Exception("No s'han afegit plats a l'oferta de participació");
        for (String dishKey : collaborativeMenuAnswer.getOfferedDishes().keySet()) {
            Dish dish = dishesRepository.get(dishKey);
            dish.setAuthor(Preferences.getCurrentUserEmail());
            dishesRepository.update(dish);
        }
        for (Dish dish : newOfferedDishes) {
            Dish d = dishesRepository.insert(dish);
            collaborativeMenuAnswer.addOfferedDish(d.getId());
        }
        CollaborativeMenuAnswer answer = collaborativeMenuAnswerRepository.insert(collaborativeMenuAnswer);

        return answer;
    }

    public List<Dish> getSuggestedDishes(String key) {
        List<Dish> list = new ArrayList<>();
        CollaborativeMenu collaborativeMenu = get(key);
        for (String k : collaborativeMenu.getSuggestedDishes().keySet()) {
            list.add(dishesRepository.get(k));
        }
        return list;
    }

    public List<Dish> getOfferedDishes(String key) {
        List<Dish> list = new ArrayList<>();
        CollaborativeMenu collaborativeMenu = get(key);
        for (String k : collaborativeMenu.getOfferedDishes().keySet()) {
            list.add(dishesRepository.get(k));
        }
        return list;
    }

    public List<CollaborativeMenuAnswer> getProposal(String key) {
        List<CollaborativeMenuAnswer> list = new ArrayList<>();
        List<CollaborativeMenuAnswer> all = collaborativeMenuAnswerRepository.all();
        if (all == null) {
            return list;
        }


        for (CollaborativeMenuAnswer answer : all) {
            String menuId = answer.getMenuId();
            if (key.equals(menuId)) list.add(answer);
        }
        return list;
    }

    public void acceptProposal(String key) {
        CollaborativeMenuAnswer answer = collaborativeMenuAnswerRepository.get(key);
        CollaborativeMenu menuHost = repository.get(answer.getMenuId());
        for (Map.Entry<String, Object> entry : answer.getOfferedDishes().entrySet()) {
            menuHost.addOfferedDish(entry.getKey());
            if(menuHost.containsSuggestedDish(entry.getKey())) menuHost.removeSuggestedDish(entry.getKey());
        }
        repository.update(menuHost);
        collaborativeMenuAnswerRepository.delete(key);
        if (userRepository.exists(answer.getGuest())) {
            User user = userRepository.get(answer.getGuest());
            user.addNewParticipatedMenu(repository.get(answer.getMenuId()));
            user.resetMessageCount(answer.getMenuId());
            userRepository.update(user);
        }
    }

    public void declineProposal(String key) {
        collaborativeMenuAnswerRepository.delete(key);
    }


    public void setCollaborativeMenuAnswerListener(Repository.OnChangedListener listener) {
        if (collaborativeMenuAnswerRepository != null) {
            collaborativeMenuAnswerRepository.setOnChangedListener(listener);
        }
    }

    public Dish getDish(String key) {
        return dishesRepository.get(key);
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
            loadNeed+=1;
            dishesRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
                }
            });
        }

        if (collaborativeMenuAnswerRepository != null) {
            loadNeed+=1;
            collaborativeMenuAnswerRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
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

    public boolean imHost(String idMenu) {
        return findUserByEmail(Preferences.getCurrentUserEmail()).imHost(idMenu);
    }

    public boolean imGuest(String idMenu) {
        return findUserByEmail(Preferences.getCurrentUserEmail()).imGuest(idMenu);
    }

    public Collection<Dish> getMySuggestedDishes(String idMenu) {
        List<Dish> d = new ArrayList<>();
        for(String idDish: get(idMenu).getOfferedDishes().keySet()){
            d.add(dishesRepository.get(idDish));
        }
        return d;
    }

    public void resetMessageCountToActualUser(String id) {
        User user = findUserByEmail(Preferences.getCurrentUserEmail());
        user.resetMessageCount(id);
        userRepository.update(user);
    }
}
