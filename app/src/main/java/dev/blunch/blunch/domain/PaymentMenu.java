package dev.blunch.blunch.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jmotger on 5/04/16.
 */
public class PaymentMenu extends Menu {

    private Map<String, Object> dishes = new LinkedHashMap<>();
    //private DishRepository dr;
    public PaymentMenu() {}

    public PaymentMenu(String name, String author, String description, String localization,
                             Date dateStart, Date dateEnd, Set<String> dishes) {
        super(name, author, description, localization, dateStart, dateEnd);
        if (dishes != null) {
            for (String dish : dishes) {
                this.dishes.put(dish, true);
            }
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

   /* private float totalPrice(Set<String> dishes ) {
        if(dishes == null)
            throw new IllegalArgumentException("Debe existir por lo menos un plato");
        float sum = 0;
        while (dishes.iterator().hasNext()) {
            sum += dr.get(dishes.iterator().next()).getPrice();
        }
        return sum;
    }*/
}
