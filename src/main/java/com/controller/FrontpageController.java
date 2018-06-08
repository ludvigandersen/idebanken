package com.controller;

import com.model.Group;
import com.model.Person;
import com.repository.IGroupDbRepository;
import com.repository.IPersonDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Mikkel
 * @author Ludvig
 */
@Controller
public class FrontpageController {

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @Autowired
    IGroupDbRepository iGroupDbRepository;

    /**
     * Her henvises til forsiden for hele vores site.
     */
    @GetMapping("/")
    public String root() {
        return "index";
    }

    /**
     * Denne metode viser login siden.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Denne metode bruger vi til at vise acess-denied siden, n&aring;r man vil se noget man ikke har rettigheder til p&aring; siden.
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    /**
     * Henviser til siden create-user, n&aring;r en person p&aring; siden vil oprette en ny bruger.
     */
    @GetMapping("/create-user")
    public String createUser(){
        return "user/create-user";
    }

    /**
     * Metoden bliver brugt n&aring;r man vil oprette en ny bruger og har trykket opret,
     * men at en anden bruger allerede har brugt den e-mail adresse man har indtastet.
     */
    @GetMapping("/create-user-email")
    public String createUser(Model model){
        model.addAttribute("email", "email");
        return "user/create-user";
    }

    /**
     * Denne metode bruger vi n&aring;r en person &oslash;nsker at oprette en bruger p&aring; vores side.
     * Om det er som en Id&eacute;person eller som udvikler betyder ikke noget for metoden,
     * den bruges nemlig til at oprette begge typer Person.
     */
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

    /**
     * Bruges til at bekr&aelig;fte at man har oprettet en ny bruger.
     */
    @GetMapping("user/confirm-created-user")
    public String confirmCreatedUser(){
        return "user/confirm-created-user";
    }

    /**
     * Vi bruger denne metode til at vise kontakt siden, alts&aring; hvordan folk kan f&aring; fat p&aring; os der st&aring;r bag siden.
     */
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    /**
     * Viser siden about, som beskriver, hvem vi er, os der st&aring;r bag siden.
     */
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    /**
     * I denne metode tager vi data fra alle vores developers s&aring; vi kan vise dem for vores brugere
     * p&aring; /all-developers p&aring; vores hjemmside.
     * */
    @GetMapping("/all-developers")
    public String readAllDevelopers(Model model){
            model.addAttribute("person_data", iPersonDbRepository.getAllPersons());
            return "all-developers";
    }

    /**
     * Metoden bliver brugt n&aring;r man vil slette sin bruger.
     */
    @GetMapping("/delete-user")
    public String deleteUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        return "user/delete-user";
    }

    /**
     * Denne metode ligger til r&aring;dighed for brugeren, n&aring;r de &oslash;nsker at slette deres profil.
     * Den kan tilg&aring;s for dem p&aring; "min side"
     */
    @PostMapping("/delete-user-post")
    public String deleteUser(@ModelAttribute Person person){
        iPersonDbRepository.deletePerson(iPersonDbRepository.getPersonId(person.getEmail()));
        return "redirect:/confirm-delete-user";
    }

    /**
     * Bekr&aelig;ftelse af at man har slettet sin bruger.
     */
    @GetMapping("/confirm-delete-user")
    public String confirmDeleteUser(){

        return "user/confirm-delete-user";
    }
}