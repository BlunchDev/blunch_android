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

    /**
     * Constructor class
     */
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

    /**
     * Insert some object in specific repository with random id.
     * @param t Object that you want to insert.
     */
    public void insert(T t) {
        new Firebase(FIREBASE_URI).child(getObjectReference()).push().setValue(t);
    }

    /**
     * Insert some object in specific repository with specific id.
     * @param t Object that you want to insert.
     * @param key Key that new object will have.
     */
    public void insert(T t, String key) {
        new Firebase(FIREBASE_URI).child(getObjectReference()).child(key).setValue(t);
    }

    /**
     * List all objects of a specific repository.
     * @return A set that contains all object of this repository.
     */
    public Set<T> list() {
        return list;
    }

    /**
     * Find some object by id (key).
     * @param id Object id (key) that you want to find.
     * @return Object with id <code>id</code>
     */
    public T findById(String id) {
        for (T t : list()) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    /**
     * Delete some object of this repository by id.
     * @param id Object key that you want to delete.
     */
    public void delete(String id) {
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        mRef.child(id).removeValue();
    }

    /**
     * Delete a relation of an object of this repository.
     * @param idParent Key of parent node.
     * @param reference Reference of the relation.
     * @param idToDelete Key of node that you want to delete.
     */
    public void delete(String idParent, String reference, String idToDelete) {
        Firebase mRef = new Firebase(FIREBASE_URI).child(getObjectReference());
        mRef.child(idParent).child(reference).child(idToDelete).removeValue();
    }

    /**
     * Convert a Firebase attribute to Domain attribute.
     * @param data Firebase instance.
     * @return Domain instance.
     */
    public abstract T convert(DataSnapshot data);

    /**
     * Get object reference URI in Firebase.
     * @return Respective reference URI of object.
     */
    public abstract String getObjectReference();

}