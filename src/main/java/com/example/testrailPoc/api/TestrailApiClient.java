package com.example.testrailPoc.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.example.testrailPoc.utils.ConfigLoader;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class TestrailApiClient {

    private final RestClient restClient;
    private final AuthHandler authHandler;
    private final String baseUrl;

    public TestrailApiClient(ConfigLoader configLoader) {
        log.info("Initializing TestrailApiClient with base URL: {}", configLoader.getBaseUrl() + ", username: " + configLoader.getUsername()  + ", api key: " + configLoader.getApiKey() );
        this.restClient = new RestClient();
        this.authHandler = new AuthHandler(configLoader.getUsername(), configLoader.getApiKey());
        this.baseUrl = configLoader.getBaseUrl().endsWith("/") ? configLoader.getBaseUrl() : configLoader.getBaseUrl() + "/";
    }

    public String getTestCases(int projectId, int suiteId) throws IOException, InterruptedException {
        String url = baseUrl + "index.php?/api/v2/get_cases/" + projectId + "&suite_id=" + suiteId;
        Map<String, String> headers = authHandler.getAuthHeaders();
        return restClient.get(url, headers);
    }

    // Get list of suites for a project
    public String getSuites(int projectId) throws IOException, InterruptedException {
        String url = baseUrl + "index.php?/api/v2/get_suites/" + projectId;
        Map<String, String> headers = authHandler.getAuthHeaders();
        return restClient.get(url, headers);
    }

    // Fetch using an absolute URL (used for pagination links)
    public String getByUrl(String url) throws IOException, InterruptedException {
        Map<String, String> headers = authHandler.getAuthHeaders();
        return restClient.get(url, headers);
    }

}
