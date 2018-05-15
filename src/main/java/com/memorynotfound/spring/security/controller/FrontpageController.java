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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontpageController {

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Idea> assigned = new ArrayList<>();
        model.addAttribute("assigned", assigned);

        List<Idea> applied = new ArrayList<>();
        model.addAttribute("applied", applied);
        return "user/index";
    }

    @GetMapping("/idea")
    public String ideaIndex() {
        return "idea/index";
    }

    @GetMapping("/create-idea")
    public String createIdea(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("ideaPersonName",auth.getName());
        return "idea/create-idea";
    }

    @PostMapping("/create-idea-post")
    public String createIdea(
        @ModelAttribute("ideaName") String ideaName,
        @ModelAttribute("ideaDescription") String ideaDescription,
        @ModelAttribute("ideaPersonName") String ideaPersonName){

            int ideaPersonId = iPersonDbRepository.getPersonId(ideaPersonName);

            Idea currentIdea = new Idea(ideaName, ideaDescription, ideaPersonId, LocalDate.now());
            System.out.println(currentIdea.toString());
            iIdeaDbRepository.createIdea(currentIdea);
            return "idea/confirm-created-idea";
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
        return "user/create-user";
    }

    @GetMapping("/create-user-email")
    public String createUser(Model model){
        model.addAttribute("email", "email");
        return "user/create-user";
    }

    @PostMapping("/create-user-post")
    public String createUser(@ModelAttribute Person person){
        if (iPersonDbRepository.checkEmail(person.getEmail())) {
            iPersonDbRepository.createPerson(person);
            System.out.println("User created: " + person.toString());
            return "user/confirm-created-user";
        } else {
            return "redirect:/create-user-email";
        }
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

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("id") int id, Model model){
        model.addAttribute("person", iPersonDbRepository.getPerson(id));
        return "delete-user";
    }
}