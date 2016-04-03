package dev.blunch.blunch.repository;

import com.firebase.client.DataSnapshot;

import java.util.LinkedHashSet;
import java.util.Set;

import dev.blunch.blunch.domain.dishes.CollaborativeDish;
import dev.blunch.blunch.utils.Repository;

/**
 * Collaborative Dishes Repository Class
 * @author albert
 */
public class CollaborativeDishesRepository extends Repository<CollaborativeDish> {

    public CollaborativeDishesRepository() {
        super();
    }

    @Override
    public CollaborativeDish convert(DataSnapshot data) {
        CollaborativeDish collaborativeDish = new CollaborativeDish();

        collaborativeDish.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {

            if (d.getKey().equals("name")) {
                collaborativeDish.setName(d.getValue().toString());
            }
            else if (d.getKey().equals("suggested")) {
                collaborativeDish.setSuggested((Boolean) d.getValue());
            }

            else {
                Set<String> keys = new LinkedHashSet<>();
                for (DataSnapshot dd : d.getChildren()) {
                    keys.add(dd.getKey());
                }
                collaborativeDish.setCollaborativeMenus(keys);
            }
        }

        return collaborativeDish;
    }

    @Override
    public String getObjectReference() {
        return "collaborativeDishes";
    }
}
