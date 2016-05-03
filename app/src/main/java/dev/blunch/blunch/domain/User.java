package dev.blunch.blunch.domain;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.firebase.client.utilities.Base64;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by jmotger on 3/05/16.
 */
@SuppressWarnings("all")
public class User implements Entity {

    private String id;
    private String name;
    private String email;
    private String imageFile;
    private Map<String, Object> myMenus;
    private Map<String, Object> participatedMenus;

    public User(){}

    public User(String name, String email, String imageFile) {
        this.name = name;
        this.email = email;
        this.imageFile = imageFile;
        myMenus = new LinkedHashMap<>();
        participatedMenus = new LinkedHashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Map<String, Object> getMyMenus() {
        return myMenus;
    }

    public void setMyMenus(List<String> myMenus) {
        this.myMenus = new LinkedHashMap<>();
        for (String s : myMenus)
            this.myMenus.put(s, true);
    }

    public Map<String, Object> getParticipatedMenus() {
        return participatedMenus;
    }

    public void setParticipatedMenus(List<String> participatedMenus) {
        this.participatedMenus = new LinkedHashMap<>();
        for (String s : participatedMenus)
            this.participatedMenus.put(s, true);
    }

    public void addNewMyMenu(Menu menu) {
        myMenus.put(menu.getId(), true);
    }

    public void addNewParticipatedMenu(Menu menu) {
        participatedMenus.put(menu.getId(), true);
    }

    public RoundedBitmapDrawable getImageRounded(Resources res) throws Exception{
        byte[] imageAsBytes = Base64.decode(imageFile.getBytes());
        Bitmap bp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, bp);
        dr.setCornerRadius(Math.max(bp.getWidth(), bp.getHeight()) / 2.0f);
        return dr;
    }
}
