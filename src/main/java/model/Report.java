package main.java.model;

import java.util.*;
import java.time.LocalDateTime;

public class Report {
    private int id;
    private String generatedBy;
    private String project;
    private List<String> bugs_summaries;
    private LocalDateTime generatedDate;

    public Report(int id, String generatedBy, String project, List<String> bugs_summaries,
            LocalDateTime generatedDate) {
        this.id = id;
        this.generatedBy = generatedBy;
        this.project = project;
        this.bugs_summaries = bugs_summaries;
        this.generatedDate = generatedDate;
    }

    public int getter_id() {
        return id;
    }

    public String getter_generatedBy() {
        return generatedBy;
    }

    public String getter_project() {
        return project;
    }

    public List<String> getter_bugs_summaries() {
        return bugs_summaries;
    }

    public LocalDateTime getter_generatedDate() {
        return generatedDate;
    }

    public void setter_id(int id) {
        this.id = id;
    }

    public void setter_generatedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public void setter_project(String project) {
        this.project = project;
    }

    public void setter_bugs_summaries(List<String> bugs_summaries) {
        this.bugs_summaries = bugs_summaries;
    }

    public void setter_generatedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }
}
