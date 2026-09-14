package com.example.testrailPoc.service;


import com.example.testrailPoc.api.TestrailApiClient;
import com.example.testrailPoc.models.TestSuite;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.testrailPoc.models.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TestCaseService {

    private final TestrailApiClient apiClient;
    private final ObjectMapper mapper;

    public TestCaseService(TestrailApiClient apiClient) {
        this.apiClient = apiClient;
        this.mapper = new ObjectMapper();
        // map snake_case JSON (e.g., section_id) to camelCase fields (sectionId)
        this.mapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE);
        // ignore unknown properties in incoming JSON to be tolerant to API changes
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Fetch and normalize test cases from TestRail (handles pagination)
    public List<TestCase> fetchTestCases(int projectId, int suiteId) throws IOException, InterruptedException {
        List<TestCase> aggregated = new ArrayList<>();

        String jsonResponse = apiClient.getTestCases(projectId, suiteId);
        log.info("Fetched test cases for project {} and suite {}. Raw response: {}", projectId, suiteId, jsonResponse);
        aggregated.addAll(parseCasesFromResponse(jsonResponse));

        // follow pagination links if present (_links.next)
        JsonNode root = mapper.readTree(jsonResponse);
        while (root.has("_links") && root.get("_links").has("next") && !root.get("_links").get("next").isNull()) {
            String nextUrl = root.get("_links").get("next").asText(null);
            if (nextUrl == null || nextUrl.isEmpty()) break;
            String nextResp = apiClient.getByUrl(nextUrl);
            aggregated.addAll(parseCasesFromResponse(nextResp));
            root = mapper.readTree(nextResp);
        }

        return aggregated;
    }

    // Fetch all test cases across all suites for a project
    public List<TestSuite> fetchAllTestCasesForProject(int projectId) throws IOException, InterruptedException {
        List<TestSuite> all = new ArrayList<>();

        String suitesResp = apiClient.getSuites(projectId);
        JsonNode sroot = mapper.readTree(suitesResp);
        JsonNode suitesArray = null;
        if (sroot.isArray()) {
            suitesArray = sroot;
        } else if (sroot.has("suites") && sroot.get("suites").isArray()) {
            suitesArray = sroot.get("suites");
        } else {
            Iterator<Map.Entry<String, JsonNode>> fields = sroot.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode child = entry.getValue();
                if (child.isArray()) { suitesArray = child; break; }
            }
        }

        if (suitesArray == null) {
            log.warn("No suites found for project {}", projectId);
            return all;
        }

        for (JsonNode suiteNode : suitesArray) {
            int suiteId = suiteNode.path("id").asInt();
            try {
                all.add(fetchTestSuiteWithCases(projectId, suiteId));
            } catch (IOException | InterruptedException e) {
                log.error("Failed fetching cases for suite {}: {}", suiteId, e.getMessage());
            }
        }

        return all;
    }

    // Fetch a single TestSuite (metadata) with its test cases
    public com.example.testrailPoc.models.TestSuite fetchTestSuiteWithCases(int projectId, int suiteId) throws IOException, InterruptedException {
        String suitesResp = apiClient.getSuites(projectId);
        JsonNode sroot = mapper.readTree(suitesResp);
        JsonNode suitesArray = null;
        if (sroot.isArray()) {
            suitesArray = sroot;
        } else if (sroot.has("suites") && sroot.get("suites").isArray()) {
            suitesArray = sroot.get("suites");
        } else {
            Iterator<Map.Entry<String, JsonNode>> fields = sroot.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode child = entry.getValue();
                if (child.isArray()) { suitesArray = child; break; }
            }
        }

        com.example.testrailPoc.models.TestSuite testSuite = new com.example.testrailPoc.models.TestSuite();
        testSuite.setId(suiteId);

        if (suitesArray != null) {
            for (JsonNode suiteNode : suitesArray) {
                if (suiteNode.path("id").asInt(-1) == suiteId) {
                    testSuite.setName(suiteNode.path("name").asText(null));
                    testSuite.setDescription(suiteNode.path("description").asText(null));
                    break;
                }
            }
        } else {
            log.warn("No suites array found while looking up suite {}", suiteId);
        }

        // Fetch cases (handles pagination)
        List<TestCase> cases = fetchTestCases(projectId, suiteId);
        testSuite.setCases(cases);
        return testSuite;
    }

    // Helper: parse cases array or single case from a JSON response into TestCase objects
    private List<TestCase> parseCasesFromResponse(String jsonResponse) throws IOException {
        JsonNode root = mapper.readTree(jsonResponse);
        List<TestCase> testCases = new ArrayList<>();

        JsonNode arrayNode = null;
        if (root.isArray()) {
            arrayNode = root;
        } else if (root.has("cases") && root.get("cases").isArray()) {
            arrayNode = root.get("cases");
        } else {
            Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode child = entry.getValue();
                if (child.isArray()) { arrayNode = child; break; }
            }
        }

        if (arrayNode != null) {
            for (JsonNode node : arrayNode) {
                TestCase tc = mapper.treeToValue(node, TestCase.class);
                testCases.add(tc);
            }
        } else if (root.isObject() && root.has("id") && root.has("title")) {
            TestCase tc = mapper.treeToValue(root, TestCase.class);
            testCases.add(tc);
        } else {
            log.warn("Unexpected TestRail response shape — no array of cases found.");
        }

        return testCases;
    }
}

