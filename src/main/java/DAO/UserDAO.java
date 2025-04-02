package main.java.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.model.User;

public class UserDAO {

    // Add a new user
    public User addUser(User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getter_name());
            stmt.setString(2, user.getter_email());
            stmt.setString(3, user.getter_password());
            stmt.setString(4, user.getter_userrole().toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    
    // Get user by email
    // This method retrieves a user from the database using their email address.
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                User.UserRole role = User.UserRole.valueOf(rs.getString("role")); // Convert string to enum
                return new User(id, name, email, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user is not found
    }

    // Get a user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    User.UserRole.fromString(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    User.UserRole.fromString(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update user details
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getter_name());
            stmt.setString(2, user.getter_email());
            stmt.setString(3, user.getter_password());
            stmt.setString(4, user.getter_userrole().toString());
            stmt.setInt(5, user.getter_id());

            int updated = stmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a user
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
