package com.epicontrol.epicontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/colaboradores")
    public String colaboradores() {
        return "colaboradores";
    }

    @GetMapping("/epis")
    public String epis() {
        return "epis";
    }

    @GetMapping("/entregas")
    public String entregas() {
        return "entregas";
    }

    @GetMapping("/relatorios")
    public String relatorios(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            // Lógica de busca (simulada por enquanto)
            if (query.equalsIgnoreCase("Roberto Carvalho")) {
                model.addAttribute("colaboradorEncontrado", "Roberto Carvalho");
                // Adicionar lista de entregas (simulada)
            } else {
                model.addAttribute("notFound", true);
            }
            model.addAttribute("query", query);
        }
        return "relatorios";
    }

    @PostMapping("/colaboradores/novo")
    public String novoColaborador(
        @RequestParam String nome,
        @RequestParam String re,
        @RequestParam String data_admissao,
        @RequestParam String setor,
        @RequestParam String funcao
    ) {
        // Lógica para salvar o novo colaborador no banco de dados virá aqui.
        System.out.println("Recebido novo colaborador: " + nome);
        return "redirect:/colaboradores";
    }

    @PostMapping("/epis/novo")
    public String novoEpi(
        @RequestParam String nome,
        @RequestParam String ca,
        @RequestParam String validade,
        @RequestParam int quantidade
    ) {
        // Lógica para salvar o novo EPI no banco de dados virá aqui.
        System.out.println("Recebido novo EPI: " + nome);
        return "redirect:/epis";
    }

    @PostMapping("/entregas/nova")
    public String novaEntrega(
        @RequestParam Long colaboradorId,
        @RequestParam Long epiId,
        @RequestParam int quantidadeEntregue
    ) {
        // Lógica para salvar a nova entrega no banco de dados virá aqui.
        System.out.println("Registrando entrega para o colaborador " + colaboradorId + " do EPI " + epiId + " | Quantidade: " + quantidadeEntregue);
        return "redirect:/entregas";
    }
}
