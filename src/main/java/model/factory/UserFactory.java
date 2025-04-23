package main.java.model.factory;

import main.java.model.User;
import main.java.model.user.*;
import main.java.model.User.UserRole;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Factory for creating different user types
 * Implements Factory Method Pattern - creates objects without specifying concrete classes
 * Implements Single Responsibility Principle - factory is only responsible for creating users
 */
public class UserFactory {
    /**
     * Creates a user of the appropriate type based on the role
     * Factory Method Pattern implementation
     */
    public static User createUser(String name, String email, String password, String role) {
        UserRole userRole = UserRole.valueOf(role);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        System.out.println("Creating user with role inside UserFactory: " + userRole);
        
        // Still returns User objects for backward compatibility
        return new User(0, name, email, password, userRole, currentTime);
    }
    
    /**
     * Creates a specialized user object based on role
     * Direct implementation of Factory Pattern
     */
    public static IUser createSpecializedUser(String name, String email, String password, String role) {
        UserRole userRole = UserRole.valueOf(role);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        
        switch (userRole) {
            case Administrator:
                return new AdminUser(0, name, email, password, currentTime);
                
            case ProjectManager:
                return new ProjectManagerUser(0, name, email, password, currentTime);
                
            case Developer:
                return new DeveloperUser(0, name, email, password, currentTime);
                
            case Tester:
                return new TesterUser(0, name, email, password, currentTime);
                
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
}