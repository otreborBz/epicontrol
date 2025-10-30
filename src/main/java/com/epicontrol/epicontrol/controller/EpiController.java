package com.epicontrol.epicontrol.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epicontrol.epicontrol.service.EpisService;

import jakarta.servlet.http.HttpSession;




@Controller
public class EpiController {

  private final EpisService episService;

  public EpiController(EpisService episService) {
    this.episService = episService;
  }

   @GetMapping("/epis")
    public String listarEpis(Model model, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN"); 
        if (token == null) {
            return "redirect:/login";
        }
        List<Map<String, Object>> epis = episService.listar(token);
        model.addAttribute("epis", epis);
        return "epis";
    }

    @PostMapping("/epis")
    public String novoEpi(
            @RequestParam("nome") String nome,
            @RequestParam("ca") String ca,
            @RequestParam("validade") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validade,
            @RequestParam("quantidade") Integer quantidade,
            HttpSession session) {

        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/epis";
        }

        try {
            episService.criar(nome, ca, validade, quantidade, token);
           
        } catch (Exception e) {
        }
        return "redirect:/epis";
    }
  
  @PostMapping("/epis/delete")
    public String delete(@RequestParam("id") String id, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/epis";
        }

        try {
            episService.deletar(id, token);
        } catch (Exception e) {
            
        }

        return "redirect:/epis";
    }

    @PostMapping("/epis/edit")
    public String editar( 
            @RequestParam("id") String id, 
            @RequestParam("nome") String nome,
            @RequestParam("ca") String ca,
            @RequestParam("validade") LocalDate validade,
            @RequestParam("quantidade") Integer quantidade,
            HttpSession session) {

        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return "redirect:/epis";
        }
        try {
            episService.editar(id, nome, ca, validade, quantidade, token);
        } catch (Exception e) {
            
        }
        return "redirect:/epis";
    }

    @GetMapping("/api/epis/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getColaborador(@PathVariable String id, HttpSession session) {
        String token = (String) session.getAttribute("ID_TOKEN");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            Map<String, Object> epi = episService.getById(id, token);
            return ResponseEntity.ok(epi);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); 
        }
    }

}
