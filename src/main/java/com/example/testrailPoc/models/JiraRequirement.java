package com.example.testrailPoc.models;

public record JiraRequirement(String issueKey, String summary, String description, String priority, String status) {
    public JiraRequirement {
        issueKey = issueKey == null ? "" : issueKey.trim();
        summary = summary == null ? "" : summary.trim();
        description = description == null ? "" : description.trim();
        priority = priority == null ? "" : priority.trim();
        status = status == null ? "" : status.trim();
    }


    public String getIssueKey() {
        return issueKey;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }
}
