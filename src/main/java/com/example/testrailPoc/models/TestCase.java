package com.example.testrailPoc.models;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    private int id;
    private String title;
    private int sectionId;
    private int typeId;
    private int priorityId;
    private int createdBy;
    private long createdOn;
    private String refs;
}
