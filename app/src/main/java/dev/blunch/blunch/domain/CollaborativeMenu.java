package dev.blunch.blunch.domain;

import java.util.List;

/**
 * Created by albert on 30/03/16.
 */
public class CollaborativeMenu {

    private String id;
    private String name;
    private String description;
    private String address;
    private List<Plate> plates;

    public CollaborativeMenu(String id, String name, String description, String address, List<Plate> plates) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.plates = plates;
    }
}
