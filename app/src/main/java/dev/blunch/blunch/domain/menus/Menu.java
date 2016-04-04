package dev.blunch.blunch.domain.menus;

import dev.blunch.blunch.utils.Entity;

/**
 * Menu abstract class
 * @author albert
 */
public abstract class Menu implements Entity {

    private String id;
    private String name;
    private String description;
    private String address;

    public Menu() {

    }

    public Menu(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public abstract String toString();

}
