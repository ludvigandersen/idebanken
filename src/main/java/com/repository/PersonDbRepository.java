package com.repository;

import com.email.Email;
import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Christoffer
 * @author Ludvig
 * @author Mikkel
 */

@Repository
public class PersonDbRepository implements IPersonDbRepository {

    @Autowired
    JdbcTemplate jdbc;
    SqlRowSet sqlRowSet;

    Email email = new Email();

    public PersonDbRepository() throws MessagingException {
    }

    /**
     * Vi g&oslash;r brug af denne metode i sammenh&aelig;ng med createPerson, da city og zipCode ligger
     * i en separat tabel.
     */
    private void insertIntoCity(int zipCode, String city){
        String sql = "INSERT IGNORE INTO city(zip_code, city)"+
                "VALUES(?,?)";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, zipCode);
            preparedStatement.setString(2, city);
        });
    }

    /**
     * Her insender vi et statement til vores database for at f&aring; oprettet en bruger i vores system.
     * Vi g&oslash;r brug af preparedstatements for at g&oslash;re vores database mere sikker.
    */
    @Override
    public void createPerson(Person person) {
        int roleId = getRoleId(person.getRole());
        boolean emailNot = false;


            String sql = "INSERT INTO Person(person_id, first_name, last_name, email, zip_code, password, role_id, email_notifications, date)"+
                    "VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";

            jdbc.update(sql, preparedStatement -> {
                preparedStatement.setString(1, person.getFirstName());
                preparedStatement.setString(2, person.getLastName());
                preparedStatement.setString(3, person.getEmail());
                preparedStatement.setInt(4, person.getZipCode());
                preparedStatement.setString(5, person.getPassword());
                preparedStatement.setInt(6, roleId);
                preparedStatement.setBoolean(7, emailNot);
                preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            });

            insertIntoCity(person.getZipCode(), person.getCity());
        email.emailCreatePerson(person);
    }

    /**
     * Her modtager vi al den data brugeren har indtastet i form af en Person person og et gammelt password.
     * Vi tjekker om det gamle password matcher passwordet
     * indtastet i gammel password baren via. {@link #checkPassword(String, int)}
     * Vi bruger herefter en UPDATE sql statement til at opdatere id&eacute;ens data i databasen
    */
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

    /**
     * Her tjekker vi om det gamle password matcher passwordet ndtastet i gammel password baren
     */
    private boolean checkPassword(String oldPassword, int personId){
        String sql = "SELECT Person.password FROM idebanken.Person WHERE person_id = ?";
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

    /**
     * Her modtager vi al den data brugeren har indtastet i form af en Person person, olfTlf1 og oldTlf2
     * Som vi bruger i et UPDATE statement og opdatere brugeren i databasen
     * F&oslash;r vi kan inds&aelig;tte tlf nr. er vi n&oslash;dt til at tjekke om brugeren allerede har et tlf nr.
     * For at se om det er et UPDATE statement eller et INSERT statement der skal bruges.
     *
     * Derefter tjekker om tlf nummerne er ens, da det skaber problemmer hvis de er,
     * som f.eks. hvis der bliver indtastet noget i tlf1 men ikke tlf2, men de to gamle nr er ens, vil
     * den stadig &aelig;ndre dem begge
     */
    @Override
    public void updatePerson(Person person, String oldTlf1, String oldTlf2) {
        String sql = "UPDATE Person SET  first_name = ?, last_name = ?," +
                " zip_code = ? WHERE person_id = ?";

        jdbc.update(sql, preparedStatement -> {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getZipCode());
            preparedStatement.setInt(4, person.getPersonId());

        });
        insertIntoCity(person.getZipCode(), person.getCity());

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

    /**
     *Her g&oslash;r  vi brug af person_id fra databasen, til at finde og slette brugeren vha. et statement
     */
    @Override
    public void deletePerson(int id) {
        jdbc.update("DELETE FROM Person WHERE person_id = ?", id);
    }

    /**
     *Her henter vi alle de brugere i vores system, der har role_id = 1, alts&aring; alle developers.
     * De bliver alle sammen tilf&oslash;jet til en arraylist s&aring; vi kan putte det i en tabel p&aring; vores side.
     * Vi g&oslash;r brug af et inner join for ogs&aring; at kunne vise zip_code p&aring; siden.
     */
    @Override
    public List<Person> getAllPersons() {

        List<Person> person = new ArrayList<>();
        String sql = "SELECT * FROM idebanken.Person INNER JOIN city ON city.zip_code = Person.zip_code WHERE Person.role_id = 1";
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

    /**
     *Vi g&oslash;r brug af denne metode for at modtage data om en enkelt person, i dette tilf&aelig;lde first_name, last_name, email og
     * city.
     * Den bliver is&aelig;r brugt n&aring; vi skal vise dataen for brugeren p&aring; hjemmesiden.
     */
    @Override
    public Person getPerson(int id) {
        String sql = "SELECT * FROM idebanken.Person INNER JOIN city ON city.zip_code = Person.zip_code WHERE Person.person_id=?";
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

    /**
     * Returnerer et {@link Person} objekt ud fra en e-mail adresse.
     */
    @Override
    public Person getPerson(String email) {
        String sql = "SELECT * FROM idebanken.Person INNER JOIN city ON city.zip_code = Person.zip_code WHERE Person.email=?";
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

    /**
     * Her finder vi brugerens tlf nummere ud fra personens personId, og inds&aelig;tter dem i en liste
     */
    private List<String> findPhoneNumbers(int id){
        List<String> tlf = new ArrayList<>();
        String sql = "SELECT tlf FROM idebanken.PhoneNumbers WHERE person_id=?";
        sqlRowSet = jdbc.queryForRowSet(sql, id);

        while (sqlRowSet.next()){
            tlf.add(sqlRowSet.getString("tlf"));
        }
        return tlf;

    }

    /**
     * Her finder vi brugerens personId ud fra E-mailen
     */
    @Override
    public int getPersonId(String email) {
        String sql = "SELECT person_id FROM idebanken.Person WHERE email=?";
        sqlRowSet = jdbc.queryForRowSet(sql, email);

        while (sqlRowSet.next()){
            return sqlRowSet.getInt("person_id");
        }
        return 0;
    }

    /**
     * Returnerer true, hvis e-mailen er ledig, og returnerer false, hvis en bruger med e-mailen allerede eksisterer.
     */
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

    /**
     * Returnerer et id p&aring; en role i databasen, ud fra en String.
     */
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