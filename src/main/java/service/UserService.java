package main.java.service;

import main.java.DAO.BugDAO;
import main.java.DAO.UserDAO;
import main.java.model.Bug;
import main.java.model.User;
import main.java.model.BugStatus;
import main.java.model.Priority;
import main.java.model.Project;
import main.java.model.User.UserRole;
import main.java.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private UserDAO userDAO;
    private BugDAO bugDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
    }

    // User Authentication
    public boolean login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getter_password().equals(password)) {
            return true;
        }
        return false;
    }

    public void logout(User user) {
        System.out.println(user.getter_name() + " logged out.");
    }

    // User Management
    public boolean addUser(String username, String email, String password, String role) {
        System.out.println("Adding user: " + username + ", " + email + ", " + role);
        try {
            UserRole userRole = UserRole.fromString(role);
            User user = new User(0, username, email, password, userRole); // ID is auto-generated
            userDAO.addUser(user);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add user: " + e.getMessage());
            return false;
        }
    }

    public boolean assignRoleToUser(String email, String newRole) {
        User user = userDAO.getUserByEmail(email);
        if (user == null) return false;

        try {
            UserRole userRole = UserRole.fromString(newRole);
            user.setter_userrole(userRole);
            return userDAO.updateUser(user);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean deleteUser(String email) {
        User user = userDAO.getUserByEmail(email);
        if (user == null || user.getter_userrole() == UserRole.Administrator) {
            return false;
        }
        return userDAO.deleteUser(user.getter_id());
    }

    public boolean deleteUserById(int id) {
        User user = userDAO.getUserById(id);
        if (user == null || user.getter_userrole() == UserRole.Administrator) {
            return false;
        }
        return userDAO.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Add this method after getAllUsers()
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }


    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean updateUserProjects(int userId, List<String> projectNames) {
        try {
            final ProjectService projectService = new ProjectService();
            // First get all project IDs from project names
            List<Integer> projectIds = projectService.getAllProjects().stream()
                .filter(p -> projectNames.contains(p.getter_name()))
                .map(Project::getter_id)
                .collect(Collectors.toList());

            // Update the Project_Developers table
            return userDAO.updateUserProjects(userId, projectIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Bug Management
    public List<Bug> viewBug() {
        return bugDAO.getAllBugs();
    }

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

    public boolean fixBug(int bugId, String developer) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null && bug.getter_bugstatus() == BugStatus.in_progress) {
            bug.setter_bugstatus(BugStatus.fixed);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        return false;
    }

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

    public List<Bug> fetchBugData() {
        return bugDAO.getAllBugs();
    }

    public boolean isAdmin(User user) {
        return user.getter_userrole() == UserRole.Administrator;
    }

    public boolean requestProject(String projectTitle, String requestedBy) {
        // TODO: Hook to ProjectDAO and persist project request
        System.out.println("Project request by " + requestedBy + ": " + projectTitle);
        return true;
    }
}
