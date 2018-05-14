package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;
import com.memorynotfound.spring.security.repository.IIdeaDbRepository;
import com.memorynotfound.spring.security.repository.IPersonDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return "idea/create-idea";
    }

    @PostMapping("/create-idea")
    public String createIdea(
        @ModelAttribute("ideaName") String ideaName,
        @ModelAttribute("ideaDescription") String ideaDescription){

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();

            iPersonDbRepository.getPerson(name);

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
    public String createUser(@ModelAttribute Person person){
        iPersonDbRepository.createPerson(person);
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

    @GetMapping("/all-developers")
    public String readAllDevelopers(Model model){
            model.addAttribute("person_data", iPersonDbRepository.getAllPersons());
            return "all-developers";
    }

    @GetMapping("/all-ideas")
    public String readAllIdeas(Model model){
        model.addAttribute("idea_data", iIdeaDbRepository.getAllIdeas());
        return "all-ideas";
    }
}