package dev.blunch.blunch.utils;

import com.firebase.client.Firebase;
import java.util.LinkedList;
import java.util.List;

public abstract class Repository<T extends Entity> {

    private final String FIREBASE_URI = "https://blunch.firebaseio.com/";

    public abstract T convert(Object o);

    public abstract String getObjectReference();

    public void insert(T t) {
        new Firebase(FIREBASE_URI).child(getObjectReference()).push().setValue(t);
    }

    public List<T> list() {
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        List<T> list = new LinkedList<>();
        for (Object o : mRef.getRepo().generateServerValues().values()) list.add(convert(o));
        return list;
    }

    public T findById(String id) {
        for (T t : list()) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    public void delete(String id) {
        T elem = findById(id);
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        mRef.child(id).removeValue();
    }
}
