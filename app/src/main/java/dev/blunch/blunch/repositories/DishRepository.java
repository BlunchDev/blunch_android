package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
@SuppressWarnings("all")
public class DishRepository extends FirebaseRepository<Dish> {

    public DishRepository(Context context) {
        super(context);
    }

    @Override
    protected Dish convert(DataSnapshot data) {
        if (data == null) return null;

        Dish dish = new Dish();
        dish.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                dish.setName(d.getValue(String.class));
            } else if (d.getKey().equals("price")) {
                dish.setPrice(d.getValue(Double.class));
            } else if (d.getKey().equals("author")) {
                dish.setAuthor(d.getValue(String.class));
            }
        }
        return dish;
    }

    @Override
    public String getObjectReference() {
        return "dish";
    }
}
