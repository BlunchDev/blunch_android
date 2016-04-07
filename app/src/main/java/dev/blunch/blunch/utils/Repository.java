package dev.blunch.blunch.utils;

import java.util.List;

/**
 * Created by casassg on 04/04/16.
 * @author casassg
 */
public interface Repository<T extends Entity> {

    T insert(T item);

    T update(T item);

    void delete(String id);

    boolean exists(String id);

    T get(String id);

    List<T> all();
}
