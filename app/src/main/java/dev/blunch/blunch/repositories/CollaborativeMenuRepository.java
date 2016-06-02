package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
@SuppressWarnings("all")
public class CollaborativeMenuRepository extends FirebaseRepository<CollaborativeMenu> {

    public CollaborativeMenuRepository(Context context) { super(context);}

    @Override
    protected CollaborativeMenu convert(DataSnapshot data) {
        if (data == null) return null;

        CollaborativeMenu collaborativeMenu = new CollaborativeMenu();
        collaborativeMenu.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                collaborativeMenu.setName(d.getValue(String.class));
            } else if (d.getKey().equals("author")) {
                collaborativeMenu.setAuthor(d.getValue(String.class));
            } else if (d.getKey().equals("description")) {
                collaborativeMenu.setDescription(d.getValue(String.class));
            } else if (d.getKey().equals("localization")) {
                collaborativeMenu.setLocalization(d.getValue(String.class));
            } else if (d.getKey().equals("dateStart")) {
                collaborativeMenu.setDateStart(d.getValue(Date.class));
            } else if (d.getKey().equals("dateEnd")) {
                collaborativeMenu.setDateEnd(d.getValue(Date.class));
            } else if (d.getKey().equals("offeredDishes")) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                collaborativeMenu.setOfferedDishesListKeys(dishes);
            } else if (d.getKey().equals("suggestedDishes")) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                collaborativeMenu.setSuggestedDishesKeys(dishes);
            } else if (d.getKey().equals("dietTags")) {
                collaborativeMenu.setDietTagsString(d.getValue(String.class));
            }
        }
        return collaborativeMenu;
    }

    @Override
    public String getObjectReference() {
        return "collaborativeMenu";
    }
}
