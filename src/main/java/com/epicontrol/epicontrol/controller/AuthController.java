package com.epicontrol.epicontrol.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.epicontrol.epicontrol.service.FirebaseAuth;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private FirebaseAuth firebaseAuthService;

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        logger.info("Tentativa de login com email: {}", email);

        boolean isAuthenticated = firebaseAuthService.authenticate(email, password);

        if (isAuthenticated) {
            return "redirect:/"; // Sucesso: redireciona para a página inicial
        } else {
            logger.warn("Falha na autenticação para o email: {}", email);
            redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos.");
            return "redirect:/login"; // Falha: volta para a página de login com uma mensagem de erro
        }
    }
}
