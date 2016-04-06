package dev.blunch.blunch.domain;

import java.util.Date;

import dev.blunch.blunch.utils.Entity;

/**
 * Menu class
 * @author jmotger
 */
public abstract class Menu implements Entity {

    private String      id;
    private String      name;
    private String      author;
    private String      description;
    private String      localization;
    private Date        dateStart;
    private Date        dateEnd;

    public Menu() {}

    public Menu(String name, String author, String description, String localization,
                Date dateStart, Date dateEnd) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.localization = localization;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd= dateEnd;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
