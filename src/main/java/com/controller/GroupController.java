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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Christoffer
 * @author Nicolai
 */

@Controller
public class GroupController {

    @Autowired
    IGroupDbRepository iGroupDbRepository;
    @Autowired
    IPersonDbRepository iPersonDbRepository;

    /**
     * Her mapper vi til opret gruppe samtidigt med at vi henter brugeres id.
     */
    @GetMapping ("/create-group")
    public String createGroup (Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("personName",auth.getName());
        return "user/create-group";
    }

    /**
     * Her oprettes gruppen. Vi bruger den tidligere hentet id og gruppens navn
     * til at inds&aelig;tte brugeren i gruppen.
     */
    @PostMapping("/create-group-post")
    public String createGroup (@ModelAttribute("name") String groupName,
                               @ModelAttribute("personName") String personName){

        Group group = new Group(groupName);
        iGroupDbRepository.createGroup(group);
        int personId = iPersonDbRepository.getPersonId(personName);
        int groupId = iGroupDbRepository.findGroup(groupName);
        iGroupDbRepository.addMember(groupId, personId);

        return "redirect:/";
    }

    /**
     * Her mapper vi til &aelig;ndre gruppe siden, hvor brugeren f&aring;r mulighed for at indtaste de
     * &aelig;ndre han vil tilf&oslash;je
     */
    @GetMapping("/edit-group")
    public String editGroup (@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        String name = iGroupDbRepository.findGroupName(id);

        double rate = 3;
        model.addAttribute("rate", rate);

        model.addAttribute("person", person);
        model.addAttribute("groupId", id);
        model.addAttribute("name", name);

        return "user/edit-group";
    }
    /**
     * Her henter vi al den indtastede data p&aring; &aelig;ndre gruppe siden, og bruger den til at &aelig;ndre
     * gruppens data via. metoden updateGroup(), derefter redirecter den til en kvittering for
     * at den er opdateret
     */
    @PostMapping("/user-update-group")
    public String updateGroup(
            @ModelAttribute("name") String name,
            @ModelAttribute("groupId") int id){

        iGroupDbRepository.updateGroup(name, id);

        return "redirect:/user/confirm-apply";
    }

    /**
     * Her mapper vi til tilf&oslash;j gruppe siden og henter gruppe id.
     */
    @GetMapping("/add-group-member")
    public String addGroupMember(@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);
        System.out.println(id);

        model.addAttribute("groupId", id);

        return "user/add-group-member";
    }

    /**
     * Her tilf&oslash;jer vi udvikler til gruppe ved at bruge den tidligere hentede gruppe id og henter person id med e-mail.
     */
    @PostMapping("/add-group-member-post")
    public String addGroupMember(@ModelAttribute("groupId") int groupId,
                                 @ModelAttribute("person_email") String personEmail){

        int personId = iPersonDbRepository.getPersonId(personEmail);
        System.out.println("Person email: " + personEmail);
        System.out.println("Person id: " + personId);
        System.out.println("group id: " + groupId);
        iGroupDbRepository.addMember(groupId, personId);
        return "redirect:/edit-group?id="+groupId;
    }

    /**
     * Her mapper vi til gruppe detaljer hvor vi viser gruppens indhold ved brug af group id.
     */
    @GetMapping("/group-details")
    public String details (@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        model.addAttribute("groupId", id);

        List<Person> persons = iGroupDbRepository.read(id);
        model.addAttribute("developers", persons);

        return "user/groupDetails";
    }

    /**
     * Denne metode bruger vi til at slette en gruppe, det g&oslash;r vi ved at hente groupId fra html'en som vi
     * derefter bruger den i deleteGroup metoden.
     */
    @PostMapping("/delete-group-post")
    public String deleteUser(@ModelAttribute("groupId") int groupId){
        iGroupDbRepository.deleteGroup(groupId);

        return "redirect:/user/group";
    }
}