package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonDbRepository implements IPersonDbRepository {
    @Override
    public void createPerson(Person person) {

    }

    @Override
    public void updatePerson(Person person, int id) {

    }

    @Override
    public void deletePerson(int id) {

    }

    @Override
    public List<Person> getAllPersons() {
        return null;
    }

    @Override
    public Person getPerson(int id) {
        return null;
    }
}
