package com.example.testrailPoc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QaGapAnalysisServiceTest {/*

    @Test
    void analyzeRequirementsIdentifiesMissingCoverage() throws Exception {
        TestCaseService fakeService = new TestCaseService(null) {
            @Override
            public List<TestSuite> fetchAllTestCasesForProject(int projectId) {
                TestCase one = new TestCase();
                one.setId(101);
                one.setTitle("Login happy path");
                one.setRefs("RQ001");

                TestCase two = new TestCase();
                two.setId(102);
                two.setTitle("Invalid credentials");
                two.setRefs("RQ002");

                TestSuite suite = new TestSuite();
                suite.setId(1);
                suite.setName("Regression Suite");
                suite.setCases(List.of(one, two));
                return List.of(suite);
            }
        };

        JiraRequirementsLoader loader = new JiraRequirementsLoader(new DefaultResourceLoader());
        QaGapAnalysisService service = new QaGapAnalysisService(
                fakeService,
                loader,
                new MarkdownRunbookService(),
                new LlmRunbookClient(new ConfigLoader())
        );

        List<RequirementGapAnalysis> analyses = service.analyzeRequirements(3, "classpath:jiraData.csv");
        assertFalse(analyses.isEmpty());

        RequirementGapAnalysis loginAnalysis = analyses.stream()
                .filter(item -> "RQ001".equals(item.getRequirementId()))
                .findFirst()
                .orElseThrow();

        assertFalse(loginAnalysis.getExistingTestCases().isEmpty());
        assertFalse(loginAnalysis.getMissingTestCases().isEmpty());

        RequirementGapAnalysis invalidLoginAnalysis = analyses.stream()
                .filter(item -> "RQ002".equals(item.getRequirementId()))
                .findFirst()
                .orElseThrow();

        assertTrue(invalidLoginAnalysis.getExistingTestCases().stream().anyMatch(title -> title.contains("Invalid")));
    }*/
}
