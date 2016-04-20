package dev.blunch.blunch.utils.dummy;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by casassg on 04/04/16.
 *
 * @author casassg
 */
public class PersonRepository extends FirebaseRepository<Person> {

    public PersonRepository(Context context) {
        super(context);
    }

    @Override
    public Person convert(DataSnapshot child) {
        Person person = new Person();
        person.setId(child.getKey());
        for (DataSnapshot d : child.getChildren()) {
            if (d.getKey().equals("name")) {
                person.setName(d.getValue().toString());
            } else if (d.getKey().equals("age")) {
                person.setAge((Integer) d.getValue());
            }
        }
        return person;
    }

    @Override
    public String getObjectReference() {
        return "test";
    }

    public void close() {
        firebase.removeValue();
    }
}
