package main.java.model;

import java.time.LocalDateTime;

public class Bug {
    public enum BugStatus {
        reported,
        in_progress,
        fixed,
        verified,
        closed;
    }

    private int id;
    private String title;
    private String description;
    private BugStatus bugstatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedTo;
    private String reportedBy;

    public Bug(int id, String title, String description, BugStatus bugstatus, LocalDateTime createdAt,
            LocalDateTime updatedAt, String assignedTo, String reportedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bugstatus = bugstatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignedTo = assignedTo;
        this.reportedBy = reportedBy;
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

}
