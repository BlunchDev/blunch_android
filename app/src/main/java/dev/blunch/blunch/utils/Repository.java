package dev.blunch.blunch.utils;

/**
 * Created by casassg on 04/04/16.
 *
 * @author casassg
 */
public interface Repository<T extends Entity> {
    T insert(T item);

    void delete(String id);
}
