package com.epicontrol.epicontrol.service;

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

@Service
public class ColaboradorService {

    @Value("${api.colaborador.url}")
    private String colaboradoresUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> listarColaboradoresComoLista(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    colaboradoresUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Converte JSON em lista de Map
            return objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void criarColaborador(String nome, String re, String data_admissao, String setor, String funcao, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("nome", nome);
            requestBody.put("re", re);
            requestBody.put("data_admissao", data_admissao);
            requestBody.put("setor", setor);
            requestBody.put("funcao", funcao);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(colaboradoresUrl, HttpMethod.POST, entity, String.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao criar colaborador: " + e.getMessage(), e);
        }
    }
}
