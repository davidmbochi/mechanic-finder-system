package com.mechanicfinder.mechanicfindersystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login-page")
    public String loginPage(){
        return "security-views/login-page";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "security-views/access-denied";
    }
}
