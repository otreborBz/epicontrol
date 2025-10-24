package com.epicontrol.epicontrol.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class FirebaseAuth {

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    @Value("${firebase.auth.url}")
    private String firebaseAuthUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean authenticate(String email, String password) {
        String url = firebaseAuthUrl + "?key=" + firebaseApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        System.out.println("Enviando requisição de autenticação para o Firebase...");
        try {
            restTemplate.postForEntity(url, entity, String.class);
            System.out.println("Firebase respondeu com sucesso!");
            return true; // Autenticação bem-sucedida
        } catch (HttpClientErrorException e) {
            // Log do erro pode ser útil aqui (e.g., e.getStatusCode(), e.getResponseBodyAsString())
            System.err.println("Erro na autenticação com Firebase: " + e.getMessage());
            return false; // Falha na autenticação
        }
    }
}
