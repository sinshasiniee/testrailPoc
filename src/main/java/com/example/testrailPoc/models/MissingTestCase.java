package com.example.testrailPoc.models;

public record MissingTestCase(String type, String scenario, String expectedResult, String riskLevel) {
    public MissingTestCase {
        type = type == null ? "" : type.trim();
        scenario = scenario == null ? "" : scenario.trim();
        expectedResult = expectedResult == null ? "" : expectedResult.trim();
        riskLevel = riskLevel == null ? "Medium" : riskLevel.trim();
    }
}
