package main.java.service.user;

import main.java.DAO.UserDAO;
import main.java.DAO.BugDAO;
import main.java.DAO.ProjectDAO;
import main.java.model.User;
import main.java.model.Bug;
import main.java.model.Project;
import main.java.model.BugStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * ProjectManager service implementation
 * Implements Single Responsibility Principle - handles project manager-specific operations
 * Follows Dependency Inversion - depends on abstractions, not concrete implementations 
 */
public class ProjectManagerService implements IProjectManagerService {
    private UserDAO userDAO;
    private BugDAO bugDAO;
    private ProjectDAO projectDAO;
    
    public ProjectManagerService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
        this.projectDAO = new ProjectDAO();
    }
    
    @Override
    public boolean assignBug(int bugId, String developer) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null) {
            bug.setter_assignedTo(developer);
            bug.setter_bugstatus(BugStatus.in_progress);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
    }
    
    @Override
    public boolean createProject(String name, String description, int managerId) {
        Project project = new Project(0, name, description, new ArrayList<>(), managerId, new ArrayList<>());
        try {
            projectDAO.addProject(project);
            // If we get here without exceptions, consider it a success
            return true;
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error creating project: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Project> getProjectsForManager(int managerId) {
        return projectDAO.getProjectsByManagerId(managerId);
    }
    
    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
    
    @Override
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }
    
    @Override
    public boolean validateCredentials(String email, String password) {
        // Get the user by email
        User user = userDAO.getUserByEmail(email);
        
        // If user exists, check password
        if (user != null) {
            // Compare provided password with stored password
            return user.getter_password().equals(password);
        }
        
        // User not found, invalid credentials
        return false;
    }
    
    @Override
    public boolean register(User user) {
        User addedUser = userDAO.addUser(user);
        return addedUser != null;  // Return true if user was successfully added
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}