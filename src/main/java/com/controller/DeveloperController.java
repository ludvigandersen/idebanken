package com.controller;

import com.email.Email;
import com.model.Group;
import com.model.Idea;
import com.model.Person;
import com.repository.IGroupDbRepository;
import com.repository.IIdeaDbRepository;
import com.repository.IPersonDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author Christoffer
 * @author Mikkel
 * @author Nicolai
 */
@Controller
public class DeveloperController {

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @Autowired
    IGroupDbRepository iGroupDbRepository;

    Email email = new Email();

    public DeveloperController() throws MessagingException {
    }

    /**
     * Metoden bruger vi til at vise forsiden n&aring;r en bruger er logget ind, samt at vise de id&eacute;er som udvikleren har ans&oslash;gt.
     */
    @GetMapping("/user")
    public String userIndex(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Idea> assigned = iIdeaDbRepository.getAssignedIdeas(iGroupDbRepository.getGroupIdsWithPerson(iPersonDbRepository.getPersonId(auth.getName())));
        model.addAttribute("assigned", assigned);

        List<Idea> applied = iIdeaDbRepository.getAppliedIdeas(iGroupDbRepository.getGroupIdsWithPerson(iPersonDbRepository.getPersonId(auth.getName())));
        model.addAttribute("applied", applied);
        return "user/index";
    }

    /**
     * Her er metoden vi bruger til at &aelig;ndre udvikler og id&eacute;person brugernes data.
     * For at updatere vores bruger ska vi hente fornavn, efternavn, E-mail, tlf1, tlf2,
     * oldtlf1, oldtlf2, postnr og by.
     * via. updatePerson()
     */
    @PostMapping("/user-update-person")
    public String updatePerson(
        @ModelAttribute("firstName") String firstName,
        @ModelAttribute("lastName") String lastName,
        @ModelAttribute("email") String email,
        @ModelAttribute("tlf1") String tlf1,
        @ModelAttribute("tlf2") String tlf2,
        @ModelAttribute("oldTlf1") String oldTlf1,
        @ModelAttribute("oldTlf2") String oldTlf2,
        @ModelAttribute("zipCode") int zipCode,
        @ModelAttribute("city") String city){


                int personId = iPersonDbRepository.getPersonId(email);

                Person currentPerson = new Person(personId, firstName, lastName, email, tlf1, tlf2, zipCode, city);
                iPersonDbRepository.updatePerson(currentPerson, oldTlf1, oldTlf2);

                return "redirect:/user/confirm-apply";
    }

    /**
     * Her er metoden vi bruger til at &aelig;ndre udvikler og id&eacute;person brugernes password.
     * For at kunne &aelig;ndre password skal vi hente E-mail, det nye password og det gamle password
     * fra my-profile.html
     */
    @PostMapping("/user-update-password")
    public String updatePersonPassword(
            @ModelAttribute("email") String email,
            @ModelAttribute("password") String password,
            @ModelAttribute("oldPassword") String oldPassword){
        Person person = new Person();

        int personId = iPersonDbRepository.getPersonId(email);

        person.setPersonId(personId);
        person.setPassword(password);

        iPersonDbRepository.updatePersonPassword(person,oldPassword);

        return "redirect:/user/confirm-apply";
    }

    /**
     * Denne metode bruger vi n&aring;r en Person &oslash;nsker at l&aelig;se mere om en id&eacute;.
     * Vi sender t Person objekt og en rating med, som bliver brugt i dashboardet.
     *
     * Vi sender ogs&aring; en liste med id&eacute;id´er, grupper og den id&eacute; personen &oslash;nsker at se, med over i HTML filen.
     * Listen med id´er bliver brugt til at finde ud af om brugeren allerede har ans&oslash;gt id&eacute;en.
     */
    @GetMapping("/user/idea")
    public String userIdea(@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Integer> ideas = iGroupDbRepository.getIdeaIdsWithGroup(iGroupDbRepository.getGroupIdsWithPerson(iPersonDbRepository.getPersonId(person.getEmail())));
        model.addAttribute("ideas", ideas);

        List<Group> groups = iGroupDbRepository.getGroupsWithPersonIn(iPersonDbRepository.getPersonId(person.getEmail()));
        model.addAttribute("groups", groups);

        Idea idea = iIdeaDbRepository.getIdea(id);
        model.addAttribute("idea", idea);

        Person ideaPerson = iPersonDbRepository.getPerson(idea.getIdeaPerson());
        model.addAttribute("ideaPerson", ideaPerson);
        return "user/idea-user";
    }

    /**
     * Metoden her bliver kaldt n&aring;r en udvikler vil ans&oslash;ge en id&eacute;.
     * Udvikleren har mulighed for at skrive en besked og v&aelig;lge hvilken gruppe han vil ans&oslash;ge id&eacute;en med.
     * Her bliver metoden {@link com.repository.GroupDbRepository#assignGroupToIdea(int, int)} kaldt.
     * {@link Email#emailApplyToIdea(Person, String, String, Idea)} metoden bliver ogs&aring; kaldt og udvikleren har nu ans&oslash;gt id&eacute;en.
     */
    @PostMapping("/aply-for-idea-post")
    public String aplyForIdea(@RequestParam("ideaId") int ideaId,
                              @RequestParam("personEmail") String developerEmail,
                              @RequestParam("group") int groupId,
                              @RequestParam("message") String message,
                              @RequestParam("ideaEmail") String ideaEmail){
        iGroupDbRepository.assignGroupToIdea(ideaId, groupId);
        email.emailApplyToIdea(iPersonDbRepository.getPerson(developerEmail), message, ideaEmail, iIdeaDbRepository.getIdea(ideaId));
        return "redirect:/user/confirm-apply";
    }

    /**
     * Metoden bliver brugt til at vise siden confirm-apply.html til at bekr&aelig;fte at udvikleren har ans&oslash;gt en id&eacute;
     */
    @GetMapping("user/confirm-apply")
    public String confirmApply(){
        return "user/confirm-apply";
    }

    /**
     * Denne metode sender et Person objekt, som er den person som er logget ind, samt en rating v&aelig;rdi ind i HTML filen via. thymeleaf.
     * Vi sender ogs&aring; en liste med id&eacute;er afsted, som skal bruges til at vise alle id&eacute;er, som er oprettet p&aring; siden.
     * Til sidst returnerer vi strengen "user/find-ideas" som g&oslash;r at HTML filen find.ideas.html bliver vist.
     */
    @GetMapping("user/find-ideas")
    public String findIdea(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Idea> ideas = iIdeaDbRepository.getAllIdeas();
        model.addAttribute("ideas", ideas);
        return "user/find-ideas";
    }

    /**
     * Denne metode bliver brugt n&aring;r en udvikler &oslash;nsker at se hvilke grupper, han er med i.
     */
    @GetMapping("user/group")
    public String groups(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        int personId = iPersonDbRepository.getPersonId(auth.getName());
        model.addAttribute("groups", iGroupDbRepository.getDeveloperGroupsWithPersonId(personId));



        return "user/group";
    }

    /**
     * Her bliver metoden brugt n&aring;r en udvikler &oslash;nsker at se sin profil og evt. &aelig;ndre fx telefon nummer.
     */
    @GetMapping("user/my-profile")
    public String myProfile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);
        return "user/my-profile";
    }
}