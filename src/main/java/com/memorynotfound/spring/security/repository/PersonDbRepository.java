package com.memorynotfound.spring.security.repository;

import com.memorynotfound.spring.security.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class PersonDbRepository implements IPersonDbRepository {

    private PreparedStatement preparedStatement;
    private Connection conn = null;

    public PersonDbRepository() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://idebanken.czb6si4xafah.eu-central-1.rds.amazonaws.com:3306/idebanken",
                                           "idebankenAdmin", "idebanken");
    }

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;


    @Override
    public void createPerson(Person person) {
        int roleId = getRoleId(person.getRole());
        boolean emailNot = false;
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO Person(person_id, first_name, last_name, email, zip_code, city, password, role_id, email_notifications, date) " +
                    "VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setInt(4, person.getZipCode());
            preparedStatement.setString(5, person.getCity());
            preparedStatement.setString(6, person.getPassword());
            preparedStatement.setInt(7, roleId);
            preparedStatement.setBoolean(8, emailNot);
            preparedStatement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.execute();

        }catch (SQLException e){
             e.printStackTrace();
        }

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
