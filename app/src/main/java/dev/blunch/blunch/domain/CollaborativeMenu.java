package dev.blunch.blunch.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collaborative Menu Class
 * @author jmotger
 */
public class CollaborativeMenu extends Menu {

    private Map<String, Object>     offeredDishes = new LinkedHashMap<>();
    private Map<String, Object>     suggestedDishes = new LinkedHashMap<>();

    public CollaborativeMenu() {}

    public CollaborativeMenu(String name, String author, String description,
                             String localization, Date dateStart, Date dateEnd) {

        super(name, author, description, localization, dateStart, dateEnd);

    }

    public CollaborativeMenu(String name, String author, String description,
                             String localization, Date dateStart, Date dateEnd,
                             List<Dish> offeredDishes, List<Dish> suggestedDishes) {

        super(name, author, description, localization, dateStart, dateEnd);

        if (offeredDishes != null) {
            for (Dish dish : offeredDishes) {
                this.offeredDishes.put(dish.getId(), true);
            }
        }
        if (suggestedDishes != null) {
            for (Dish dish : suggestedDishes) {
                this.suggestedDishes.put(dish.getId(), true);
            }
        }
    }

    public Map<String, Object> getOfferedDishes() {
        return offeredDishes;
    }

    public void setOfferedDishes(Map<String, Object> offeredDishes) {
        this.offeredDishes = offeredDishes;
    }

    public Map<String, Object> getSuggestedDishes() {
        return suggestedDishes;
    }

    public void setSuggestedDishes(Map<String, Object> suggestedDishes) {
        this.suggestedDishes = suggestedDishes;
    }

    public void setOfferedDishes(Set<String> inOfferedDishes) {
        offeredDishes = new LinkedHashMap<>();
        for (String dishKey : inOfferedDishes) {
            offeredDishes.put(dishKey,true);
        }
    }

    public void setOfferedDishesList(List<String> inOfferedDishes) {
        offeredDishes = new LinkedHashMap<>();
        for (String dishKey : inOfferedDishes) {
            offeredDishes.put(dishKey,true);
        }
    }

    public void setOfferedDishes(List<Dish> offeredDishes) {
        this.offeredDishes = new LinkedHashMap<>();
        for (Dish dish : offeredDishes) {
            this.offeredDishes.put(dish.getId(), true);
        }
    }

    public void setSuggestedDishesList(List<String> inSuggestedDishes) {
        suggestedDishes = new LinkedHashMap<>();
        for (String dishKey : inSuggestedDishes){
            suggestedDishes.put(dishKey,true);
        }
    }

    public void setSuggestedDishes(Set<String> inSuggestedDishes) {
        suggestedDishes = new LinkedHashMap<>();
        for (String dishKey : inSuggestedDishes){
            suggestedDishes.put(dishKey,true);
        }
    }

    public void setSuggestedDishes(List<Dish> suggestedDishes) {
        this.suggestedDishes = new LinkedHashMap<>();
        for (Dish dish : suggestedDishes) {
            this.suggestedDishes.put(dish.getId(), true);
        }    }

    public void addSuggestedDish(String dish) {
        this.suggestedDishes.put(dish, true);
    }

    public void addOfferedDish(String dish) {
        this.offeredDishes.put(dish, true);
    }

    public void removeSuggestedDish(String dish) {
        this.suggestedDishes.remove(dish);
    }

    public void removeOfferedDish(String dish) {
        this.offeredDishes.remove(dish);
    }

    public boolean containsOfferedDish(String dish) {
        return this.offeredDishes.containsKey(dish);
    }

    public boolean containsSuggestedDish(String dish) {
        return this.suggestedDishes.containsKey(dish);
    }

}