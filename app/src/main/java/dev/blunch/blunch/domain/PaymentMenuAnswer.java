package dev.blunch.blunch.domain;

import java.util.List;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by casassg on 22/04/16.
 *
 * @author casassg
 */
public class PaymentMenuAnswer implements Entity {

    private List<Dish> choosenDishes;
    private String idMenu;
    private String id;

    public PaymentMenuAnswer(List<Dish> choosenDishes, String idMenu) {
        this.choosenDishes = choosenDishes;
        this.idMenu = idMenu;
    }

    public PaymentMenuAnswer() {
    }

    public List<Dish> getChoosenDishes() {
        return choosenDishes;
    }

    public void setChoosenDishes(List<Dish> choosenDishes) {
        this.choosenDishes = choosenDishes;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
