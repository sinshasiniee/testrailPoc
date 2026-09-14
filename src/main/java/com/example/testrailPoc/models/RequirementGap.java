package com.example.testrailPoc.models;

import java.util.List;

public record RequirementGap(JiraRequirement requirement, List<TestRailCase> existingCases, List<MissingTestCase> missingCases) {
}
