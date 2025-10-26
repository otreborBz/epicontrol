package com.epicontrol.epicontrol.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.epicontrol.epicontrol.service.FirebaseAuth;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private FirebaseAuth firebaseAuthService;

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password,
                              RedirectAttributes redirectAttributes, HttpSession session) {

        logger.info("Tentativa de login com email: {}", email);

        try {
            // Recebe o token JWT do Firebase via Node API
            String idToken = firebaseAuthService.authenticate(email, password);

            // Armazena na sessão
            session.setAttribute("ID_TOKEN", idToken);

            logger.info("Autenticação bem-sucedida. Token armazenado na sessão.");
            return "redirect:/"; // sucesso

        } catch (Exception e) {
            logger.warn("Falha na autenticação para o email: {} - {}", email, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos.");
            return "redirect:/login"; // falha
        }
    }
}
