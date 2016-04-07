package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
public class DishRepository extends FirebaseRepository<Dish> {

    public DishRepository(Context context) {
        super(context);
    }

    @Override
    public Dish convert(DataSnapshot data) {
        Dish dish = new Dish();
        dish.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("name")) {
                dish.setName(d.getValue().toString());
            } else if (d.getKey().equals("price")) {
                dish.setPrice(Double.parseDouble(d.getValue().toString()));
            }
        }
        return dish;
    }

    @Override
    public String getObjectReference() {
        return "dish";
    }
}
