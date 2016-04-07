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
        t.setId(key);
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
        map.remove(id);
    }

    /**
     * Update specific item of repository
     * @param item item that you want to update
     */
    public void update(T item) {
        delete(item.getId());
        insertWithId(item, item.getId());
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

    /**
     * Get specific item of this repository
     * @param id key that identifies the item
     * @return Item that you want to get
     */
    public T get(String id) {
        return map.get(id);
    }

    /**
     * Get all repository
     * @return a list that contains all values of this repository
     */
    public List<T> all() {
        return new ArrayList<>(map.values());
    }

    /**
     * Listener that controls when a child is added
     * @param dataSnapshot data
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        notifyChange(OnChangedListener.EventType.Added);
    }

    /**
     * Listener that controls when a child is changed
     * @param dataSnapshot data
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        notifyChange(OnChangedListener.EventType.Changed);
    }

    /**
     * Listener that controls when a child is removed
     * @param dataSnapshot data
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        map.remove(dataSnapshot.getKey());
        notifyChange(OnChangedListener.EventType.Removed);
    }

    /**
     * Listener that controls when a child is moved
     * @param dataSnapshot data
     */
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        map.put(dataSnapshot.getKey(), convert(dataSnapshot));
        notifyChange(OnChangedListener.EventType.Moved);
    }

    private void notifyChange(OnChangedListener.EventType moved) {
        if (listener!=null){
            listener.onChanged(moved);
        }
    }

    /**
     * Listener that controls when it is occurred an error
     * @param firebaseError error
     */
    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.e(TAG, firebaseError.getMessage(), firebaseError.toException());
    }

    /**
     * Set Listener of Firebase reference
     * @param listener new listener to set
     */
    public void setOnChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }
}