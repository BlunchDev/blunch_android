package dev.blunch.blunch.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by jmotger on 6/04/16.
 */
public class CollaborativeMenuAnswer implements Entity {

    private String id;
    private String menuId;
    private String guest;
    private Date date;
    private Map<String, Object> offeredDishes = new LinkedHashMap<>();

    public CollaborativeMenuAnswer() {}

    public CollaborativeMenuAnswer(String guest, String menuId, Date date, List<String> offeredDishes) {
        this.guest = guest;
        this.date = date;
        this.menuId = menuId;

        if(offeredDishes!=null) {
            for (String dishKey : offeredDishes) {
                this.offeredDishes.put(dishKey, true);
            }
        }
    }

    public String getMenuId() { return menuId; }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public Map<String, Object> getOfferedDishes() {
        return offeredDishes;
    }

    public void setOfferedDishes(Map<String, Object> offeredDishes) {
        this.offeredDishes = offeredDishes;
    }

    public void setOfferedDishes(List<String> suggestedDishes) {
        this.offeredDishes = new LinkedHashMap<>();
        for (String dishKey : suggestedDishes) {
            this.offeredDishes.put(dishKey, true);
        }    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public boolean containsOfferedDish(String dish) {
        return this.offeredDishes.containsKey(dish);
    }

    public void addOfferedDish(String dish) {
        this.offeredDishes.put(dish, true);
    }

    public void removeOfferedDish(String dish) {
        this.offeredDishes.remove(dish);
    }
}
