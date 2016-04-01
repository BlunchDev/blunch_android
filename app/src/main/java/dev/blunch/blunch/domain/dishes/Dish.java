package dev.blunch.blunch.domain.dishes;

/**
 * Dish super class
 * @author albert
 */
public class Dish {

    private String id;
    private String name;

    public Dish(String name) {
        this.id = "";
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
