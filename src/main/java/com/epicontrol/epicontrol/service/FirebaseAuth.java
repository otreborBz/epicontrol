package com.epicontrol.epicontrol.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FirebaseAuth {

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    @Value("${firebase.auth.url}")
    private String firebaseAuthUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuth.class);

    public boolean authenticate(String email, String password) {
        if (firebaseApiKey == null || firebaseApiKey.trim().isEmpty()) {
            logger.error("A chave de API do Firebase (firebase.api.key) não está configurada.");
            return false;
        }

        String url = UriComponentsBuilder.fromHttpUrl(firebaseAuthUrl)
                .queryParam("key", firebaseApiKey)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.info("Enviando requisição de autenticação para o Firebase para o email: {}", email);
        try {
            restTemplate.postForEntity(url, requestEntity, String.class);
            logger.info("Autenticação bem-sucedida para o email: {}", email);
            return true; // Autenticação bem-sucedida
        } catch (HttpClientErrorException e) {
            logger.error("Erro na autenticação com Firebase: Status Code: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false; // Falha na autenticação
        }
    }
}
