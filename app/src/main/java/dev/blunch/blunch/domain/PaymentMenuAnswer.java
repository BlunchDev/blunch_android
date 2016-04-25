package dev.blunch.blunch.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by casassg on 22/04/16.
 *
 * @author casassg
 */
public class PaymentMenuAnswer implements Entity {

    private String id;
    private String menuId;
    private String guest;
    private Date date;
    private Map<String, Object> choosenDishes = new LinkedHashMap<>();

    public PaymentMenuAnswer(String idMenu, String guest, Date date, List<Dish> dishes) {
        this.menuId = idMenu;
        this.guest = guest;
        this.date = date;

        if(dishes!=null) {
            for (Dish dish : dishes) {
                this.choosenDishes.put(dish.getId(), true);
            }
        }
    }

    public PaymentMenuAnswer() {

    }

    public Map<String, Object> getChoosenDishes() {
        return choosenDishes;
    }

    public void setChoosenDishes(Map<String, Object> choosenDishes) {
        this.choosenDishes = choosenDishes;
    }

    public String getIdMenu() {
        return menuId;
    }

    public void setIdMenu(String idMenu) {
        this.menuId = idMenu;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setChoosenDishesKeys(List<String> dishes) {
        this.choosenDishes = new LinkedHashMap<>();
        for (String dishKey : dishes) {
            this.choosenDishes.put(dishKey, true);
        }    }

    public void setChoosenDishesList(List<Dish> dishes) {
        this.choosenDishes = new LinkedHashMap<>();
        for (Dish dish : dishes) {
            this.choosenDishes.put(dish.getId(), true);
        }    }

    public boolean containsOfferedDish(String dish) {
        return this.choosenDishes.containsKey(dish);
    }

    public void addOfferedDish(String dish) {
        this.choosenDishes.put(dish, true);
    }

    public void addOfferedDish(Dish dish) { this.choosenDishes.put(dish.getId(), true); }

    public void removeOfferedDish(String dish) {
        this.choosenDishes.remove(dish);
    }
}
