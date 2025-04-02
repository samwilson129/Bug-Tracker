package main.java.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.java.model.Project;

public class ProjectDAO {

    public void addProject(Project project) {
        String sql = "INSERT INTO projects (id, name, description, bugs) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, project.getter_id());
            stmt.setString(2, project.getter_name());
            stmt.setString(3, project.getter_description());
            stmt.setString(4, String.join(",", project.getter_bugs()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project getProjectById(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                List<String> bugs = bugsStr != null && !bugsStr.isEmpty()
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();
                return new Project(id, name, description, bugs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                List<String> bugs = bugsStr != null && !bugsStr.isEmpty()
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();
                projects.add(new Project(id, name, description, bugs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public boolean updateProject(Project project) {
        String sql = "UPDATE projects SET name = ?, description = ?, bugs = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getter_name());
            stmt.setString(2, project.getter_description());
            stmt.setString(3, String.join(",", project.getter_bugs()));
            stmt.setInt(4, project.getter_id());
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
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