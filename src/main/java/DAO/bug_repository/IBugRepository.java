package main.java.DAO.bug_repository;

import main.java.model.Bug;

import java.util.List;


public interface IBugRepository {
    int addBug(Bug bug);
    Bug getBugById(int id);
    List<Bug> getAllBugs();
    boolean updateBug(Bug bug);
    boolean deleteBug(int id);
    List<Bug> getBugByReportedId(String reportedBy);
    int getAssignedBugCount(int userId);
    int getCompletedBugCount(int userId);
    List<Bug> getUnassignedBugs();
    List<Bug> getUnassignedBugsForProject(int projectId);
    boolean assignBugToUser(int bugId, int userId);
    int getActiveBugCount(int projectId);
    int getActiveProjectBugCount(int projectId);
}