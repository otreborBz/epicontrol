package com.epicontrol.epicontrol.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class EpisService {

  @Value("${api.epi.url}")
  private String epiUrl;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public EpisService() {
    this.objectMapper.registerModule(new JavaTimeModule());
  }

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

            // Converte o campo 'validade' de Array para LocalDate
            for (Map<String, Object> epi : epis) {
                if (epi.get("validade") instanceof ArrayList) {
                    List<Integer> dateParts = (List<Integer>) epi.get("validade");
                    if (dateParts.size() >= 3) {
                        LocalDate validade = LocalDate.of(dateParts.get(0), dateParts.get(1), dateParts.get(2));
                        epi.put("validade", validade);
                    }
                }
            }
            return epis;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

public void criar(String nome, String ca, LocalDate validade, Integer quantidade, String token) {
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

        restTemplate.exchange(epiUrl, HttpMethod.POST, entity, String.class);

    } catch (Exception e) {
        throw new RuntimeException("Falha ao criar EPI: " + e.getMessage(), e);
    }
}

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

public Map<String, Object> getById(String id, String token) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = epiUrl + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        Map<String, Object> epi = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        // Converte o campo 'validade' de Array para LocalDate
        if (epi != null && epi.get("validade") instanceof ArrayList) {
            List<Integer> dateParts = (List<Integer>) epi.get("validade");
            if (dateParts.size() >= 3) {
                LocalDate validade = LocalDate.of(dateParts.get(0), dateParts.get(1), dateParts.get(2));
                epi.put("validade", validade);
            }
        }

        return epi;

    } catch (Exception e) {
        throw new RuntimeException("Falha ao buscar EPI por ID: " + e.getMessage(), e);
    }
}

public void editar(String id, String nome, String ca, LocalDate validade, Integer quantidade, String token){
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set( "Authorization", "Bearer " + token);

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
