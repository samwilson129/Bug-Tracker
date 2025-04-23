package main.java.model.user;

import java.sql.Timestamp;
import main.java.model.User.UserRole;

/**
 * ProjectManager-specific user implementation
 * Implements Single Responsibility Principle - class has one responsibility
 * Implements Liskov Substitution Principle - can be used anywhere IUser is expected
 */
public class ProjectManagerUser extends AbstractUser {
    
    public ProjectManagerUser(int id, String name, String email, String password, Timestamp created_date) {
        super(id, name, email, password, UserRole.ProjectManager, created_date);
    }
    
    // ProjectManager-specific methods
    public boolean canAssignBugs() {
        return true;
    }
    
    public boolean canCreateProjects() {
        return true;
    }
    
    public boolean canAssignDevelopers() {
        return true;
    }
    
    public boolean canGenerateReports() {
        return true;
    }
}