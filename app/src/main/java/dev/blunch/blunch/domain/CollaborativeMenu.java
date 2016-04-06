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
                             String localization, Date dateStart, Date dateEnd,
                             Set<String> offeredDishes, Set<String> suggestedDishes) {

        super(name, author, description, localization, dateStart, dateEnd);

        for (String dishKey : offeredDishes) {
            this.offeredDishes.put(dishKey, true);
        }
        for (String dishKey : suggestedDishes) {
            this.suggestedDishes.put(dishKey, true);
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

    public void setOfferedDishes(Set<String> offeredDishes) {
        this.offeredDishes = new LinkedHashMap<>();
        for (String dishKey : offeredDishes) {
            this.offeredDishes.put(dishKey, true);
        }
    }

    public void setSuggestedDishes(Set<String> suggestedDishes) {
        this.suggestedDishes = new LinkedHashMap<>();
        for (String dishKey : suggestedDishes) {
            this.suggestedDishes.put(dishKey, true);
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