package main.java.service.user;

import main.java.DAO.UserDAO;
import main.java.DAO.BugDAO;
import main.java.model.User;
import main.java.model.Bug;
import main.java.model.BugStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer service implementation
 * Implements Single Responsibility Principle - handles developer-specific operations
 * Applies Open/Closed Principle - can be extended without modifying core functionality
 */
public class DeveloperService implements IDeveloperService {
    private UserDAO userDAO;
    private BugDAO bugDAO;
    
    public DeveloperService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
    }
    
    @Override
    public boolean fixBug(int bugId, String developer) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null && bug.getter_bugstatus() == BugStatus.in_progress) {
            bug.setter_bugstatus(BugStatus.fixed);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
    }
    
    @Override
    public Bug viewBug(int bugId) {
        return bugDAO.getBugById(bugId);
    }
    
    @Override
    public List<Bug> getBugsAssignedTo(String developerEmail) {
        User developer = userDAO.getUserByEmail(developerEmail);
        if (developer != null) {
            return bugDAO.getAllBugs().stream()
                    .filter(b -> b.getter_assignedTo() != null && 
                           developer.getter_id() == Integer.parseInt(b.getter_assignedTo()))
                    .collect(Collectors.toList());
        }
        return List.of();
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