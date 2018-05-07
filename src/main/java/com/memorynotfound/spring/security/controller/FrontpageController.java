package com.memorynotfound.spring.security.controller;

import com.memorynotfound.spring.security.model.Developer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontpageController {

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/manager")
    public String managerIndex() {
        return "manager/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    @GetMapping("/create-developer")
    public String createDeveloper(){
        return "create/create-developer";
    }

    @PostMapping("/create-developer")
    public String createDeveloper(@ModelAttribute("firstName") String firstName){
        System.out.println("test");
        System.out.println(firstName);
        return "index";
    }
}