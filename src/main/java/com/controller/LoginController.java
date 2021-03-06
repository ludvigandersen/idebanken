package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikkel
 */
@Controller
public class LoginController {

    /**
     * Metoden her bliver kaldt n&aring;r man logger ind og fra {@link com.config.SecurityConfig}
     * klassen bliver sendt videre.
     * Her finder vi s&aring; ud af hvilken user role man er logget ind med og afh&aelig;ngig af det
     * bliver man redirected til den rigtige side.
     */
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