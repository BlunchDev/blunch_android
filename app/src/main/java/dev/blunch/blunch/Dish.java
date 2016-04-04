package dev.blunch.blunch;

/**
 * Created by Daniela Santos on 01-04-2016.
 */
public class Dish {
    private String name;
    private float price;

    public Dish(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
