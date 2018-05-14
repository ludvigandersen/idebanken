package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;
import com.memorynotfound.spring.security.repository.IIdeaDbRepository;
import com.memorynotfound.spring.security.repository.IPersonDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class FrontpageController {

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/idea")
    public String ideaIndex() {
        return "idea/index";
    }
    @GetMapping("/create-idea")
    public String createIdea(){
        return "create-idea";
    }

    @PostMapping("/create-idea")
    public String createIdea(
        @ModelAttribute("ideaName") String ideaName,
        @ModelAttribute("ideaDescription") String ideaDescription,
        @ModelAttribute("ideaPerson") int ideaPerson){



            Idea currentIdea = new Idea(ideaName, ideaDescription, ideaPerson, LocalDate.now());
            System.out.println(currentIdea.toString());
            iIdeaDbRepository.createIdea(currentIdea);
            return "confirm-created-idea";
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
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }
}