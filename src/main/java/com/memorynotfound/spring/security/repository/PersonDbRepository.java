package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonDbRepository implements IPersonDbRepository {

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    @Override
    public void createPerson(Person person) {
        int roleId = getRoleId(person.getRole());
        boolean emailNot = false;
        jdbc.update("INSERT INTO Person (person_id, first_name, last_name, email, zip_code, " +
                        "city, password, role_id, email_notifications, date)" +
                        "VALUES (default, ?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        person.getFirstName(), person.getLastName(), person.getEmail(),
                        person.getZipCode(), person.getCity(), person.getPassword(), roleId, emailNot, person.getDate()
                });
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

    @Override
    public boolean checkEmail(String email) {
        return false;
    }

    private int getRoleId(String role){
        int roleId = 0;

        String sql = "SELECT role_id FROM PersonRole WHERE role = '" + role + "'";
        sqlRowSet = jdbc.queryForRowSet(sql);

        while (sqlRowSet.next()){
            roleId = sqlRowSet.getInt("role_id");
        }
        return roleId;
    }
}
