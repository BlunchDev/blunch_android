package dev.blunch.blunch.repositories;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

/**
 * Created by jmotger on 12/04/16.
 */
public class CollaborativeMenuAnswerRepositoryTest {

    @Test
    public void check_object_reference(){
        CollaborativeMenuAnswerRepository repository = new CollaborativeMenuAnswerRepository(RuntimeEnvironment.application);
        assertEquals(repository.getObjectReference(),"collaborativeMenuAnswer");
    }

}
