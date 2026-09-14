package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class CsvJiraDataServiceImpl implements JiraDataService {
    private final ResourceLoader resourceLoader;

    public CsvJiraDataServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<JiraRequirement> loadRequirements(String source) throws IOException {
        Path csvPath = resolvePath(source);
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

                JiraRequirement requirement = new JiraRequirement(
                        readColumn(row, header, "Issue Key"),
                        readColumn(row, header, "Summary"),
                        readColumn(row, header, "Description"),
                        readColumn(row, header, "Priority"),
                        readColumn(row, header, "Status")
                );

                if (requirement.issueKey() == null || requirement.issueKey().isBlank()) {
                    continue;
                }
                requirements.add(requirement);
            }
        } catch (CsvValidationException e) {
            throw new IOException("Unable to parse Jira CSV requirements", e);
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
