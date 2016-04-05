package dev.blunch.blunch.utils;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

/**
 * Repository abstract class
 *
 * @param <T> repository type
 * @author albert
 */
public abstract class FirebaseRepository<T extends Entity> implements Repository <T> {

    protected final String FIREBASE_URI = "https://blunch.firebaseio.com/";
    private static final String TAG = FirebaseRepository.class.getSimpleName();
    protected Firebase firebase;

    /**
     * Constructor class
     */
    public FirebaseRepository(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(FIREBASE_URI);
    }

    /**
     * Add value Event listener for real time db
     * @param listener value eventlistener
     */
    public void addValueEventListener(ValueEventListener listener) {
        firebase.child(getObjectReference()).addValueEventListener(listener);
    }

    /**
     * Insert some object in specific repository with random id.
     * @param item Object that you want to insert.
     */
    @Override
    public T insert(T item) {
        Firebase ref = firebase.child(getObjectReference()).push();
        ref.setValue(item);
        item.setId(ref.getKey());
        return item;
    }

    /**
     * Insert some object in specific repository with specific id.
     * @param t   Object that you want to insert.
     * @param key Key that new object will have.
     */
    public void insertWithId(T t, String key) {
        firebase.child(getObjectReference()).child(key).setValue(t);
    }

    /**
     * Delete some object of this repository by id.
     * @param id Object key that you want to delete.
     */
    public void delete(String id) {
        Firebase mRef = firebase.child(getObjectReference());
        mRef.child(id).removeValue();
    }

    /**
     * Delete a relation of an object of this repository.
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