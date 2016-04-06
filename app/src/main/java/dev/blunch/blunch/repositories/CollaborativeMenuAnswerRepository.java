package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

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
        return null;
    }

    @Override
    public String getObjectReference() {
        return null;
    }
}
