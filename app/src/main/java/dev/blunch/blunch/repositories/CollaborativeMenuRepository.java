package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
public class CollaborativeMenuRepository extends FirebaseRepository<CollaborativeMenu> {

    public CollaborativeMenuRepository(Context context) { super(context);}

    @Override
    public CollaborativeMenu convert(DataSnapshot data) {
        CollaborativeMenu collaborativeMenu = new CollaborativeMenu();
        collaborativeMenu.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                collaborativeMenu.setName(d.getValue().toString());
            } else if (d.getKey().equals("author")) {
                collaborativeMenu.setAuthor(d.getValue().toString());
            } else if (d.getKey().equals("description")) {
                collaborativeMenu.setDescription(d.getValue().toString());
            } else if (d.getKey().equals("localization")) {
                collaborativeMenu.setLocalization(d.getValue().toString());
            } else if (d.getKey().equals("dateStart")) {
                collaborativeMenu.setDateStart(new Date(Long.parseLong(d.getValue().toString())));
            } else if (d.getKey().equals("dateEnd")) {
                collaborativeMenu.setDateEnd(new Date(Long.parseLong(d.getValue().toString())));
            } else if (d.getKey().equals("offeredDishes")) {
                Set<String> dishes = new HashSet<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getValue().toString());
                }
                collaborativeMenu.setOfferedDishes(dishes);
            } else if (d.getKey().equals("suggestedDishes")) {
                Set<String> dishes = new HashSet<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getValue().toString());
                }
                collaborativeMenu.setSuggestedDishes(dishes);
            }
        }
        return collaborativeMenu;
    }

    @Override
    public String getObjectReference() {
        return "collaborativeMenu";
    }
}
