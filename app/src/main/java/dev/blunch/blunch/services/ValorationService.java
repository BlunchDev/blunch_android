package dev.blunch.blunch.services;

import dev.blunch.blunch.domain.Valoration;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by pere on 5/3/16.
 */
public class ValorationService  extends Service<Valoration> {

    private final Repository<Valoration> valorationrepository;

    public ValorationService(Repository<Valoration> repository, Repository<Valoration> valorationrepository) {
        super(repository);
        this.valorationrepository = valorationrepository;
    }

    public Valoration save(Valoration item) {
        if (item == null) {
            throw new UnsupportedOperationException("Service needs to be created with the ValorationRepository to function");
        }
        return repository.insert(item);
    }




}
