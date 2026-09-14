package com.example.testrailPoc.models;

public record LatencyMatrix(
        long jiraLoadMs,
        long testRailFetchMs,
        long llmInferenceMs,
        long totalMs,
        int requirementCount,
        int batchCount,
        int unmappedCount) {

    public String toMarkdownTable() {
        return "\n## Latency Matrix\n\n" +
                "| Metric | Duration (ms) | Notes |\n" +
                "| :--- | ---: | :--- |\n" +
                "| Jira CSV load | " + jiraLoadMs + " | Time spent loading and normalizing Jira requirement rows |\n" +
                "| TestRail fetch | " + testRailFetchMs + " | Time spent fetching and parsing all TestRail suites/cases |\n" +
                "| LLM inference | " + llmInferenceMs + " | Aggregate time spent across requirement partitions |\n" +
                "| Total processing | " + totalMs + " | End-to-end runbook generation time |\n" +
                "| Requirement count | " + requirementCount + " | Jira requirements processed |\n" +
                "| Batch count | " + batchCount + " | Partitions processed concurrently |\n" +
                "| Unmapped TestRail cases | " + unmappedCount + " | Cases without a matching Jira reference |\n";
    }
}
