package dev.blunch.blunch.domain;

import dev.blunch.blunch.utils.Entity;

/**
 * Dish Class
 * @author jmotger
 */
public class Dish implements Entity {

    private String  id;
    private String  name;
    private Double  price;

    public Dish() {}

    public Dish (String name, Double price) {
        this.name = name;
        if(price < 0)
            throw new IllegalArgumentException("El precio tiene que ser positivo");
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
