package dev.blunch.blunch.domain.dishes;

import dev.blunch.blunch.utils.Entity;

/**
 * Dish abstract class
 * @author albert
 */
public abstract class Dish implements Entity {

    private String id;
    private String name;

    public Dish() {

    }

    public Dish(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();
}
