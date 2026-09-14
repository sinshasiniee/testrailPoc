package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;

import java.io.IOException;
import java.util.List;

public interface JiraDataService {
    List<JiraRequirement> loadRequirements(String source) throws IOException;
}
