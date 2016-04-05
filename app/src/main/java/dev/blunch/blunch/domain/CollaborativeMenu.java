package dev.blunch.blunch.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmotger on 5/04/16.
 */
public class CollaborativeMenu extends Menu {

    //A list of the dishes keys offered by the guest
    private List<Dish> offeredDishes;
    //A list of the suggested dishes keys by the guest
    private List<Dish> suggestedDishes;

    public CollaborativeMenu() {}

    public CollaborativeMenu(String id, String name, String author, String description,
                             String localization, Date dateStart, Date dateEnd,
                             List<Dish> offeredDishes, List<Dish> suggestedDishes) {

        super(name, author, description, localization, dateStart, dateEnd);

        this.offeredDishes = offeredDishes;
        this.suggestedDishes = suggestedDishes;

    }

    public List<Dish> getOfferedDishes() {
        return offeredDishes;
    }

    public void setOfferedDishes(List<Dish> offeredDishes) {
        this.offeredDishes = offeredDishes;
    }

    public List<Dish> getSuggestedDishes() {
        return suggestedDishes;
    }

    public void setSuggestedDishes(List<Dish> suggestedDishes) {
        this.suggestedDishes = suggestedDishes;
    }

    public void addSuggestedDish(Dish dish) {
        this.suggestedDishes.add(dish);
    }

    public void addOfferedDish(Dish dish) {
        this.offeredDishes.add(dish);
    }

}