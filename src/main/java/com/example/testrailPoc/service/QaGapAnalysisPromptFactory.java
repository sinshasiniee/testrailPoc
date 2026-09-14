package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.MissingTestCase;
import com.example.testrailPoc.models.TestRailCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class QaGapAnalysisPromptFactory {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String PROMPT_TEMPLATE_PATH = "prompts/missing-testcase-prompt.txt";

    private QaGapAnalysisPromptFactory() {
    }

    public static String buildPrompt(JiraRequirement requirement, List<TestRailCase> existingCases) {
        String template = loadTemplate();
        String existingCasesJson = serialize(existingCases);
        String schemaJson = serialize(List.of(new MissingTestCase(
                "Negative/Positive/Edge/Performance/Boundary/ErrorHandling/Other",
                "Scenario name",
                "The system rejects the request with HTTP 400 and the message 'Invalid input'",
                "High")));

        return template.formatted(
                requirement.issueKey(),
                requirement.summary(),
                requirement.description(),
                requirement.priority(),
                requirement.status(),
                existingCasesJson,
                schemaJson
        );
    }

    private static String loadTemplate() {
        try (InputStream inputStream = QaGapAnalysisPromptFactory.class
                .getClassLoader()
                .getResourceAsStream(PROMPT_TEMPLATE_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing prompt template: " + PROMPT_TEMPLATE_PATH);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load prompt template: " + PROMPT_TEMPLATE_PATH, e);
        }
    }

    private static String serialize(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}

