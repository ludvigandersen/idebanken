package com.memorynotfound.spring.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

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

    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request){
        if (request.isUserInRole("USER")){
            return "redirect:/user";
        } else if (request.isUserInRole("manager")){
            return "redirect:/manager";
        }
        return "/login";
    }

}