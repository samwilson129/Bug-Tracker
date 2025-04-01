package service;

import DAO.BugDAO;
import model.Bug;
import model.Bug.BugStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BugService {
    private final BugDAO bugDAO;

    public BugService() {
        this.bugDAO = new BugDAO();
    }

    // Report a new bug
    public Bug reportBug(String title, String description, String reportedBy) {
        Bug newBug = new Bug(
            0, // ID is auto-generated in the database
            title,
            description,
            BugStatus.reported, // Default status when reported
            LocalDateTime.now(),
            LocalDateTime.now(),
            null, // No developer assigned initially
            reportedBy
        );
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

    // Delete a bug
    public boolean deleteBug(int bugId) {
        return bugDAO.deleteBug(bugId);
    }

    // Fetch all bug data (for reports, views, etc.)
    public List<Bug> fetchBugData() {
        return bugDAO.getAllBugs();
    }
}
