package com.controller;

import com.model.Idea;
import com.model.Person;
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

import java.time.LocalDate;

/**
 * @author Christoffer
 */

@Controller
public class IdeaController {

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;
    @Autowired
    IPersonDbRepository iPersonDbRepository;

    /**
     * Her mapper vi til opret id&eacute; siden.
     */
    @GetMapping("/create-idea")
    public String createIdea(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        model.addAttribute("ideaPersonName", auth.getName());
        return "idea/create-idea";
    }

    /**
     * Her henter vi det data som brugeren har indtastet p&aring; opret id&eacute; siden, og gennem iIdeaDbRepository,
     * kalder vi createIdea metoden og tilf&oslash;jer den nye id&eacute; i databasen.
     * Derefter redirecter den til en kvittering
     */
    @PostMapping("/create-idea-post")
    public String createIdea(
            @ModelAttribute("ideaName") String ideaName,
            @ModelAttribute("ideaDescription") String ideaDescription,
            @ModelAttribute("ideaPersonName") String ideaPersonName){

                int ideaPersonId = iPersonDbRepository.getPersonId(ideaPersonName);

                Idea currentIdea = new Idea(ideaName, ideaDescription, ideaPersonId, LocalDate.now());
                System.out.println(currentIdea.toString());
                iIdeaDbRepository.createIdea(currentIdea);
                return "redirect:/idea/confirm-created-idea";
    }

    /**
     * Her mapper vi til kvitteringen, som brugen f&aring;r efter en id&eacute; er oprettet
     */
    @GetMapping("idea/confirm-created-idea")
    public String confirmCreatedIdea(){
        return "idea/confirm-created-idea";
    }

    @GetMapping("/idea")
    public String ideaIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute(person);
        return "idea/index";
    }

    /**
     * Her viser vi id&eacute;brugeren en liste over alle de id&eacute;er han har oprettet, vi henter hans navn via. auth
     * og bruger det til at finde hans PersonId.
     */
    @GetMapping("/idea/my-ideas")
    public String myIdeas(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        int ideaPersonId = iPersonDbRepository.getPersonId(name);
        Person person = iPersonDbRepository.getPerson(auth.getName());

        model.addAttribute("idea_text",iIdeaDbRepository.getIdeaList(ideaPersonId));
        model.addAttribute(person);
        return "idea/my-ideas";
    }

    /**
     * Her henter vi en liste af alle id&eacute;er der er lagt op websiden
     */
    @GetMapping("/all-ideas")
    public String readAllIdeas(Model model){
        model.addAttribute("idea_data", iIdeaDbRepository.getAllIdeas());
        return "all-ideas";
    }

    /**
     * Her mapper vi til edit idea siden, hvor id&eacute;personen har mulighed for at &aelig;ndre sin id&eacute;.
     */
    @GetMapping("/edit-idea")
    public String userIdeaPerson(@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute( person);

        Idea idea = iIdeaDbRepository.getIdea(id);
        model.addAttribute("idea", idea);
        return "idea/edit-idea";
    }
    /**
     * Her henter vi al den indtastede data p&aring; &aelig;ndre id&eacute; siden, og bruger den til at &aelig;ndre id&eacute;personens id&eacute; via.
     * metoden updateIdea()
     */
    @PostMapping("/edit-idea-post")
    public String editIdea(
            @ModelAttribute("ideaName") String ideaName,
            @ModelAttribute("ideaDescription") String ideaDescription,
            @ModelAttribute("ideaId") int ideaId){

            Idea currentIdea = new Idea(ideaId, ideaName, ideaDescription);
            iIdeaDbRepository.updateIdea(currentIdea);

        return "redirect:/idea/confirm-created-idea";
    }

    /**
     * Denne metode bruger vi til at slette en id&eacute;, det g&oslash;r vi ved at hente ideaId fra html'en og
     * bruger den i deleteIdea metoden.
     */
    @PostMapping("/delete-idea-post")
    public String deleteIdea(
            @ModelAttribute("ideaId") int ideaId){

        iIdeaDbRepository.deleteIdea(ideaId);

        return "redirect:/idea/confirm-created-idea";
    }


}
