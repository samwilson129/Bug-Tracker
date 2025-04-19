package main.java.model.factory;

import main.java.model.User;
import main.java.model.User.UserRole;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserFactory {
    public static User createUser(String name, String email, String password, String role) {
        UserRole userRole = UserRole.valueOf(role);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        System.out.println("Creating user with role inside UserFactory: " + userRole);
        
        switch (userRole) {
            case Administrator:
                return new User(0, name, email, password, UserRole.Administrator, currentTime);
                
            case ProjectManager:
                return new User(0, name, email, password, UserRole.ProjectManager, currentTime);
                
            case Developer:
                return new User(0, name, email, password, UserRole.Developer, currentTime);
                
            case Tester:
                return new User(0, name, email, password, UserRole.Tester, currentTime);
                
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
}