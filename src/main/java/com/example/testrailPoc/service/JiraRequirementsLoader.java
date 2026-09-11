package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class JiraRequirementsLoader {
    private final ResourceLoader resourceLoader;

    public JiraRequirementsLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<JiraRequirement> load(String jiraCsvPath) throws IOException, CsvValidationException {
        Path csvPath = resolvePath(jiraCsvPath);
        List<JiraRequirement> requirements = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {
            String[] header = csvReader.readNext();
            if (header == null) {
                return requirements;
            }

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                if (row.length == 0 || (row.length == 1 && row[0].isBlank())) {
                    continue;
                }

                JiraRequirement requirement = new JiraRequirement();
                requirement.setIssueKey(readColumn(row, header, "Issue Key"));
                requirement.setSummary(readColumn(row, header, "Summary"));
                requirement.setDescription(readColumn(row, header, "Description"));
                requirement.setPriority(readColumn(row, header, "Priority"));
                requirement.setStatus(readColumn(row, header, "Status"));

                if (requirement.getIssueKey() == null || requirement.getIssueKey().isBlank()) {
                    continue;
                }
                requirements.add(requirement);
            }
        }

        return requirements;
    }

    private Path resolvePath(String jiraCsvPath) throws IOException {
        if (jiraCsvPath == null || jiraCsvPath.isBlank()) {
            Resource defaultResource = new ClassPathResource("jiraData.csv");
            if (defaultResource.exists()) {
                return defaultResource.getFile().toPath();
            }
            throw new FileNotFoundException("Default Jira requirements CSV not found on classpath.");
        }

        if (jiraCsvPath.startsWith("classpath:")) {
            Resource resource = resourceLoader.getResource(jiraCsvPath);
            if (resource.exists()) {
                return resource.getFile().toPath();
            }
        }

        Path path = Path.of(jiraCsvPath);
        if (Files.exists(path)) {
            return path;
        }

        Resource resource = resourceLoader.getResource("classpath:" + jiraCsvPath);
        if (resource.exists()) {
            return resource.getFile().toPath();
        }

        throw new FileNotFoundException("Jira requirements CSV not found: " + jiraCsvPath);
    }

    private String readColumn(String[] row, String[] header, String columnName) {
        for (int i = 0; i < header.length; i++) {
            if (columnName.equalsIgnoreCase(header[i].trim())) {
                if (i < row.length) {
                    return row[i] == null ? "" : row[i].trim();
                }
            }
        }
        return "";
    }
}
