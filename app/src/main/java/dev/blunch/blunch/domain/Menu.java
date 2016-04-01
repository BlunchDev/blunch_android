package dev.blunch.blunch.domain;

import java.util.List;

import dev.blunch.blunch.domain.dishes.Dish;

/**
 * Menu class
 * @author albert
 */
public class Menu {

    private String id;
    private String name;
    private String description;
    private String address;
    private List<Dish> dishs;

    public Menu(String name, String description, String address, List<Dish> dishs) {
        this.id = "";
        this.name = name;
        this.description = description;
        this.address = address;
        this.dishs = dishs;
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

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public List<Dish> getDishs() {
        return dishs;
    }
}
