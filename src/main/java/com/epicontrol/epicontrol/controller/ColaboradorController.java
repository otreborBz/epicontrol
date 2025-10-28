package com.epicontrol.epicontrol.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<Map<String, Object>> colaboradores = colaboradorService.listarColaboradoresComoLista(token);
        model.addAttribute("colaboradores", colaboradores);
        return "colaboradores";
    }

    @PostMapping("/colaboradores")
    public String novoColaborador(String nome, String re, String data_admissao,String setor,String funcao,HttpSession session) {

        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/colaboradores";
        }

        try {
            colaboradorService.criarColaborador(nome, re, data_admissao, setor, funcao, token);
        } catch (Exception e) {
        }
        return "redirect:/colaboradores";
    }

    @PostMapping("/colaboradores/delete")
    public String deleteColaborador(@RequestParam("id") String id, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/colaboradores";
        }

        try {
            colaboradorService.deletarColaborador(id, token);
        } catch (Exception e) {
            
        }

        return "redirect:/colaboradores";
    }

    @PostMapping("/colaboradores/edit")
    public String editarColaborador( 
            @RequestParam("id") String id, 
            @RequestParam("nome") String nome,
            @RequestParam("re") String re,
            @RequestParam("data_admissao") String data_admissao,
            @RequestParam("setor") String setor,
            @RequestParam("funcao") String funcao,
            HttpSession session) {

        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/colaboradores";
        }
        try {
            colaboradorService.editarColaborador(id, nome, re, data_admissao, setor, funcao, token);
        } catch (Exception e) {
            
        }
        return "redirect:/colaboradores";
    }

    @GetMapping("/api/colaboradores/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getColaborador(@PathVariable String id, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        try {
            Map<String, Object> colaborador = colaboradorService.getColaboradorById(id, token);
            return ResponseEntity.ok(colaborador);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }

}
