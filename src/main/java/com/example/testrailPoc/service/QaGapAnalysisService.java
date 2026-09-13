package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.TestSuite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class QaGapAnalysisService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String QA_GAP_ANALYSIS_PROMPT_TEMPLATE = loadPromptTemplate();

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
        String prompt = QA_GAP_ANALYSIS_PROMPT_TEMPLATE.formatted(jiraJson, testRailJson);

        String llmOutput = llmRunbookClient.generate(prompt);
        if (llmOutput != null && !llmOutput.isBlank()) {
            return llmOutput.trim();
        }
        return null;
    }

    private static String loadPromptTemplate() {
        try (InputStream inputStream = QaGapAnalysisService.class
                .getClassLoader()
                .getResourceAsStream("prompts/qa-gap-analysis-prompt.txt")) {
            if (inputStream == null) {
                throw new IllegalStateException("Prompt template not found: prompts/qa-gap-analysis-prompt.txt");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load QA gap analysis prompt template", e);
        }
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
