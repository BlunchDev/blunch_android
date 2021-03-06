package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger on 3/05/16.
 */
@SuppressWarnings("all")
public class UserRepository extends FirebaseRepository<User> {

    public UserRepository(Context context) { super(context);}

    @Override
    protected User convert(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null) return null;

        User user = new User();
        user.setId(dataSnapshot.getKey());
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            if (d.getKey().equals("name")) {
                user.setName(d.getValue(String.class));
            } else if (d.getKey().equals("imageFile")) {
                user.setImageFile(d.getValue(String.class));
            } else if (d.getKey().equals("valorationNumber")) {
                user.setValorationNumber(d.getValue(Integer.class));
            } else if (d.getKey().equals("valorationAverage")) {
                user.setValorationAverage(d.getValue(Double.class));
            } else if (d.getKey().equals("myMenus")) {
                List<String> menus = new ArrayList<>();
                for (DataSnapshot menu : d.getChildren()) {
                    menus.add(menu.getKey());
                }
                user.setMyMenus(menus);
            } else if (d.getKey().equals("participatedMenus")) {
                List<String> menus = new ArrayList<>();
                for (DataSnapshot menu : d.getChildren()) {
                    menus.add(menu.getKey());
                }
                user.setParticipatedMenus(menus);
            } else if (d.getKey().equals("myChats")) {
                Map<String, Object> chats = new LinkedHashMap<>();
                for (DataSnapshot chat : d.getChildren()) {
                    chats.put(chat.getKey(), chat.getValue());
                }
                user.setMyChats(chats);
            }
        }
        return user;
    }

    @Override
    public String getObjectReference() {
        return "user";
    }

}
