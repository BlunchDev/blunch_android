package dev.blunch.blunch.utils.dummy;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by casassg on 04/04/16.
 *
 * @author casassg
 */
public class Person implements Entity {
    private String id;
    private String name;
    private Integer age;

    public Person() {

    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
