package dev.blunch.blunch.domain;

import android.util.Pair;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Payment Menu Class
 * @author jmotger
 */
public class PaymentMenu extends Menu {

    private Map<String, Object>     dishes = new LinkedHashMap<>();

    public PaymentMenu() {}

    public PaymentMenu(String name, String author, String description, String localization,
                             Date dateStart, Date dateEnd, Set<String> dishes) {
        super(name, author, description, localization, dateStart, dateEnd);

        for (String dish : dishes) {
            this.dishes.put(dish, true);
        }

    }

    public Map<String, Object> getDishes() {
        return dishes;
    }

    public void setDishes(Map<String, Object> dishes) {
        this.dishes = dishes;
    }

    public void setDishes(Set<String> dishes) {
        for (String dish : dishes) {
            this.dishes.put(dish, true);
        }
    }

    public void addDish(String dish) {
        this.dishes.put(dish, true);
    }

    public void removeDish(String dish) {
        this.dishes.remove(dish);
    }

    public boolean containsDish(String dish) {
        return this.dishes.containsKey(dish);
    }

}
