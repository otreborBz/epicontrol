package com.epicontrol.epicontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showHomePage() {
        return "home"; // Retorna o nome do arquivo HTML (home.html)
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "Login"; 
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Retorna o nome do arquivo HTML (register.html)
    }
}
