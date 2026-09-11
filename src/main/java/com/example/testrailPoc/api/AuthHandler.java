package com.example.testrailPoc.api;

import java.util.HashMap;
import java.util.Map;

public class AuthHandler {

    private final String username;
    private final String apiKey;

    public AuthHandler(String username, String apiKey) {
        this.username = username;
        this.apiKey = apiKey;
    }

    public Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        String basicAuth = java.util.Base64.getEncoder()
                .encodeToString((username + ":" + apiKey).getBytes());
        headers.put("Authorization", "Basic " + basicAuth);
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
