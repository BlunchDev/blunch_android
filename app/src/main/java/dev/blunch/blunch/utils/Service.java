package dev.blunch.blunch.utils;

import java.util.List;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
public abstract class Service<T extends Entity> {

    protected final Repository<T> repository;

    public Service (Repository<T> repository){
        this.repository = repository;
    }

    public T save(T item) {
        if (repository.exists(item.getId())) return repository.update(item);
        return repository.insert(item);
    }

    public T get(String key) {
        return repository.get(key);
    }

    public void delete(String key){
        repository.delete(key);
    }

    public int getAmount(){
        return repository.all().size();
    }

    public List<T> getAll() {
        return repository.all();
    }

    public void setOnChangedListener(Repository.OnChangedListener listener){
        repository.setOnChangedListener(listener);
    }

}
