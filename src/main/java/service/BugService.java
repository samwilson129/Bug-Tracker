package main.java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import main.java.DAO.BugDAO;
import main.java.model.Bug;
import main.java.model.Bug.BugStatus;

public class BugService {
    private final BugDAO bugDAO;

    public BugService() {
        this.bugDAO = new BugDAO();
    }

    // Report a new bug
    public Bug reportBug(String title, String description, String reportedBy, int projectId) {
        Bug newBug = new Bug(
                0, // ID is auto-generated in the database
                title,
                description,
                BugStatus.reported, // Default status when reported
                Bug.Priority.Low, // Default priority
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, // No developer assigned initially
                reportedBy,
                projectId);
        int generatedId = bugDAO.addBug(newBug);
        newBug.setter_id(generatedId); // Set the auto-generated ID
        return newBug;
    }

    // Get bug by ID
    public Bug getBugById(int id) {
        return bugDAO.getBugById(id);
    }

    // Get all bugs
    public List<Bug> getAllBugs() {
        return bugDAO.getAllBugs();
    }

    // Assign a bug to a developer
    public boolean assignBug(int bugId, String developerUsername) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null) {
            bug.setter_assignedTo(developerUsername);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
    }

    // Update bug status
    public boolean updateBugStatus(int bugId, BugStatus newStatus) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null) {
            bug.setter_bugstatus(newStatus);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
    }

    // Get bugs count assigned to a specific user
    public int getAssignedBugCount(int userId) {
        return bugDAO.getAssignedBugCount(userId);
    }

    // Get bugs count completed by a specific user
    public int getCompletedBugCount(int userId) {
        return bugDAO.getCompletedBugCount(userId);
    }

    // Get list of bugs unassigned to any developer
    public List<Bug> getUnassignedBugs() {
        return bugDAO.getUnassignedBugs();
    }

    // Get list of unassigned bugs for a specific project
    public List<Bug> getUnassignedBugsForProject(int projectId) {
        return bugDAO.getUnassignedBugsForProject(projectId);
    }

    // Get list of bugs assigned to a specific developer
    public boolean assignBugToUser(int bugId, int userId) {
        return bugDAO.assignBugToUser(bugId, userId);
    }

    // Delete a bug
    public boolean deleteBug(int bugId) {
        return bugDAO.deleteBug(bugId);
    }

    // Fetch all bug data (for reports, views, etc.)
    public List<Bug> fetchBugData() {
        return bugDAO.getAllBugs();
    }

    // Get list of bugs by project ID
    public List<Bug> getBugsByProjectId(int projectId) {
        List<Bug> allBugs = getAllBugs();
        return allBugs.stream()
                .filter(bug -> bug.getter_projectId() == projectId)
                .collect(Collectors.toList());
    }

    // Get list of active bugs
    public int getActiveBugCount(int projectId) {
        return bugDAO.getActiveBugCount(projectId);
    }

    public int getActiveProjectBugCount(int projectId) {
        return bugDAO.getActiveProjectBugCount(projectId);
    }
}
