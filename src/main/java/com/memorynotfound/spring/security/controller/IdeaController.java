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

@Controller
public class IdeaController {

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;
    @Autowired
    IPersonDbRepository iPersonDbRepository;

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
                return "redirect:/idea/confirm-created-idea";
    }

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

    @GetMapping("/all-ideas")
    public String readAllIdeas(Model model){
        model.addAttribute("idea_data", iIdeaDbRepository.getAllIdeas());
        return "all-ideas";
    }

    @GetMapping("/edit-idea")
    public String userIdeaPerson(@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute( person);

        Idea idea = iIdeaDbRepository.getIdea(id);
        model.addAttribute("idea", idea);
        return "idea/edit-idea";
    }

    @PostMapping("/edit-idea-post")
    public String editIdea(
            @ModelAttribute("ideaName") String ideaName,
            @ModelAttribute("ideaDescription") String ideaDescription,
            @ModelAttribute("ideaId") int ideaId){

            Idea currentIdea = new Idea(ideaId, ideaName, ideaDescription);
            iIdeaDbRepository.updateIdea(currentIdea);

        return "redirect:/idea/confirm-created-idea";
    }

    @PostMapping("/delete-idea-post")
    public String deleteIdea(
            @ModelAttribute("ideaId") int ideaId){

        iIdeaDbRepository.deleteIdea(ideaId);

        return "redirect:/idea/confirm-created-idea";
    }


}
