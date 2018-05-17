package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Group;
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

@Controller
public class GroupController {

    @Autowired
    IGroupDbRepository iGroupDbRepository;
    @Autowired
    IPersonDbRepository iPersonDbRepository;

    @GetMapping ("/create-group")
    public String createGroup (Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("PersonName",auth.getName());
        return "user/create-group";
    }

    @PostMapping("/create-group-post")
    public String createGroup (@ModelAttribute("name") String groupName,
                               @ModelAttribute("PersonName") String PersonName){


        Group group = new Group(groupName);
        iGroupDbRepository.createGroup(group);
        int PersonId = iPersonDbRepository.getPersonId(PersonName);


        return "redirect:/";
    }

    @GetMapping("/group-details")
    public String details (@RequestParam("id") int id, Model model){

        Group group = iGroupDbRepository.read(id);
        model.addAttribute("group", group);

        return "user/groupDetails";
    }
}