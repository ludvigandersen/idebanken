package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.email.Email;
import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PersonDbRepository implements IPersonDbRepository {

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    Email email = new Email();

    public PersonDbRepository() throws MessagingException {
    }

    @Override
    public void createPerson(Person person) {
        int roleId = getRoleId(person.getRole());
        boolean emailNot = false;


            String sql = "INSERT INTO Person(person_id, first_name, last_name, email, zip_code, city, password, role_id, email_notifications, date)"+
                    "VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            jdbc.update(sql, preparedStatement -> {
                preparedStatement.setString(1, person.getFirstName());
                preparedStatement.setString(2, person.getLastName());
                preparedStatement.setString(3, person.getEmail());
                preparedStatement.setInt(4, person.getZipCode());
                preparedStatement.setString(5, person.getCity());
                preparedStatement.setString(6, person.getPassword());
                preparedStatement.setInt(7, roleId);
                preparedStatement.setBoolean(8, emailNot);
                preparedStatement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            });
        email.emailCreatePerson(person);
    }

    @Override
    public void updatePerson(Person person, int id) {

    }

    @Override
    public void deletePerson(int id) {



    }

    @Override
    public List<Person> getAllPersons() {

        List<Person> person = new ArrayList<>();
        String sql = "SELECT first_name, last_name, email, city FROM idebanken.Person WHERE role_id = 1";
        sqlRowSet = jdbc.queryForRowSet(sql);

        while (sqlRowSet.next()){
            person.add(new Person(
                    sqlRowSet.getString("first_name"),
                    sqlRowSet.getString("last_name"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("city"))
                    );
        }

        return person;
    }

    @Override
    public Person getPerson(int id) {
        String sql = "SELECT * FROM idebanken.Person WHERE person_id=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);

        while (sqlRowSet.next()){
            return new Person(
                    sqlRowSet.getString("first_name"),
                    sqlRowSet.getString("last_name"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("city")
            );
        }
        return null;
    }

    @Override
    public Person getPerson(String email) {
        String sql = "SELECT * FROM idebanken.Person WHERE email=?";
        sqlRowSet = jdbc.queryForRowSet(sql, email);

        while (sqlRowSet.next()){
            return new Person(
                    sqlRowSet.getString("first_name"),
                    sqlRowSet.getString("last_name"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("city")
            );
        }
        return null;
    }

    @Override
    public int getPersonId(String email) {
        String sql = "SELECT person_id FROM idebanken.Person WHERE email=?";
        sqlRowSet = jdbc.queryForRowSet(sql, email);

        while (sqlRowSet.next()){
                    return sqlRowSet.getInt("person_id");
        }
        return 0;
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
