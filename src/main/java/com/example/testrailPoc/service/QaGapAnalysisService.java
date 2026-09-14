package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.LatencyMatrix;
import com.example.testrailPoc.models.MissingTestCase;
import com.example.testrailPoc.models.RequirementGap;
import com.example.testrailPoc.models.TestRailCase;
import com.example.testrailPoc.models.TestSuite;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QaGapAnalysisService {
    private static final int DEFAULT_BATCH_SIZE = 20;

    private final TestCaseService testCaseService;
    private final JiraDataService jiraDataService;
    private final LlmRunbookClient llmRunbookClient;
    private final RunbookAssembler runbookAssembler;
    private final ExecutorService virtualThreadExecutor;

    public QaGapAnalysisService(
            TestCaseService testCaseService,
            JiraDataService jiraDataService,
            LlmRunbookClient llmRunbookClient,
            RunbookAssembler runbookAssembler) {
        this.testCaseService = testCaseService;
        this.jiraDataService = jiraDataService;
        this.llmRunbookClient = llmRunbookClient;
        this.runbookAssembler = runbookAssembler;
        this.virtualThreadExecutor = createVirtualThreadExecutor();
    }

    public String generateRunbook(int projectId, String jiraCsvPath, String outputPath) throws Exception {
        long totalStart = System.nanoTime();

        long jiraLoadStart = System.nanoTime();
        List<JiraRequirement> requirements = jiraDataService.loadRequirements(jiraCsvPath);
        long jiraLoadMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - jiraLoadStart);
        log.info("Requirements loaded from Jira in {} ms", jiraLoadMs);


        long testRailFetchStart = System.nanoTime();
        List<TestSuite> suites = testCaseService.fetchAllTestCasesForProject(projectId);
        long testRailFetchMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - testRailFetchStart);
        log.info("testcases fetched from TestRail in {} ms", testRailFetchMs);

        List<TestRailCase> testRailCases = TestRailCaseIndex.fromSuites(suites);
        Map<String, List<TestRailCase>> groupedByReference = TestRailCaseIndex.groupByReference(testRailCases);
        Set<String> jiraIds = requirements.stream()
                .map(JiraRequirement::issueKey)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());
        List<TestRailCase> unmapped = TestRailCaseIndex.findUnmapped(testRailCases, jiraIds);
        log.info("found {} unmapped test cases in TestRail", unmapped.size());

        ProcessedRequirements processed = processRequirementsInParallel(requirements, groupedByReference);
        LatencyMatrix latencyMatrix = new LatencyMatrix(
                jiraLoadMs,
                testRailFetchMs,
                processed.llmInferenceMs(),
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - totalStart),
                requirements == null ? 0 : requirements.size(),
                processed.batchCount(),
                unmapped.size());

        String markdown = runbookAssembler.assemble(processed.gaps(), unmapped);
        log.info("{}", latencyMatrix.toMarkdownTable());
        writeOutput(markdown, outputPath);
        return markdown;
    }

    private ProcessedRequirements processRequirementsInParallel(
            List<JiraRequirement> requirements,
            Map<String, List<TestRailCase>> groupedByReference) {
        if (requirements == null || requirements.isEmpty()) {
            return new ProcessedRequirements(Collections.emptyList(), 0L, 0);
        }

        List<List<JiraRequirement>> partitions = partition(requirements, DEFAULT_BATCH_SIZE);
        AtomicLong llmInferenceMs = new AtomicLong();
        List<CompletableFuture<List<RequirementGap>>> futures = partitions.stream()
                .map(partition -> CompletableFuture.supplyAsync(() -> analyzePartition(partition, groupedByReference, llmInferenceMs), virtualThreadExecutor))
                .toList();

        List<RequirementGap> gaps = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();

        return new ProcessedRequirements(gaps, llmInferenceMs.get(), partitions.size());
    }

    private List<RequirementGap> analyzePartition(
            List<JiraRequirement> partition,
            Map<String, List<TestRailCase>> groupedByReference,
            AtomicLong llmInferenceMs) {
        List<RequirementGap> results = new ArrayList<>();
        for (JiraRequirement requirement : partition) {
            List<TestRailCase> existingCases = groupedByReference.getOrDefault(requirement.issueKey(), Collections.emptyList());
            long start = System.nanoTime();
            List<MissingTestCase> missingCases = llmRunbookClient.generateMissingCases(requirement, existingCases);
            llmInferenceMs.addAndGet(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            log.info("LLM generated {} missing cases for requirement {} in {} ms", missingCases.size(), requirement.issueKey(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            if (missingCases != null && !missingCases.isEmpty()) {
                results.add(new RequirementGap(requirement, existingCases, missingCases));
            }
        }
        return results;
    }

    private List<List<JiraRequirement>> partition(List<JiraRequirement> requirements, int batchSize) {
        List<List<JiraRequirement>> partitions = new ArrayList<>();
        for (int start = 0; start < requirements.size(); start += batchSize) {
            int end = Math.min(start + batchSize, requirements.size());
            partitions.add(requirements.subList(start, end));
        }
        return partitions;
    }

    private void writeOutput(String markdown, String outputPath) throws IOException {
        String resolvedOutputPath = outputPath == null || outputPath.isBlank() ? "qa-runbook.md" : outputPath;
        Path outputFile = Path.of(resolvedOutputPath);
        Path parent = outputFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(outputFile, markdown, StandardCharsets.UTF_8);
    }

    @PreDestroy
    public void shutdown() {
        virtualThreadExecutor.shutdown();
    }

    private static ExecutorService createVirtualThreadExecutor() {
        try {
            Method newVirtualThreadPerTaskExecutor = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            return (ExecutorService) newVirtualThreadPerTaskExecutor.invoke(null);
        } catch (ReflectiveOperationException ex) {
            return Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
        }
    }

    private record ProcessedRequirements(List<RequirementGap> gaps, long llmInferenceMs, int batchCount) {
    }
}

