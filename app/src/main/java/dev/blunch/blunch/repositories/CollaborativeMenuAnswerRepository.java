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
    protected CollaborativeMenuAnswer convert(DataSnapshot data) {
        CollaborativeMenuAnswer collaborativeMenuAnswer = new CollaborativeMenuAnswer();
        collaborativeMenuAnswer.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if (d.getKey().equals("guest")) {
                collaborativeMenuAnswer.setGuest(d.getValue(String.class));
            } else if (d.getKey().equals("date")) {
                collaborativeMenuAnswer.setDate(d.getValue(Date.class));
            } else if (d.getKey().equals("offeredDishes")) {
                List<String> dishes = new ArrayList<>();
                for (DataSnapshot dish : d.getChildren()) {
                    dishes.add(dish.getKey());
                }
                collaborativeMenuAnswer.setOfferedDishes(dishes);
            }
        }
        return collaborativeMenuAnswer;

    }

    @Override
    public String getObjectReference() {
        return "collaborativeMenuAnswer";
    }
}
