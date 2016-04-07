package dev.blunch.blunch.utils;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
public class MockRepository<T extends Entity> extends Repository<T> {

    private HashMap<String,T> map;
    private Integer count = 0;

    public MockRepository() {
        map = new HashMap<>();
    }

    private String nextId() {
        return String.valueOf(++count);
    }

    @Override
    public T insert(T item) {
        String id = nextId();
        item.setId(id);
        map.put(id,item);
        return item;
    }

    @Override
    public T update(T item) {
        String id = item.getId();
        map.put(id, item);
        return item;
    }

    @Override
    public void delete(String id) {
        map.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return map.keySet().contains(id);
    }

    @Override
    public T get(String id) {
        return map.get(id);
    }

    @Override
    public List<T> all() {
        return new ArrayList<>(map.values());
    }

    @Override
    public T convert(DataSnapshot data) {
        return (T) data.getPriority();
    }

    private DataSnapshot snap(T item){
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        when(mockSnapshot.getPriority()).thenReturn(item);
        return mockSnapshot;
    }

    public void simulateExternalAddition(T item){
        onChildAdded(snap(item), "NON");
    }

    public void simulateExternalChange(T item) {
        onChildChanged(snap(item), "NON");
    }

    public void simulateExternalDelete(T item) {
        onChildRemoved(snap(item));
    }

    public void simulateExternalMove(T item) {
        onChildMoved(snap(item),"NON");
    }


    @Override
    public void onCancelled(FirebaseError firebaseError) {
        System.err.println("Pos no va");
    }
}
