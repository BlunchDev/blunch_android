package dev.blunch.blunch.repository;

import com.firebase.client.DataSnapshot;

import java.util.LinkedHashSet;
import java.util.Set;

import dev.blunch.blunch.domain.menus.CollaborativeMenu;
import dev.blunch.blunch.utils.Repository;

/**
 * Collaborative Menus Repository Class
 * @author albert 
 */
public class CollaborativeMenusRepository extends Repository<CollaborativeMenu> {

    @Override
    public CollaborativeMenu convert(DataSnapshot data) {
        CollaborativeMenu collaborativeMenu = new CollaborativeMenu();

        collaborativeMenu.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {

            if (d.getKey().equals("name")) {
                collaborativeMenu.setName(d.getValue().toString());
            }
            else if (d.getKey().equals("description")) {
                collaborativeMenu.setDescription(d.getValue().toString());
            }
            else if (d.getKey().equals("address")) {
                collaborativeMenu.setAddress(d.getValue().toString());
            }
            else {
                Set<String> keys = new LinkedHashSet<>();
                for (DataSnapshot dd : d.getChildren()) {
                    keys.add(dd.getKey());
                }
                collaborativeMenu.setCollaborativeDishes(keys);
            }
        }

        return collaborativeMenu;
    }

    @Override
    public String getObjectReference() {
        return "collaborativeMenus";
    }
}
