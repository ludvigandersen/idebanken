package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Group;
import com.memorynotfound.spring.security.repository.IGroupDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GroupController {

    @Autowired
    IGroupDbRepository iGroupDbRepository;

    @GetMapping ("/create-group")
    public String createGroup (){
        return "create-group";
    }
    @PostMapping("/create-group-post")
    public String createGroup (@ModelAttribute Group group){
        iGroupDbRepository.createGroup(group);
        return "CreateGroup";
    }


}
