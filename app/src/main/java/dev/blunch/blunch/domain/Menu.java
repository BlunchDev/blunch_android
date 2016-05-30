package dev.blunch.blunch.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String              dietTagsSingleString;

    public Menu() {}

    public Menu(String name, String author, String description, String localization,
                Date dateStart, Date dateEnd) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.localization = localization;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dietTagsSingleString = "";
    }

    public String getDietTags() { return dietTagsSingleString; }

    public void setDietTagsString(String dietTags) { this.dietTagsSingleString = dietTags; }

    public void addDietTags(List<DietTags> dietTags) {
        for (DietTags dietTag : dietTags) {
            if (this.dietTagsSingleString.equals("")) this.dietTagsSingleString += dietTag.toString();
            else this.dietTagsSingleString += "&" + dietTag.toString();
        }
    }

    public List<DietTags> retrievArrayOfTags() {
        if (!this.dietTagsSingleString.equals("") && this.dietTagsSingleString != null) {
            String[] stringDietTags = this.dietTagsSingleString.split("&");
            List<DietTags> dietTags = new ArrayList<>();
            for (String s : stringDietTags) {
                Log.d(s, s);
                dietTags.add(DietTags.valueOf(s));
            }
            return dietTags;
        } else return null;
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
