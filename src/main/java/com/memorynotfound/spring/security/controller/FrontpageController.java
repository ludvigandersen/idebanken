package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Person;
import com.memorynotfound.spring.security.repository.IPersonDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class FrontpageController {

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/manager")
    public String managerIndex() {
        return "manager/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    @GetMapping("/create-user")
    public String createUser(){
        return "create-user";
    }

    @PostMapping("/create-user")
    public String createUser(
            @ModelAttribute("firstName") String firstName,
            @ModelAttribute("lastName") String lastName,
            @ModelAttribute("email") String email,
            @ModelAttribute("tlf1") String tlf1,
            @ModelAttribute("tlf2") String tlf2,
            @ModelAttribute("zipCode") int zipCode,
            @ModelAttribute("city") String city,
            @ModelAttribute("password") String password,
            @ModelAttribute("role") String role){

        Person currentPerson = new Person(firstName, lastName, email, tlf1, tlf2, zipCode, city, password, role, LocalDate.now());
        System.out.println(currentPerson.toString());
        iPersonDbRepository.createPerson(currentPerson);
        return "confirm-created-user";
    }
}