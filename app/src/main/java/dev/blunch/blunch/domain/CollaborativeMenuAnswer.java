package dev.blunch.blunch.domain;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by jmotger on 6/04/16.
 */
public class CollaborativeMenuAnswer implements Entity {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
