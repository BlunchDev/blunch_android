package dev.blunch.blunch;

import com.firebase.client.DataSnapshot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import java.lang.*;
import java.util.*;
import dev.blunch.blunch.utils.*;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsPackageTest {

    private PersonRepository personRepository = new PersonRepository();

    @Test
    public void generate_different_keys() throws Exception {
        String key1 = Utils.generateId();
        String key2 = Utils.generateId();
        assertFalse(key1.equals(key2));
    }

    @Test
    public void add_correctly_some_object_in_repository() throws Exception {
        Set<String> sons = new LinkedHashSet<>();
        sons.add("Pere");
        sons.add("Victor");
        Person personToAdd = new Person("Albert", 20, sons);
        personRepository.insert(personToAdd);
        Person personToFind = personRepository.findById(personToAdd.getId());
        assertEquals(personToAdd.getId(), personToFind.getId());
        assertEquals(personToAdd.getAge(), personToFind.getAge());
        Set<String> keys = personToAdd.getSons().keySet();
        for (String s : keys) {
            assertTrue(personToFind.getSons().containsKey(s));
        }
        personRepository.delete(personToAdd.getId());
    }

}

class PersonRepository extends Repository<Person> {

    public PersonRepository() {
        super();
    }

    @Override
    public Person convert(DataSnapshot data) {
        Person person = new Person();

        person.setId(data.getKey());
        for (DataSnapshot d : data.getChildren()) {

            if (d.getKey().equals("name")) {
                person.setName(d.getValue().toString());
            }
            else if (d.getKey().equals("age")) {
                person.setAge((Integer) d.getValue());
            }

            else {
                Set<String> keys = new LinkedHashSet<>();
                for (DataSnapshot dd : d.getChildren()) {
                    keys.add(dd.getKey());
                }
                person.setSon(keys);
            }
        }

        return person;
    }

    @Override
    public String getObjectReference() {
        return "people";
    }
}

class Person implements Entity {
    private String id;
    private String name;
    private Integer age;
    private Map<String, Object> sons = new LinkedHashMap<>();

    public Person() {

    }

    public Person(String name, Integer age, Set<String> sons) {
        this.name = name;
        this.age = age;
        for (String s : sons) this.sons.put(s, true);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Map<String, Object> getSons() {
        return sons;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSons(Map<String, Object> sons) {
        this.sons = sons;
    }

    public void setSon(Set<String> sonsKeys) {
        for (String s : sonsKeys) sons.put(s, true);
    }
}