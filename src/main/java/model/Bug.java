package main.java.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bug {
    public enum BugStatus {
        reported,
        in_progress,
        fixed,
        verified,
        closed;

        public static BugStatus fromString(String status) {
            System.out.println("Converting string to BugStatus: " + status);
            return BugStatus.valueOf(status.toLowerCase());
        }
    }

    public enum Priority {
        Low,
        Medium,
        High,
        Critical;
    }

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("bugstatus")
    private BugStatus bugstatus;

    @JsonProperty("priority")
    private Priority priority;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("assignedTo")
    private String assignedTo;

    @JsonProperty("reportedBy")
    private String reportedBy;

    @JsonProperty("project_id")
    private int project_id;

    public Bug(int id, String title, String description, BugStatus bugstatus,Priority priority, LocalDateTime createdAt,
            LocalDateTime updatedAt, String assignedTo, String reportedBy, int project_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bugstatus = bugstatus;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignedTo = assignedTo;
        this.reportedBy = reportedBy;
        this.project_id = project_id;
    }

    public Bug() {
        // Default constructor
    }

    public int getter_id() {
        return id;
    }

    public String getter_title() {
        return title;
    }

    public String getter_description() {
        return description;
    }

    public BugStatus getter_bugstatus() {
        return bugstatus;
    }

    public Priority getter_priority() {
        return priority;
    }

    public LocalDateTime getter_createdAt() {
        return createdAt;
    }

    public LocalDateTime getter_updatedAt() {
        return updatedAt;
    }

    public String getter_assignedTo() {
        return assignedTo;
    }

    public String getter_reportedBy() {
        return reportedBy;
    }

    public int getter_projectId() {
        return project_id;
    }

    public void setter_id(int id) {
        this.id = id;
    }

    public void setter_title(String title) {
        this.title = title;
    }

    public void setter_description(String description) {
        this.description = description;
    }

    public void setter_bugstatus(BugStatus bugstatus) {
        this.bugstatus = bugstatus;
    }

    public void setter_priority(Priority priority) {
        this.priority = priority;
    }

    public void setter_createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setter_updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setter_assignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setter_reportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setter_projectId(int project_id) {
        this.project_id = project_id;
    }

}
