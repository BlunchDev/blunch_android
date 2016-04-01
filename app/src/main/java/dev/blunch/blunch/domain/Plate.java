package dev.blunch.blunch.domain;

/**
 * Created by albert on 30/03/16.
 */
public class Plate {

    private String id;
    private String name;
    private double price;

    public Plate(String name, double price) {
        this.id = "";
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
