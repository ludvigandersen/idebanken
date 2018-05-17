package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.model.Person;
import com.memorynotfound.spring.security.repository.IGroupDbRepository;
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

import java.util.List;

@Controller
public class GroupController {

    @Autowired
    IGroupDbRepository iGroupDbRepository;
    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @GetMapping ("/create-group")
    public String createGroup (Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("personName",auth.getName());
        return "user/create-group";
    }

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

    @PostMapping("/add-group-member-post")
    public String addGroupMember(@ModelAttribute("groupId") int groupId,
                                 @ModelAttribute("person_email") String personEmail){

        int personId = iPersonDbRepository.getPersonId(personEmail);
        System.out.println("Person email: " + personEmail);
        System.out.println("Person id: " + personId);
        System.out.println("group id: " + groupId);
        iGroupDbRepository.addMember(groupId, personId);
        return "redirect:/add-group-member?id="+groupId;
    }

    @GetMapping("/group-details")
    public String details (@RequestParam("id") int id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = iPersonDbRepository.getPerson(auth.getName());
        model.addAttribute("person", person);
        double rate = 3;
        model.addAttribute("rate", rate);

        List<Person> persons = iGroupDbRepository.read(id);
        model.addAttribute("developers", persons);

        return "user/groupDetails";
    }
}