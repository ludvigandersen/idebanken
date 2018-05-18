package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.email.Email;
import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public void updatePersonPassword(Person person, String oldPassword){

        if(checkPassword(oldPassword, person.getPersonId())) {
            String sql = "UPDATE idebanken.Person SET password = ? WHERE person_id = ?";

            jdbc.update(sql, preparedStatement -> {
                preparedStatement.setString(1, person.getPassword());
                preparedStatement.setInt(2, person.getPersonId());
            });
        }
    }

    private boolean checkPassword(String oldPassword, int personId){
        String sql = "SELECT * FROM idebanken.Person WHERE person_id = ?";
        sqlRowSet = jdbc.queryForRowSet(sql,personId);

        String password = "";
        while (sqlRowSet.next()){
                    password = sqlRowSet.getString("password");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(passwordEncoder.matches(oldPassword, password)){
            System.out.println("Pass check true");
            return true;
        }else{
            System.out.println("Pass check false");
            return false;
        }

    }

    @Override
    public void updatePerson(Person person, String oldTlf1, String oldTlf2) {
        String sql = "UPDATE Person SET  first_name = ?, last_name = ?," +
                " zip_code = ?, city = ? WHERE person_id = ?";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getZipCode());
            preparedStatement.setString(4, person.getCity());
            preparedStatement.setInt(5, person.getPersonId());

        });

        if(!person.getTlf1().equalsIgnoreCase("")){
            if(!person.getTlf1().equalsIgnoreCase(oldTlf2)) {
                if (oldTlf1.equalsIgnoreCase("")) {
                    sql = "INSERT INTO PhoneNumbers  (tlf_id, person_id, tlf) " +
                            "VALUES (DEFAULT, ?, ?)";

                    jdbc.update(sql, preparedStatement -> {
                        preparedStatement.setInt(1, person.getPersonId());
                        preparedStatement.setString(2, person.getTlf1());

                    });
                } else if (!oldTlf1.equalsIgnoreCase(person.getTlf1())) {
                    String sql1 = "UPDATE PhoneNumbers SET  tlf = ? WHERE person_id = ? AND tlf = ?";

                    jdbc.update(sql1, preparedStatement -> {
                        preparedStatement.setString(1, person.getTlf1());
                        preparedStatement.setInt(2, person.getPersonId());
                        preparedStatement.setString(3, oldTlf1);
                    });
                }
            }
        }

        if(!person.getTlf2().equalsIgnoreCase("")) {
            if (!person.getTlf2().equalsIgnoreCase(oldTlf1)) {
                if (oldTlf2.equalsIgnoreCase("")) {
                    sql = "INSERT INTO PhoneNumbers  (tlf_id, person_id tlf) " +
                            "VALUES (DEFAULT, ?, ?)";

                    jdbc.update(sql, preparedStatement -> {
                        preparedStatement.setInt(1, person.getPersonId());
                        preparedStatement.setString(2, person.getTlf2());

                    });
                } else if (!oldTlf2.equalsIgnoreCase(person.getTlf2())) {
                    sql = "UPDATE PhoneNumbers SET  tlf = ? WHERE person_id = ? AND tlf = ? ";

                    jdbc.update(sql, preparedStatement -> {
                        preparedStatement.setString(1, person.getTlf2());
                        preparedStatement.setInt(2, person.getPersonId());
                        preparedStatement.setString(3, oldTlf2);
                    });
                }
            }
        }

    }

    @Override
    public void deletePerson(int id) {
        jdbc.update("DELETE FROM Person WHERE person_id = " + id);
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
        Person person = new Person();


        while (sqlRowSet.next()){
            person.setPersonId(sqlRowSet.getInt("person_id"));
            person.setFirstName(sqlRowSet.getString("first_name"));
            person.setLastName(sqlRowSet.getString("last_name"));
            person.setEmail(sqlRowSet.getString("email"));
            person.setZipCode(sqlRowSet.getInt("zip_code"));
            person.setCity(sqlRowSet.getString("city"));
        }
        List<String> tlf = findPhoneNumbers(person.getPersonId());

        int counter = 1;

        for (String i: tlf){
            if(counter == 2){
                if(i.equalsIgnoreCase("") || i.equalsIgnoreCase(null)){
                    break;

                }else {
                    person.setTlf2(i);
                    counter++;
                }
            }
            if(counter == 1) {
                if (i.equalsIgnoreCase("") || i.equalsIgnoreCase(null)) {
                    break;

                } else {
                    person.setTlf1(i);
                    counter++;
                }
            }
        }
        return person;
    }

    private List<String> findPhoneNumbers(int id){
        List<String> tlf = new ArrayList<>();
        String sql = "SELECT tlf FROM idebanken.PhoneNumbers WHERE person_id=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);

        while (sqlRowSet.next()){
            tlf.add(sqlRowSet.getString("tlf"));
        }
        return tlf;

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
        String sql = "SELECT email FROM idebanken.Person";
        sqlRowSet = jdbc.queryForRowSet(sql);
        ArrayList<String> emails = new ArrayList<>();
        while (sqlRowSet.next()){
            emails.add(sqlRowSet.getString("email"));
        }

        if (emails.contains(email)){
            return false;
        } else {
            return true;
        }
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
