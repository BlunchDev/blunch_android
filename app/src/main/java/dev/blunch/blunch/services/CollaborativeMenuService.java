package dev.blunch.blunch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
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

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository, Repository<Dish> repoDishes,
                                    Repository<CollaborativeMenuAnswer> collaborativeMenuAnswerRepository) {
        super(repository);
        this.dishesRepository = repoDishes;
        this.collaborativeMenuAnswerRepository = collaborativeMenuAnswerRepository;
    }

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository, Repository<Dish> repoDishes) {
        super(repository);
        dishesRepository = repoDishes;
        this.collaborativeMenuAnswerRepository = null;
    }

    public CollaborativeMenuService(Repository<CollaborativeMenu> repository) {
        super(repository);
        dishesRepository = null;
        collaborativeMenuAnswerRepository = null;
    }

    @Override
    public CollaborativeMenu save(CollaborativeMenu item) {
        return super.save(item);
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
        return repository.insert(item);
    }

    public CollaborativeMenuAnswer reply(CollaborativeMenuAnswer collaborativeMenuAnswer,
                                         List<Dish> newOfferedDishes) throws Exception {
        if (!repository.exists(collaborativeMenuAnswer.getMenuId())) throw new Exception("El menu seleccionat no existeix");
        else if (collaborativeMenuAnswer.getOfferedDishes().size() == 0 && newOfferedDishes.size() == 0)
            throw new Exception("No s'han afegit plats a l'oferta de participació");
        for (Dish dish : newOfferedDishes) {
            Dish d = dishesRepository.insert(dish);
            collaborativeMenuAnswer.addOfferedDish(d);
        }
        return collaborativeMenuAnswerRepository.insert(collaborativeMenuAnswer);
    }

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
        for (Dish dish : newOfferedDishes) {
            Dish d = dishesRepository.insert(dish);
            collaborativeMenuAnswer.addOfferedDish(d.getName());
        }
        return collaborativeMenuAnswerRepository.insert(collaborativeMenuAnswer);
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
        }
        repository.update(menuHost);
        collaborativeMenuAnswerRepository.delete(key);
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

}
