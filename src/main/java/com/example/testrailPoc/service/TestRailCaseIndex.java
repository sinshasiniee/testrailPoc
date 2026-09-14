package com.example.testrailPoc.service;

import com.example.testrailPoc.models.TestCase;
import com.example.testrailPoc.models.TestRailCase;
import com.example.testrailPoc.models.TestSuite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class TestRailCaseIndex {
    private TestRailCaseIndex() {
    }

    public static Map<String, List<TestRailCase>> groupByReference(List<TestRailCase> testRailCases) {
        return testRailCases.stream()
                .filter(tc -> tc.reference() != null && !tc.reference().isBlank())
                .collect(Collectors.groupingBy(
                        TestRailCase::reference,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    public static List<TestRailCase> findUnmapped(List<TestRailCase> testRailCases, Set<String> jiraIds) {
        return testRailCases.stream()
                .filter(tc -> tc.reference() == null || tc.reference().isEmpty() || !jiraIds.contains(tc.reference()))
                .toList();
    }

    public static List<TestRailCase> fromSuites(List<TestSuite> suites) {
        if (suites == null || suites.isEmpty()) {
            return Collections.emptyList();
        }

        List<TestRailCase> cases = new ArrayList<>();
        for (TestSuite suite : suites) {
            if (suite == null || suite.getCases() == null) {
                continue;
            }
            for (TestCase testCase : suite.getCases()) {
                if (testCase == null) {
                    continue;
                }
                String refs = testCase.getRefs() == null ? "" : testCase.getRefs();
                String reference = extractReference(refs);
                cases.add(new TestRailCase(
                        testCase.getId(),
                        testCase.getTitle(),
                        reference,
                        "case",
                        "unknown"));
            }
        }
        return cases;
    }

    private static String extractReference(String refs) {
        if (refs == null || refs.isBlank()) {
            return "";
        }
        String[] parts = refs.split(",");
        return parts[0].trim();
    }


}
