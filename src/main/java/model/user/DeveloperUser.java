package main.java.model.user;

import java.sql.Timestamp;
import main.java.model.User.UserRole;

/**
 * Developer-specific user implementation
 * Implements Single Responsibility Principle - class has one responsibility
 * Implements Liskov Substitution Principle - can be used anywhere IUser is expected
 */
public class DeveloperUser extends AbstractUser {
    
    public DeveloperUser(int id, String name, String email, String password, Timestamp created_date) {
        super(id, name, email, password, UserRole.Developer, created_date);
    }
    
    // Developer-specific methods
    public boolean canFixBugs() {
        return true;
    }
    
    public boolean canViewAssignedBugs() {
        return true;
    }
    
    public boolean canLogWorkHours() {
        return true;
    }
}