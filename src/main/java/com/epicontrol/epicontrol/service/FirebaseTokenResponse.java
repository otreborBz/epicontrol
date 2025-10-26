package com.epicontrol.epicontrol.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignora campos do JSON que não mapeamos para evitar erros
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseTokenResponse {

    private String idToken;

    // Getters e Setters

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}