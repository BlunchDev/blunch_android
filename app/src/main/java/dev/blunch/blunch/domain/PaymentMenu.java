package dev.blunch.blunch.domain;

import android.util.Pair;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmotger on 5/04/16.
 */
public class PaymentMenu extends Menu {

    //A list of the dishes and the respective prices
    private List<Pair<Dish, Double>> dishes;

    public PaymentMenu() {}

    public PaymentMenu(String id, String name, String author, String description, String localization,
                             Date dateStart, Date dateEnd, List<Pair<Dish, Double>> dishes) {
        super(name, author, description, localization, dateStart, dateEnd);

        this.dishes = dishes;
    }

    public List<Pair<Dish, Double>> getDishes() {
        return dishes;
    }

    public void setDishes(List<Pair<Dish, Double>> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish dish, Double price) {
        this.dishes.add(new Pair<Dish, Double>(dish, price));
    }

}
