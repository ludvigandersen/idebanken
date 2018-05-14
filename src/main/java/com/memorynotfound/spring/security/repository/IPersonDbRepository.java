package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Person;

import java.util.List;

public interface IPersonDbRepository {
    void createPerson(Person person);
    void updatePerson(Person person, int id);
    void deletePerson(int id);
    List<Person> getAllPersons();
    Person getPerson(int id);
    boolean checkEmail(String email);
}