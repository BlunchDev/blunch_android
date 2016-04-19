package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by jmotger
 */
public class CollaborativeMenuAnswerRepository extends FirebaseRepository<CollaborativeMenuAnswer> {

    public CollaborativeMenuAnswerRepository(Context context) {
        super(context);
    }

    @Override
    public CollaborativeMenuAnswer convert(DataSnapshot data) {
        CollaborativeMenuAnswer collaborativeMenuAnswer = new CollaborativeMenuAnswer();
        collaborativeMenuAnswer.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("guest".equals(d.getKey())) {
                collaborativeMenuAnswer.setGuest(d.getValue(String.class));
            } else if ("date".equals(d.getKey())) {
                collaborativeMenuAnswer.setDate(d.getValue(Date.class));
            } else if ("offeredDishes".equals(d.getKey())) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                collaborativeMenuAnswer.setOfferedDishes(dishes);
            }else if ("menuId".equals(d.getKey())) {
                collaborativeMenuAnswer.setMenuId(d.getValue(String.class));
            }
        }
        return collaborativeMenuAnswer;

    }

    @Override
    public String getObjectReference() {
        return "collaborativeMenuAnswer";
    }
}
