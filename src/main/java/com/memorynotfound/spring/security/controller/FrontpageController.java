package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;
import com.memorynotfound.spring.security.repository.IGroupDbRepository;
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
    IPersonDbRepository iPersonDbRepository;

    @Autowired
    IGroupDbRepository iGroupDbRepository;

    @GetMapping("/")
    public String root() {
        return "index";
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
            iGroupDbRepository.createGroup(new Group(person.getEmail()), true);
            iGroupDbRepository.assignPersonToGroup(iPersonDbRepository.getPersonId(person.getEmail()), iGroupDbRepository.getGroupIdWithName(person.getEmail()));
            System.out.println("User created: " + person.toString());
            return "redirect:/user/confirm-created-user";
        } else {
            return "redirect:/create-user-email";
        }
    }

    @GetMapping("user/confirm-created-user")
    public String confirmCreatedUser(){
        return "user/confirm-created-user";
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

    @GetMapping("/delete-user")
    public String deleteUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        return "user/delete-user";
    }

    @PostMapping("/delete-user-post")
    public String deleteUser(@ModelAttribute Person person){
        iPersonDbRepository.deletePerson(iPersonDbRepository.getPersonId(person.getEmail()));
        return "redirect:/login";
    }

    @GetMapping("/edit-user")
    public String editUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        return "user/edit-user";
    }

    @PostMapping("/edit-user-post")
    public String editUser(){

        return "user/edit-user";
    }
}