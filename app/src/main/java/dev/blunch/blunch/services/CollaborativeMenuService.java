package dev.blunch.blunch.services;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
public class CollaborativeMenuService extends Service<CollaborativeMenu> {


    public CollaborativeMenuService(Repository<CollaborativeMenu> repository) {
        super(repository);
    }
}
