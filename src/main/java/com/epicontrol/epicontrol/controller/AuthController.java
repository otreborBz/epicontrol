package com.epicontrol.epicontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.epicontrol.epicontrol.service.FirebaseAuth;

@Controller
public class AuthController {

    @Autowired
    private FirebaseAuth firebaseAuthService;

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        System.out.println("Tentativa de login com email: " + email);

        boolean isAuthenticated = firebaseAuthService.authenticate(email, password);

        if (isAuthenticated) {
            return "redirect:/"; // Sucesso: redireciona para a página inicial
        } else {
            redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos.");
            return "redirect:/login"; // Falha: volta para a página de login com uma mensagem de erro
        }
    }
}
