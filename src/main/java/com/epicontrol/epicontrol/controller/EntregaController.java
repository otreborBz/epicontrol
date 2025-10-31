package com.epicontrol.epicontrol.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epicontrol.epicontrol.service.ColaboradorService;
import com.epicontrol.epicontrol.service.EpisService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/entregas")
public class EntregaController {

    private final ColaboradorService colaboradorService;
    private final EpisService episService;

    public EntregaController(ColaboradorService colaboradorService, EpisService episService) {
        this.colaboradorService = colaboradorService;
        this.episService = episService;
    }

    @GetMapping
    public String paginaEntregas(Model model, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> colaboradores = colaboradorService.listarColaboradoresComoLista(token);
        List<Map<String, Object>> epis = episService.listar(token);

        model.addAttribute("colaboradores", colaboradores);
        model.addAttribute("epis", epis);

        return "entregas";
    }
}