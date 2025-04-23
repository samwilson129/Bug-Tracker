package main.java.service.user;

import main.java.DAO.UserDAO;
import main.java.model.User;
import main.java.model.User.UserRole;
import java.util.List;

/**
 * Admin service implementation
 * Implements Single Responsibility Principle - handles admin-specific operations 
 */
public class AdminService implements IAdminService {
    private UserDAO userDAO;
    
    public AdminService() {
        this.userDAO = new UserDAO();
    }
    
    @Override
    public boolean deleteUser(String email) {
        // UserDAO only has deleteUser(int id), so first get user by email
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            // Then delete by ID
            return userDAO.deleteUser(user.getter_id());
        }
        return false;
    }
    
    @Override
    public boolean deleteUserById(int id) {
        // Just use the existing deleteUser(int id) method
        return userDAO.deleteUser(id);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    @Override
    public boolean assignRoleToUser(String email, String newRole) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            user.setter_userrole(UserRole.valueOf(newRole));
            return userDAO.updateUser(user);
        }
        return false;
    }
    
    @Override
    public boolean isAdmin(User user) {
        return user.getUserRole() == UserRole.Administrator;
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
        // UserDAO.addUser returns User, not boolean
        User result = userDAO.addUser(user);
        return result != null; // Success if user was returned
    }
}