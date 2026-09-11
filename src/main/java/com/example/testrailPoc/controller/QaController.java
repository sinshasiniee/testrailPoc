package com.example.testrailPoc.controller;

import com.example.testrailPoc.service.QaGapAnalysisService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QaController {
    private final QaGapAnalysisService qaGapAnalysisService;

    public QaController(QaGapAnalysisService qaGapAnalysisService) {
        this.qaGapAnalysisService = qaGapAnalysisService;
    }

    @GetMapping(value = "/qa/runbook", produces = MediaType.TEXT_MARKDOWN_VALUE)
    public String generateRunbook(
            @RequestParam int projectId,
            @RequestParam(required = false) String jiraCsvPath,
            @RequestParam(required = false, defaultValue = "") String outputPath) throws Exception {
        return qaGapAnalysisService.generateRunbook(projectId, jiraCsvPath, outputPath);
    }
}
