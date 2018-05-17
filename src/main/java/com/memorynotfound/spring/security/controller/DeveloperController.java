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

        List<Integer> ideas = iGroupDbRepository.getIdeaIdsWithGroup(iGroupDbRepository.getGroupIdsWithPerson(iPersonDbRepository.getPersonId(person.getEmail())));
        model.addAttribute("ideas", ideas);

        List<Group> groups = iGroupDbRepository.getGroupsWithPersonIn(iPersonDbRepository.getPersonId(person.getEmail()));
        model.addAttribute("groups", groups);

        Idea idea = iIdeaDbRepository.getIdea(id);
        model.addAttribute("idea", idea);
        return "user/idea-user";
    }

    @PostMapping("/aply-for-idea-post")
    public String aplyForIdea(@RequestParam("ideaId") int ideaId,
                              @RequestParam("personEmail") String email,
                              @RequestParam("group") int groupId,
                              @RequestParam("message") String message){
        iGroupDbRepository.assignGroupToIdea(ideaId, groupId);
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
