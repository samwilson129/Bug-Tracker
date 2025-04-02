package main.java.service;

import main.java.DAO.BugDAO;
import main.java.DAO.UserDAO;
import main.java.model.Bug;
import main.java.model.User;
import main.java.model.Bug.BugStatus;
import main.java.model.User.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private UserDAO userDAO;
    private BugDAO bugDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.bugDAO = new BugDAO();
    }

    // User Authentication
    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getter_password().equals(password)) {
            return user;
        }
        return null;
    }

    public void logout(User user) {
        System.out.println(user.getter_name() + " logged out.");
    }

    // User Management
    public void addUser(User user) {
        userDAO.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    // Bug Management
    public List<Bug> viewBug() {
        return bugDAO.getAllBugs();
    }

    // Tester reports a new bug
    public void reportBug(String title, String description, String reportedBy) {
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
        bugDAO.addBug(newBug);
        System.out.println("Bug reported successfully.");
    }

    // Developer fixes a bug and updates its status
    public boolean fixBug(int bugId, String developer) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null && bug.getter_bugstatus() == BugStatus.in_progress) {
            bug.setter_bugstatus(BugStatus.fixed);
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        System.out.println("Bug cannot be fixed or is not in progress.");
        return false;
    }

    // Project Manager assigns a bug to a developer
    public boolean assignBug(int bugId, String developer) {
        Bug bug = bugDAO.getBugById(bugId);
        if (bug != null) {
            bug.setter_assignedTo(developer);
            bug.setter_bugstatus(BugStatus.in_progress); // Change status to in progress
            bug.setter_updatedAt(LocalDateTime.now());
            return bugDAO.updateBug(bug);
        }
        System.out.println("Bug assignment failed.");
        return false;
    }

    // Administrator generates a bug report
    public void generateReport() {
        List<Bug> bugs = bugDAO.getAllBugs();
        System.out.println("Bug Report:");
        for (Bug bug : bugs) {
            System.out.println("ID: " + bug.getter_id() +
                               ", Title: " + bug.getter_title() +
                               ", Status: " + bug.getter_bugstatus() +
                               ", Assigned To: " + (bug.getter_assignedTo() != null ? bug.getter_assignedTo() : "Unassigned") +
                               ", Reported By: " + bug.getter_reportedBy() +
                               ", Updated At: " + bug.getter_updatedAt());
        }
    }

    public void manageUsers(User adminUser) {
        if (adminUser.getter_userrole() != UserRole.Administrator) {
            System.out.println("Access denied. Only administrators can manage users.");
            return;
        }

        //Placeholder implementation for user management menu: This will be done in the website.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- User Management Menu ---");
            System.out.println("1. Assign New Role to a User");
            System.out.println("2. Add New User");
            System.out.println("3. Delete User");
            System.out.println("4. Fetch Bug Data");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    assignUserRole(scanner);
                    break;
                case 2:
                    addNewUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4: 
                    fetchBugData();
                    break;
                case 5:
                    System.out.println("Exiting User Management...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void assignUserRole(Scanner scanner) {
        System.out.print("Enter the email of the user to modify: ");
        String email = scanner.nextLine();
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Current Role: " + user.getter_userrole());
        System.out.println("Available Roles: Administrator, ProjectManager, Developer, Tester");
        System.out.print("Enter new role: ");
        try {
            UserRole newRole = UserRole.fromString(scanner.nextLine());
            user.setter_userrole(newRole);
            if (userDAO.updateUser(user)) {
                System.out.println("User role updated successfully.");
            } else {
                System.out.println("Failed to update user role.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role entered.");
        }
    }

    private void addNewUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Available Roles: Administrator, ProjectManager, Developer, Tester");
        System.out.print("Enter role: ");

        try {
            UserRole userRole = UserRole.fromString(scanner.nextLine());
            User newUser = new User(0, username, password, email, userRole); // ID is auto-generated
            newUser = userDAO.addUser(newUser); // Add user and get assigned ID
            System.out.println("User added successfully with ID: " + newUser.getter_id());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role entered. User not added.");
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.print("Enter the email of the user to delete: ");
        String email = scanner.nextLine();
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.getter_userrole() == UserRole.Administrator) {
            System.out.println("Cannot delete an administrator.");
            return;
        }

        if (userDAO.deleteUser(user.getter_id())) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    public void requestProject() {
        // Placeholder: This function should allow project managers to request new projects.
        System.out.println("Requesting project... (Functionality to be implemented)");
    }

    public void fetchBugData() {
        // Placeholder: This function should allow administrators to fetch detailed bug data.
        
        List<Bug> bugList = bugDAO.getAllBugs();

        if (bugList.isEmpty()) {
            System.out.println("No bugs found in the database.");
            return;
        }

        System.out.println("\n--- Bug List ---");
        for (Bug bug : bugList) {
            System.out.println("ID: " + bug.getter_id());
            System.out.println("Title: " + bug.getter_title());
            System.out.println("Description: " + bug.getter_description());
            System.out.println("Status: " + bug.getter_bugstatus());
            System.out.println("Created At: " + bug.getter_createdAt());
            System.out.println("Updated At: " + bug.getter_updatedAt());
            System.out.println("Assigned To: " + (bug.getter_assignedTo() != null ? bug.getter_assignedTo() : "Unassigned"));
            System.out.println("Reported By: " + bug.getter_reportedBy());
            System.out.println("----------------------");
        }
    }
}
