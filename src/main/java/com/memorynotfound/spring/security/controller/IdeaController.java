package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Idea;
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
        return "idea/confirm-created-idea";
    }


    @GetMapping("/all-ideas")
    public String readAllIdeas(Model model){
        model.addAttribute("idea_data", iIdeaDbRepository.getAllIdeas());
        return "all-ideas";
    }



}
