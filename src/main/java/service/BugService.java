package main.java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import main.java.DAO.BugDAO;
import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.model.Priority;
import main.java.DAO.bug_repository.IBugRepository;
import main.java.DAO.bug_strategy.AssignBugStrategy;
import main.java.DAO.bug_strategy.BugOperationStrategy;
import main.java.DAO.bug_strategy.UpdateStatusStrategy;

public class BugService {
    private final IBugRepository bugRepository;

    public BugService(IBugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }
    
    public BugService() {
        this.bugRepository = new BugDAO();
    }

    // Report a new bug
    public Bug reportBug(String title, String description, String reportedBy, int projectId) {
        Bug newBug = new Bug(
                0, // ID is auto-generated in the database
                title,
                description,
                BugStatus.reported, // Default status when reported
                Priority.Low, // Default priority
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, // No developer assigned initially
                reportedBy,
                projectId);
        int generatedId = bugRepository.addBug(newBug);
        newBug.setter_id(generatedId); // Set the auto-generated ID
        return newBug;
    }

    // Get bug by ID
    public Bug getBugById(int id) {
        return bugRepository.getBugById(id);
    }

    // Get all bugs
    public List<Bug> getAllBugs() {
        return bugRepository.getAllBugs();
    }

    // Assign a bug to a developer using Strategy Pattern
    public boolean assignBug(int bugId, String developerUsername) {
        Bug bug = bugRepository.getBugById(bugId);
        if (bug != null) {
            BugOperationStrategy strategy = new AssignBugStrategy(bugRepository, developerUsername);
            return strategy.execute(bug);
        }
        return false;
    }
    public boolean updateBugStatus(int bugId, BugStatus newStatus) {
        Bug bug = bugRepository.getBugById(bugId);
        if (bug != null) {
            // Using Strategy pattern
            BugOperationStrategy strategy = new UpdateStatusStrategy(bugRepository, newStatus);
            return strategy.execute(bug);
        }
        return false;
    }

    // Get bugs count assigned to a specific user
    public int getAssignedBugCount(int userId) {
        return bugRepository.getAssignedBugCount(userId);
    }

    // Get bugs count completed by a specific user
    public int getCompletedBugCount(int userId) {
        return bugRepository.getCompletedBugCount(userId);
    }

    // Get list of bugs unassigned to any developer
    public List<Bug> getUnassignedBugs() {
        return bugRepository.getUnassignedBugs();
    }

    // Get list of unassigned bugs for a specific project
    public List<Bug> getUnassignedBugsForProject(int projectId) {
        return bugRepository.getUnassignedBugsForProject(projectId);
    }

    // Get list of bugs assigned to a specific developer
    public boolean assignBugToUser(int bugId, int userId) {
        return bugRepository.assignBugToUser(bugId, userId);
    }

    // Delete a bug
    public boolean deleteBug(int bugId) {
        return bugRepository.deleteBug(bugId);
    }

    // Fetch all bug data (for reports, views, etc.)
    public List<Bug> fetchBugData() {
        return bugRepository.getAllBugs();
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
        return bugRepository.getActiveBugCount(projectId);
    }

    public int getActiveProjectBugCount(int projectId) {
        return bugRepository.getActiveProjectBugCount(projectId);
    }
}