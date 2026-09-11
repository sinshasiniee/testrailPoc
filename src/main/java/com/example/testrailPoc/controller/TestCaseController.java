package com.example.testrailPoc.controller;

import com.example.testrailPoc.models.TestSuite;
import com.example.testrailPoc.service.TestCaseService;
import com.example.testrailPoc.models.TestCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {
    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping
    public List<TestSuite> get(@RequestParam int projectId) throws Exception {
        return testCaseService.fetchAllTestCasesForProject(projectId);
    }
}