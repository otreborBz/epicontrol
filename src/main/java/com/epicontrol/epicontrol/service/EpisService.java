package com.epicontrol.epicontrol.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epicontrol.epicontrol.model.EpisModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EpisService {

    @Value("${api.epi.url}")
    private String epiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lista todos os EPIs e formata as datas para "dd/MM/yyyy".
     */
    public List<Map<String, Object>> listar(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    epiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            List<Map<String, Object>> epis = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            // Formata a data de cada EPI
            DateTimeFormatter firebaseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Map<String, Object> epi : epis) {
                if (epi.get("validade") instanceof String) {
                    String validadeStr = (String) epi.get("validade");
                    try {
                        LocalDate data = LocalDate.parse(validadeStr, firebaseFormatter);
                        String validadeFormatada = data.format(displayFormatter);
                        epi.put("validade", validadeFormatada);
                    } catch (Exception e) {
                        // mantém a string original se falhar a conversão
                    }
                }
            }

            return epis;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Cria um novo EPI.
     */
    public void criar(String nome, String ca, String validade, Integer quantidade, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            EpisModel request = new EpisModel();
            request.setNome(nome);
            request.setCa(ca);
            request.setValidade(validade); // validade agora é String
            request.setQuantidade(quantidade);

            HttpEntity<EpisModel> entity = new HttpEntity<>(request, headers);
            restTemplate.exchange(epiUrl, HttpMethod.POST, entity, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar EPI: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta um EPI pelo ID.
     */
    public void deletar(String id, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = epiUrl + "/" + id;
            restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar EPI: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um EPI pelo ID e formata a data.
     */
    public Map<String, Object> getById(String id, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = epiUrl + "/" + id;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            Map<String, Object> epi = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<Map<String, Object>>() {}
            );

            // Formata a data individual
            DateTimeFormatter firebaseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (epi.get("validade") instanceof String) {
                String validadeStr = (String) epi.get("validade");
                try {
                    LocalDate data = LocalDate.parse(validadeStr, firebaseFormatter);
                    epi.put("validade", data.format(displayFormatter));
                } catch (Exception e) {
                    // mantém original se não for possível converter
                }
            }

            return epi;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao buscar EPI por ID: " + e.getMessage(), e);
        }
    }

    /**
     * Edita um EPI existente.
     */
    public void editar(String id, String nome, String ca, String validade, Integer quantidade, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("nome", nome);
            requestBody.put("ca", ca);
            requestBody.put("validade", validade);
            requestBody.put("quantidade", quantidade);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = epiUrl + "/" + id;
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao editar EPI: " + e.getMessage(), e);
        }
    }
}
