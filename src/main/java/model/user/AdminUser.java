package main.java.model.user;

import java.sql.Timestamp;
import main.java.model.User.UserRole;

/**
 * Admin-specific user implementation
 * Implements Single Responsibility Principle - class has one responsibility
 * Implements Liskov Substitution Principle - can be used anywhere IUser is expected
 */
public class AdminUser extends AbstractUser {
    
    public AdminUser(int id, String name, String email, String password, Timestamp created_date) {
        super(id, name, email, password, UserRole.Administrator, created_date);
    }
    
    // Admin-specific methods
    public boolean canDeleteUser() {
        return true;
    }
    
    public boolean canManageRoles() {
        return true;
    }
    
    public boolean canConfigureSystem() {
        return true;
    }
}