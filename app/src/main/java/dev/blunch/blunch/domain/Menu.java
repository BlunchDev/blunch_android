package dev.blunch.blunch.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.utils.Entity;

/**
 * Menu class
 * @author jmotger
 */
public abstract class Menu implements Entity {

    private String              id;
    private String              name;
    private String              author;
    private String              description;
    private String              localization;
    private Date                dateStart;
    private Date                dateEnd;
    private String              dietTags;

    public Menu() {}

    public Menu(String name, String author, String description, String localization,
                Date dateStart, Date dateEnd) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.localization = localization;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dietTags = "";
    }

    public String getDietTagsString() { return dietTags; }

    public void setDietTagsString(String dietTags) { this.dietTags = dietTags; }

    public void addDietTags(List<DietTags> dietTags) {
        for (DietTags dietTag : dietTags) {
            if (dietTag.equals("")) this.dietTags += dietTag.toString();
            else this.dietTags += "&" + dietTag.toString();
        }
    }

    public List<DietTags> getDietTags() {
        String[] stringDietTags = this.dietTags.split("&");
        List<DietTags> dietTags = new ArrayList<>();
        for (String s : stringDietTags) {
            dietTags.add(DietTags.valueOf(s));
        }
        return dietTags;
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
