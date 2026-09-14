package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.MissingTestCase;
import com.example.testrailPoc.models.TestRailCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LlmRunbookClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ChatClient chatClient;

    public LlmRunbookClient(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public List<MissingTestCase> generateMissingCases(JiraRequirement requirement, List<TestRailCase> existingCases) {
        String prompt = QaGapAnalysisPromptFactory.buildPrompt(requirement, existingCases);
        String rawResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        return parseResponse(rawResponse);
    }

    private List<MissingTestCase> parseResponse(String rawResponse) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return List.of();
        }

        String cleaned = rawResponse.trim();
        if (cleaned.startsWith("```")) {
            int start = cleaned.indexOf("[");
            int end = cleaned.lastIndexOf("]");
            if (start >= 0 && end > start) {
                cleaned = cleaned.substring(start, end + 1);
            }
        }

        try {
            return OBJECT_MAPPER.readValue(cleaned, new TypeReference<List<MissingTestCase>>() {
            });
        } catch (IOException e) {
            return fallbackParse(cleaned);
        }
    }

    private List<MissingTestCase> fallbackParse(String cleaned) {
        List<MissingTestCase> parsed = new ArrayList<>();
        if (cleaned.contains("\"type\"")) {
            try {
                parsed.addAll(OBJECT_MAPPER.readValue(cleaned, new TypeReference<List<MissingTestCase>>() {
                }));
            } catch (IOException ignored) {
                return List.of();
            }
        }
        return parsed;
    }
}

