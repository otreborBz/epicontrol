package com.epicontrol.epicontrol.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FirebaseAuth {

    @Value("${api.auth.url}") // 👉 Agora aponta para o endpoint da sua API Node
    private String apiAuthUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuth.class);

    public String authenticate(String email, String password) { 
        if (apiAuthUrl == null || apiAuthUrl.trim().isEmpty()) {
            throw new IllegalStateException("URL da API de autenticação não configurada.");
        }

        String url = apiAuthUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.info("🔐 Enviando autenticação para a API Node: {}", url);
        try {
            // 1. Pega a resposta como String para poder inspecioná-la
            ResponseEntity<String> responseAsString = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            String responseBody = responseAsString.getBody();
            logger.info("📬 Resposta recebida da API Node: {}", responseBody);

            // 2. Tenta converter a String para o objeto esperado
            ObjectMapper objectMapper = new ObjectMapper();
            FirebaseTokenResponse tokenResponse = objectMapper.readValue(responseBody, FirebaseTokenResponse.class);
            
            if (tokenResponse.getIdToken() == null) {
                throw new RuntimeException("Resposta do Firebase não continha um idToken.");
            }
            logger.info("Token emcntrado: ", tokenResponse.getIdToken());

            return tokenResponse.getIdToken(); // Retorna apenas o token vindo do Node.js
        } catch (HttpClientErrorException e) {
            // Este bloco captura erros de HTTP (4xx, 5xx) onde o servidor Node respondeu com um status de erro.
            throw new RuntimeException("Falha na autenticação: " + e.getResponseBodyAsString(), e);
        } catch (JsonProcessingException e) {
            // Este bloco captura erros de conversão do JSON.
           
            throw new RuntimeException("Resposta inválida do servidor de autenticação.", e);
        } catch (RestClientException e) {
            // Este bloco captura outros erros, como falha na conversão da resposta (JSON inválido).
            throw new RuntimeException("Falha ao processar resposta do servidor de autenticação.", e);
        }
    }
}
