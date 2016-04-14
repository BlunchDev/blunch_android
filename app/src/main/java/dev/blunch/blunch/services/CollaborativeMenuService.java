package dev.blunch.blunch.services;

import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by casassg on 07/04/16.
 * @author casassg
 */
public class CollaborativeMenuService extends Service<CollaborativeMenu> {

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
        this.dishesRepository = repoDishes;
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
        if (dishesRepository==null){
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        for(Dish dish : offeredDishes) {
            dishesRepository.insert(dish);
        }
        for (Dish dish : suggestedDishes) {
            dishesRepository.insert(dish);
        }
        repository.insert(item);
        return item;
    }
}
