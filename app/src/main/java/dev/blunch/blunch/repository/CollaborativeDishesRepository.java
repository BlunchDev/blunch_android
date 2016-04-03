package dev.blunch.blunch.repository;

import android.util.Log;

import java.util.Map;

import dev.blunch.blunch.domain.dishes.CollaborativeDish;
import dev.blunch.blunch.utils.Repository;

/**
 * Collaborative Dishes Repository Class
 * @author albert
 */
public class CollaborativeDishesRepository extends Repository<CollaborativeDish> {

    @Override
    public CollaborativeDish convert(Object o) {
        Long jaja = (Long) o;
        Log.d("JAJAJAJ", String.valueOf(jaja));
        /*Map<String, Object> map = (Map<String, Object>) o;
        CollaborativeDish dish = new CollaborativeDish();
        String id = (String) "1";
        dish.setId(id);
        String name = (String) map.get("name");
        dish.setName(name);
        Boolean suggested = (Boolean) map.get("suggested");
        dish.setSuggested(suggested);
        return dish;*/
        return null;
    }

    @Override
    public String getObjectReference() {
        return "collaborative_dishes";
    }
}
