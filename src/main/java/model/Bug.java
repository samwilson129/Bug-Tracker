package main.java.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bug {
    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("bugstatus")
    private BugStatus bugStatus;

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
    private int projectId;

    // Default constructor
    public Bug() {
    }

    // Full constructor
    public Bug(int id, String title, String description, BugStatus bugStatus, Priority priority, 
               LocalDateTime createdAt, LocalDateTime updatedAt, String assignedTo, 
               String reportedBy, int projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bugStatus = bugStatus;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignedTo = assignedTo;
        this.reportedBy = reportedBy;
        this.projectId = projectId;
    }

    // Standard getters and setters with consistent naming
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BugStatus getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(BugStatus bugStatus) {
        this.bugStatus = bugStatus;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    // Legacy getter methods for backward compatibility
    public int getter_id() {
        return getId();
    }

    public String getter_title() {
        return getTitle();
    }

    public String getter_description() {
        return getDescription();
    }

    public BugStatus getter_bugstatus() {
        return getBugStatus();
    }

    public Priority getter_priority() {
        return getPriority();
    }

    public LocalDateTime getter_createdAt() {
        return getCreatedAt();
    }

    public LocalDateTime getter_updatedAt() {
        return getUpdatedAt();
    }

    public String getter_assignedTo() {
        return getAssignedTo();
    }

    public String getter_reportedBy() {
        return getReportedBy();
    }

    public int getter_projectId() {
        return getProjectId();
    }

    // Legacy setter methods for backward compatibility
    public void setter_id(int id) {
        setId(id);
    }

    public void setter_title(String title) {
        setTitle(title);
    }

    public void setter_description(String description) {
        setDescription(description);
    }

    public void setter_bugstatus(BugStatus bugStatus) {
        setBugStatus(bugStatus);
    }

    public void setter_priority(Priority priority) {
        setPriority(priority);
    }

    public void setter_createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
    }

    public void setter_updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
    }

    public void setter_assignedTo(String assignedTo) {
        setAssignedTo(assignedTo);
    }

    public void setter_reportedBy(String reportedBy) {
        setReportedBy(reportedBy);
    }

    public void setter_projectId(int projectId) {
        setProjectId(projectId);
    }
}