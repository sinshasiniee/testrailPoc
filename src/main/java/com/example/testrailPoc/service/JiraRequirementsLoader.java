package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;

@Deprecated
public class JiraRequirementsLoader {
    private final CsvJiraDataServiceImpl csvJiraDataService;

    public JiraRequirementsLoader(ResourceLoader resourceLoader) {
        this.csvJiraDataService = new CsvJiraDataServiceImpl(resourceLoader);
    }

    public List<JiraRequirement> load(String jiraCsvPath) throws IOException, CsvValidationException {
        return csvJiraDataService.loadRequirements(jiraCsvPath);
    }
}
