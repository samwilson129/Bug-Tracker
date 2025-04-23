package main.java.model.user;

import java.sql.Timestamp;
import main.java.model.User.UserRole;

/**
 * Tester-specific user implementation
 * Implements Single Responsibility Principle - class has one responsibility
 * Implements Liskov Substitution Principle - can be used anywhere IUser is expected
 */
public class TesterUser extends AbstractUser {
    
    public TesterUser(int id, String name, String email, String password, Timestamp created_date) {
        super(id, name, email, password, UserRole.Tester, created_date);
    }
    
    // Tester-specific methods
    public boolean canCreateBugReports() {
        return true;
    }
    
    public boolean canVerifyBugFixes() {
        return true;
    }
    
    public boolean canCreateTestCases() {
        return true;
    }
}