package com.example.testrailPoc.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestSuite {
    private int id;
    private String name;
    private String description;
    private List<TestCase> cases;
}
