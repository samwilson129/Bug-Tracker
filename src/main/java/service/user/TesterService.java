package main.java.service.user;

import main.java.DAO.UserDAO;
import main.java.DAO.BugDAO;
import main.java.model.User;
import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.model.Priority;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tester service implementation
 * Implements Single Responsibility Principle - handles tester-specific operations
 * Applies Interface Segregation - implements only relevant operations for testers
 */
public class TesterService implements ITesterService {
    private UserDAO userDAO;
    private BugDAO bugDAO;
    
    public TesterService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
    }
    
    @Override
    public void reportBug(String title, String description, String reportedBy, int projectId) {
        Bug newBug = new Bug(
            0,
            title,
            description,
            BugStatus.reported,
            Priority.Low,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            reportedBy,
            projectId
        );
        bugDAO.addBug(newBug);
    }
    
    @Override
    public boolean verifyBugFix(int bugId) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null && bug.getter_bugstatus() == BugStatus.fixed) {
            bug.setter_bugstatus(BugStatus.verified);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
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
        return addedUser != null;  // Return true if user was successfully added;
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}