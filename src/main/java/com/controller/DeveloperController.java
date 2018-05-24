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
     * Denne metode bruger vi når en Person ønsker at læse mere om en idé.
     * Vi sender t Person objekt og en rating med, som bliver brugt i dashboardet.
     *
     * Vi sender også en liste med idéid´er, grupper og den idé personen ønsker at se, med over i HTML filen.
     * Listen med id´er bliver brugt til at finde ud af om brugeren allerede har ansøgt idéen.
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

    @GetMapping("user/confirm-apply")
    public String confirmApply(){
        return "user/confirm-apply";
    }

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