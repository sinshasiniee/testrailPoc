package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.TestSuite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class QaGapAnalysisService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TestCaseService testCaseService;
    private final JiraRequirementsLoader jiraRequirementsLoader;
    private final LlmRunbookClient llmRunbookClient;

    public QaGapAnalysisService(
            TestCaseService testCaseService,
            JiraRequirementsLoader jiraRequirementsLoader,
            LlmRunbookClient llmRunbookClient) {
        this.testCaseService = testCaseService;
        this.jiraRequirementsLoader = jiraRequirementsLoader;
        this.llmRunbookClient = llmRunbookClient;
    }

    public String generateRunbook(int projectId, String jiraCsvPath, String outputPath) throws Exception {
        List<TestSuite> suites = testCaseService.fetchAllTestCasesForProject(projectId);
        List<JiraRequirement> requirements = jiraRequirementsLoader.load(jiraCsvPath);


        String markdown = buildRunbook(suites, requirements);
        writeOutput(markdown, outputPath);
        return markdown;
    }


    private String buildRunbook( List<TestSuite> suites, List<JiraRequirement> requirements) throws IOException, InterruptedException {
        String testRailJson = buildJsonForTestSuites(suites);
        String jiraJson = buildJsonForRequirements(requirements);
        String prompt = """
                You are a QA assistant. I will provide you two inputs:
                1. A list of Jira requirements (with requirementId, requirementSummary, requirementDescription).
                2. A list of TestRail test cases (with testcaseId, title, and reference field that may contain a Jira requirement ID or link).
                
                Your task:
                - Compare Jira requirements against TestRail test cases by matching requirementId with the reference field in TestRail.
                - For each requirement, output:
                  - requirementId
                  - requirementSummary
                  - requirementDescription
                  - existingTestCases: array of titles of all test cases that reference this requirement
                  - missingTestCases: array of **multiple suggested test cases** that should exist but are missing, based on the requirement description. Be thorough and propose as many realistic test cases as possible to cover positive, negative, edge, and boundary scenarios.
                - If any TestRail test case has no reference to a Jira requirement, list it separately under "Unmapped Test Cases".
                - Ensure the output is in Markdown table format with columns:
                  requirementId, requirementSummary, requirementDescription, existingTestCases, missingTestCases.
                - Be exhaustive: every requirement should have a rich set of missing test cases if coverage is incomplete.
                
                """.formatted(jiraJson, testRailJson);

        String llmOutput = llmRunbookClient.generate(prompt);
        if (llmOutput != null && !llmOutput.isBlank()) {
            return llmOutput.trim();
        }
        return null;
    }

    private String buildJsonForTestSuites(List<TestSuite> suites) {
        try {
            return OBJECT_MAPPER.writeValueAsString(suites);
        } catch (IOException e) {
            return "[]";
        }
    }

    private String buildJsonForRequirements(List<JiraRequirement> requirements) {
        try {
            return OBJECT_MAPPER.writeValueAsString(requirements);
        } catch (IOException e) {
            return "[]";
        }
    }

    private void writeOutput(String markdown, String outputPath) throws IOException {
        String resolvedOutputPath = outputPath == null || outputPath.isBlank()
                ? "qa-runbook.md"
                : outputPath;
        Path outputFile = Path.of(resolvedOutputPath);
        Path parent = outputFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(outputFile, markdown, StandardCharsets.UTF_8);
    }
}
