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
    private String  author;

    public Dish() {}

    public Dish (String name) {
        this.name = name;
        this.price = 0.;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
