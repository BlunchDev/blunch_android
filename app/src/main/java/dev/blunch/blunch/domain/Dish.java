package dev.blunch.blunch.domain;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by jmotger on 5/04/16.
 */
public class Dish implements Entity {

    private String id;
    private String name;
    private Double price;

    public Dish() {}

    public Dish (String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
