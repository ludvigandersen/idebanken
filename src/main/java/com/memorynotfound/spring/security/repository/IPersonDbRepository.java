package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Person;

import java.util.List;
/**
 * Dette er vores interface for PersonDbRepository
 * @author Mikkel Dalby Nielsen
 * @author Christoffer
 * @author Ludvig
 */

public interface IPersonDbRepository {
    void createPerson(Person person);
    void updatePerson(Person person, String oldTlf1, String oldTlf2);
    void updatePersonPassword(Person person, String gamlePassword);
    void deletePerson(int id);
    List<Person> getAllPersons();
    Person getPerson(int id);
    Person getPerson(String email);
    int getPersonId(String email);
    boolean checkEmail(String email);
}