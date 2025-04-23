package main.java.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.model.User;

public class UserDAO {

    // Add a new user
    public User addUser(User user) {
        System.out.println("[DEBUG] aaAdding user: " + user.getter_name() + ", " + user.getter_email() + ", "
                + user.getter_userrole());
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for addUser()");
            stmt.setString(1, user.getter_name());
            stmt.setString(2, user.getter_email());
            stmt.setString(3, user.getter_password());
            stmt.setString(4, user.getter_userrole().toString());

            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());
            stmt.executeUpdate();
            System.out.println("[DEBUG] User insertion successful.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    // Get user by email
    public User getUserByEmail(String email) {
        System.out.println("[DEBUG] Fetching user by email: " + email);
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for getUserByEmail()");
            stmt.setString(1, email);
            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("[DEBUG] User found: " + rs.getString("name"));
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                User.UserRole role = User.UserRole.valueOf(rs.getString("role"));
                return new User(id, name, email, password, role);
            } else {
                System.out.println("[DEBUG] No user found with email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Error retrieving user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get user by name
    public User getUserByName(String name) {
        System.out.println("[DEBUG] Fetching user by name: " + name);
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for getUserByName()");
            stmt.setString(1, name);
            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("[DEBUG] User found: " + rs.getString("name"));
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        User.UserRole.fromString(rs.getString("role")));
            } else {
                System.out.println("[DEBUG] No user found with name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Error retrieving user by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get a user by ID
    public User getUserById(int id) {
        System.out.println("[DEBUG] Fetching user by ID: " + id);
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for getUserById()");
            stmt.setInt(1, id);
            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("[DEBUG] User found: " + rs.getString("name"));
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        User.UserRole.fromString(rs.getString("role")));
            } else {
                System.out.println("[DEBUG] No user found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public List<User> getAllUsers() {
        System.out.println("[DEBUG] Fetching all users...");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("[DEBUG] DB Connection established for getAllUsers()");
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        User.UserRole.fromString(rs.getString("role")),
                        rs.getTimestamp("created_date"));
                users.add(user);
                System.out.println("[DEBUG] Retrieved user: " + user.getter_name());
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Error fetching all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    // Update user details
    public boolean updateUser(User user) {
        System.out.println("[DEBUG] Updating user ID: " + user.getter_id());
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for updateUser()");
            stmt.setString(1, user.getter_name());
            stmt.setString(2, user.getter_email());
            stmt.setString(3, user.getter_password());
            stmt.setString(4, user.getter_userrole().toString());
            stmt.setInt(5, user.getter_id());

            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());
            int updated = stmt.executeUpdate();
            System.out.println("[DEBUG] Rows updated: " + updated);
            return updated > 0;

        } catch (SQLException e) {
            System.out.println("[ERROR] Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserProjects(int userId, List<Integer> projectIds) {
        String deleteSQL = "DELETE FROM Project_Developers WHERE developer_id = ?";
        String insertSQL = "INSERT INTO Project_Developers (project_id, developer_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // First delete all existing assignments
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                    deleteStmt.setInt(1, userId);
                    deleteStmt.executeUpdate();
                }

                // Then insert new assignments
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    for (Integer projectId : projectIds) {
                        insertStmt.setInt(1, projectId);
                        insertStmt.setInt(2, userId);
                        insertStmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a user
    public boolean deleteUser(int id) {
        System.out.println("[DEBUG] Deleting user with ID: " + id);
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("[DEBUG] DB Connection established for deleteUser()");
            stmt.setInt(1, id);
            System.out.println("[DEBUG] Executing SQL: " + stmt.toString());

            int deleted = stmt.executeUpdate();
            System.out.println("[DEBUG] Rows deleted: " + deleted);
            return deleted > 0;

        } catch (SQLException e) {
            System.out.println("[ERROR] Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
