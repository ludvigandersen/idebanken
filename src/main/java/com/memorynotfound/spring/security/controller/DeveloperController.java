package com.memorynotfound.spring.security.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DeveloperController {

    @Autowired
    IIdeaDbRepository iIdeaDbRepository;

    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @Autowired
    IGroupDbRepository iGroupDbRepository;

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

    @GetMapping("/user/idea")
    public String userIdea(@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Integer> groupIds = iGroupDbRepository.getGroupIdsWithPerson(iPersonDbRepository.getPersonId(auth.getName()));

        List<Integer> aplied = iGroupDbRepository.getGroupIdsWithIdea(groupIds);
        model.addAttribute("aplied", aplied);

        Idea idea = iIdeaDbRepository.getIdea(id);
        model.addAttribute("idea", idea);
        return "user/idea-user";
    }

    @PostMapping("/user/aply-for-idea-post")
    public String aplyForIdea(@RequestParam("ideaId") int ideaId, @RequestParam("personEmail") String email){

        return "user/confirm-aply";
    }

}
