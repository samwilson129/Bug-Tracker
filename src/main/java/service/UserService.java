package main.java.service;

import main.java.DAO.*;
import main.java.model.*;
import main.java.model.User.UserRole;
import main.java.service.user.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Modified UserService to delegate to role-specific services
 * Implements Facade Pattern - simplifies interface to subsystems
 * Implements Single Responsibility Principle - each service has one responsibility
 */
public class UserService implements IUserService {
    private UserDAO userDAO;
    private BugDAO bugDAO;
    private ProjectDAO projectDAO;
    private ProjectService projectService = new ProjectService();
    
    // Role-specific service instances
    private IAdminService adminService;
    private IDeveloperService developerService;
    private IProjectManagerService projectManagerService;
    private ITesterService testerService;
    
    public UserService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
        this.projectDAO = new ProjectDAO();
        
        // Initialize role-specific services
        this.adminService = new AdminService();
        this.developerService = new DeveloperService();
        this.projectManagerService = new ProjectManagerService();
        this.testerService = new TesterService();
    }
    
    // Delegate admin operations
    public boolean deleteUser(String email) {
        return adminService.deleteUser(email);
    }
    
    public boolean deleteUserById(int id) {
        return adminService.deleteUserById(id);
    }
    
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
    
    public boolean isAdmin(User user) {
        return adminService.isAdmin(user);
    }
    
    // Delegate developer operations
    public boolean fixBug(int bugId, String developer) {
        return developerService.fixBug(bugId, developer);
    }
    
    public List<Bug> viewBug() {
        return bugDAO.getAllBugs();
    }
    
    // Delegate project manager operations
    public boolean assignBug(int bugId, String developer) {
        return projectManagerService.assignBug(bugId, developer);
    }
    
    // Delegate tester operations
    public void reportBug(String title, String description, String reportedBy, int projectId) {
        testerService.reportBug(title, description, reportedBy, projectId);
    }
    
    // Common operations from IUserService
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
        // UserDAO doesn't have this method, implement manually
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            return user.getter_password().equals(password);
        }
        return false;
    }
    
    @Override
    public boolean register(User user) {
        User addedUser = userDAO.addUser(user);
        return addedUser != null;  // Return true if user was successfully added
    }
    
    /**
     * Creates a new user account
     * Uses the Factory Pattern through UserFactory
     * 
     * @param username The username for the new user
     * @param email The email for the new user
     * @param password The password for the new user
     * @param role The role of the new user
     * @return True if user created successfully, false otherwise
     */
    //remove this later looks very generated
    public boolean addUser(String username, String email, String password, String role) {
        try {
            // Use the factory to create the appropriate user object
            User user = main.java.model.factory.UserFactory.createUser(username, email, password, role);
            
            // Register the user in the database
            User createdUser = userDAO.addUser(user);
            
            // Return success if the user was created
            return createdUser != null;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserProjects(int userId, List<String> projectNames) {
        try {
            
            // Get project IDs from project names
            List<Integer> projectIds = projectService.getAllProjects().stream()
                .filter(p -> projectNames.contains(p.getter_name()))
                .map(Project::getter_id)
                .collect(Collectors.toList());

            // Update the Project_Developers table
            return userDAO.updateUserProjects(userId, projectIds);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to update user projects: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Other methods that don't fit neatly into roles can remain here
    public List<Bug> fetchBugData() {
        return bugDAO.getAllBugs();
    }
    
    public boolean requestProject(String projectTitle, String requestedBy) {
        // Implementation remains here
        return true;
    }
}
