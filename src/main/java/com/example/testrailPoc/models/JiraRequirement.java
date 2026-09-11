package com.example.testrailPoc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JiraRequirement {
    private String issueKey;
    private String summary;
    private String description;
    private String priority;
    private String status;
}
