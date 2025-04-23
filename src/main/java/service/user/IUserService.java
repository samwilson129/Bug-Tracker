package main.java.service.user;

import main.java.model.User;
import java.util.List;

/**
 * Base interface for user services
 * Implements Interface Segregation Principle - defines common user operations
 * Implements Dependency Inversion Principle - high level modules depend on this abstraction
 */
public interface IUserService {
    User getUserById(int id);
    User getUserByEmail(String email);
    boolean updateUser(User user);
    boolean validateCredentials(String email, String password);
    boolean register(User user);
    List<User> getAllUsers();
}