package com.memorynotfound.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request){
        if (request.isUserInRole("USER")){
            return "redirect:/user";
        } else if (request.isUserInRole("IDEA")){
            return "redirect:/idea";
        }
        return "/login";
    }
}