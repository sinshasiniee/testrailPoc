package com.example.testrailPoc.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigLoader {

    @Value("${testrail.baseUrl:}")
    private String baseUrl;

    @Value("${testrail.username:}")
    private String username;

    @Value("${testrail.apiKey:}")
    private String apiKey;

    @Value("${llm.apiKey:}")
    private String llmApiKey;

    @Value("${llm.baseUrl:}")
    private String llmBaseUrl;

    @Value("${llm.model:gpt-4o-mini}")
    private String llmModel;

    @Value("${jira.requirements.path:classpath:jiraData.csv}")
    private String jiraRequirementsPath;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getLlmApiKey() {
        return llmApiKey;
    }

    public String getLlmBaseUrl() {
        return llmBaseUrl;
    }

    public String getLlmModel() {
        return llmModel;
    }

    public String getJiraRequirementsPath() {
        return jiraRequirementsPath;
    }
}
