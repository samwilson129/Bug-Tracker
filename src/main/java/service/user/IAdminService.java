package main.java.service.user;

import main.java.model.User;
import java.util.List;

/**
 * Admin-specific service interface
 * Implements Interface Segregation Principle - clients only depend on methods they use
 */
public interface IAdminService extends IUserService {
    boolean deleteUser(String email);
    boolean deleteUserById(int id);
    boolean assignRoleToUser(String email, String newRole);
    boolean isAdmin(User user);
}