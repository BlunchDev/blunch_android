package dev.blunch.blunch.utils;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Repository abstract class
 *
 * @param <T> repository type
 * @author albert
 */
public abstract class FirebaseRepository<T extends Entity> implements Repository<T>, ChildEventListener {

    private OnChangedListener listener;

    public interface OnChangedListener {
        enum EventType {Added, Changed, Removed, Moved}

        void onChanged(EventType type);
    }

    protected final String FIREBASE_URI = "https://blunch.firebaseio.com/";
    private static final String TAG = FirebaseRepository.class.getSimpleName();
    private final HashMap<String, T> map;
    protected Firebase firebase;

    /**
     * Constructor class
     */
    public FirebaseRepository(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(FIREBASE_URI);
        map = new LinkedHashMap<>();

        firebase.addChildEventListener(this);
    }

    /**
     * Insert some object in specific repository with random id.
     *
     * @param item Object that you want to insert.
     */
    @Override
    public T insert(T item) {
        Firebase ref = firebase.child(getObjectReference()).push();
        ref.setValue(item);
        item.setId(ref.getKey());
        map.put(ref.getKey(), item);
        return item;
    }

    /**
     * Insert some object in specific repository with specific id.
     *
     * @param t   Object that you want to insert.
     * @param key Key that new object will have.
     */
    public void insertWithId(T t, String key) {
        firebase.child(getObjectReference()).child(key).setValue(t);
        map.put(key, t);
    }

    /**
     * Delete some object of this repository by id.
     *
     * @param id Object key that you want to delete.
     */
    public void delete(String id) {
        Firebase mRef = firebase.child(getObjectReference());
        mRef.child(id).removeValue();
    }

    /**
     * Delete a relation of an object of this repository.
     *
     * @param idParent   Key of parent node.
     * @param reference  Reference of the relation.
     * @param idToDelete Key of node that you want to delete.
     */
    public void delete(String idParent, String reference, String idToDelete) {
        Firebase mRef = firebase.child(getObjectReference());
        mRef.child(idParent).child(reference).child(idToDelete).removeValue();

    }

    /**
     * Convert a Firebase attribute to Domain attribute.
     *
     * @param data Firebase instance.
     * @return Domain instance.
     */
    public abstract T convert(DataSnapshot data);

    /**
     * Get object reference URI in Firebase.
     *
     * @return Respective reference URI of object.
     */
    public abstract String getObjectReference();

    public T get(String id) {
        return map.get(id);
    }

    public List<T> all() {
        return new ArrayList<>(map.values());
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        listener.onChanged(OnChangedListener.EventType.Added);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        listener.onChanged(OnChangedListener.EventType.Changed);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        map.remove(dataSnapshot.getKey());
        listener.onChanged(OnChangedListener.EventType.Removed);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        listener.onChanged(OnChangedListener.EventType.Moved);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.e(TAG, firebaseError.getMessage(), firebaseError.toException());
    }

    public void setOnChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }
}