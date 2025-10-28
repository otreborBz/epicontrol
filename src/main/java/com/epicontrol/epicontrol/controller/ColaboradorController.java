package com.epicontrol.epicontrol.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.epicontrol.epicontrol.service.ColaboradorService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping("/colaboradores")
    public String listarColaboradores(Model model, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN"); 
        if (token == null) {
            return "redirect:/login";
        }

        // Lista de colaboradores no formato Map<String,Object>
        List<Map<String, Object>> colaboradores = colaboradorService.listarColaboradoresComoLista(token);
        model.addAttribute("colaboradores", colaboradores);
        return "colaboradores";
    }

    @PostMapping("/colaboradores")
    public String novoColaborador(
            String nome,
            String re,
            String data_admissao,
            String setor,
            String funcao,
            HttpSession session) {

        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/colaboradores";
        }

        try {
            colaboradorService.criarColaborador(nome, re, data_admissao, setor, funcao, token);
        } catch (Exception e) {
            // Aqui você pode adicionar um tratamento de erro, como exibir uma mensagem na tela.
        }
        return "redirect:/colaboradores";
    }
}
