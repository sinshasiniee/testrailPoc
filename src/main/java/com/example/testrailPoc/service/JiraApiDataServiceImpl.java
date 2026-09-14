package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JiraApiDataServiceImpl implements JiraDataService {
    @Override
    public List<JiraRequirement> loadRequirements(String source) throws IOException {
        return Collections.emptyList();
    }
}
