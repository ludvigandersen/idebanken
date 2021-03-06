package com.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * @author Mikkel
 * @author Ludvig
 */
public class Person {
    private int personId;
    private String firstName;
    private String lastName;
    private String email;
    private String tlf1;
    private String tlf2;
    private int zipCode;
    private String city;
    private String password;
    private String role;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public Person() {
    }

    public Person(int personId, String firstName, String lastName, String email, String tlf1, String tlf2, int zipCode, String city) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tlf1 = tlf1;
        this.tlf2 = tlf2;
        this.zipCode = zipCode;
        this.city = city;
    }

    public Person(String firstName, String lastName, String email, String tlf1, String tlf2, int zipCode, String city, String password, String role, LocalDate date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tlf1 = tlf1;
        this.tlf2 = tlf2;
        this.zipCode = zipCode;
        this.city = city;
        this.password = encoder.encode(password);
        this.role = role;
        this.date = date;
    }

    public Person(String first_name, String last_name, String email, String city) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.city = city;
    }

    public Person(String first_name, String last_name, String email, int zipCode, String city) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.zipCode = zipCode;
        this.city = city;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTlf1() {
        return tlf1;
    }

    public void setTlf1(String tlf1) {
        this.tlf1 = tlf1;
    }

    public String getTlf2() {
        return tlf2;
    }

    public void setTlf2(String tlf2) {
        this.tlf2 = tlf2;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encoder.encode(password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {

        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", tlf1='" + tlf1 + '\'' +
                ", tlf2='" + tlf2 + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}