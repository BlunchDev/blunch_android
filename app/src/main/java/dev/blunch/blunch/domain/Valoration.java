package dev.blunch.blunch.domain;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by pere on 5/3/16.
 */
public class Valoration implements Entity {

    private double points;
    private String comment;
    private Menu menu;
    private String host;
    private String guest;

    public Valoration(){

    }

    public Valoration(String host, String guest, Menu menu, double points, String comment){
        this.host = host;
        this.guest = guest;
        this.menu = menu;
        this.points = points;
        this.comment = comment;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }
}
