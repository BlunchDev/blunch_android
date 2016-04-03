package dev.blunch.blunch.utils;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Repository abstract class
 * @param <T> repository type
 * @author albert
 */
public abstract class Repository<T extends Entity> {

    private final String FIREBASE_URI = "https://blunch.firebaseio.com/";
    private Set<T> list;

    public Repository() {
        list = new LinkedHashSet<>();
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(convert(data));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("The read failed: " + firebaseError.getMessage());
            }
        });
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(convert(data));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(convert(data));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(convert(data));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(convert(data));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void insert(T t) {
        new Firebase(FIREBASE_URI).child(getObjectReference()).push().setValue(t);
    }

    public Set<T> list() {
        return list;
    }

    public T findById(String id) {
        for (T t : list()) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    public void delete(String id) {
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        mRef.child(id).removeValue();
    }

    public abstract T convert(DataSnapshot data);

    public abstract String getObjectReference();

}