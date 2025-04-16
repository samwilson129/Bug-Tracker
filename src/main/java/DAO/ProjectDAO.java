package main.java.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.java.model.Project;

public class ProjectDAO {

    public void addProject(Project project) {
        String insertProjectSQL = "INSERT INTO projects (id, name, description, bugs, manager_id) VALUES (?, ?, ?, ?, ?)";
        String insertDeveloperSQL = "INSERT INTO project_developers (project_id, developer_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            try (PreparedStatement projectStmt = conn.prepareStatement(insertProjectSQL);
                 PreparedStatement developerStmt = conn.prepareStatement(insertDeveloperSQL)) {

                // Insert into Projects
                projectStmt.setInt(1, project.getter_id());
                projectStmt.setString(2, project.getter_name());
                projectStmt.setString(3, project.getter_description());
                projectStmt.setString(4, String.join(",", project.getter_bugs()));
                projectStmt.setInt(5, project.getter_managerId());
                projectStmt.executeUpdate();

                // Insert into Project_Developers
                for (Integer devId : project.getter_developerIds()) {
                    developerStmt.setInt(1, project.getter_id());
                    developerStmt.setInt(2, devId);
                    developerStmt.addBatch();
                }
                developerStmt.executeBatch();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project getProjectById(int id) {
        String selectProjectSQL = "SELECT * FROM projects WHERE id = ?";
        String selectDevelopersSQL = "SELECT developer_id FROM project_developers WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement projectStmt = conn.prepareStatement(selectProjectSQL);
             PreparedStatement devStmt = conn.prepareStatement(selectDevelopersSQL)) {

            projectStmt.setInt(1, id);
            ResultSet rs = projectStmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                int managerId = rs.getInt("manager_id");

                List<String> bugs = (bugsStr != null && !bugsStr.isEmpty())
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();

                devStmt.setInt(1, id);
                ResultSet devRs = devStmt.executeQuery();
                List<Integer> developerIds = new ArrayList<>();
                while (devRs.next()) {
                    developerIds.add(devRs.getInt("developer_id"));
                }

                return new Project(id, name, description, bugs, managerId, developerIds);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getProjectsByManagerId(int managerId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE manager_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                String developerIdsStr = rs.getString("developer_ids");
    
                List<String> bugs = bugsStr != null && !bugsStr.isEmpty()
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();
    
                List<Integer> developerIds = developerIdsStr != null && !developerIdsStr.isEmpty()
                        ? Arrays.stream(developerIdsStr.split(",")).map(Integer::parseInt).toList()
                        : new ArrayList<>();
    
                Project project = new Project();
                project.setter_id(id);
                project.setter_name(name);
                project.setter_description(description);
                project.setter_bugs(bugs);
                project.setter_managerId(managerId);
                project.setter_developerIds(developerIds);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }


    public List<Project> getProjectsByDeveloperId(int developerId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE FIND_IN_SET(?, developer_ids) > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, String.valueOf(developerId));
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                String developerIdsStr = rs.getString("developer_ids");
                int managerId = rs.getInt("manager_id");
    
                List<String> bugs = bugsStr != null && !bugsStr.isEmpty()
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();
    
                List<Integer> developerIds = developerIdsStr != null && !developerIdsStr.isEmpty()
                        ? Arrays.stream(developerIdsStr.split(",")).map(Integer::parseInt).toList()
                        : new ArrayList<>();
    
                Project project = new Project();
                project.setter_id(id);
                project.setter_name(name);
                project.setter_description(description);
                project.setter_bugs(bugs);
                project.setter_managerId(managerId);
                project.setter_developerIds(developerIds);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
    


    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String selectAllProjectsSQL = "SELECT * FROM projects";
        String selectDevelopersSQL = "SELECT developer_id FROM project_developers WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement devStmt = conn.prepareStatement(selectDevelopersSQL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllProjectsSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String bugsStr = rs.getString("bugs");
                int managerId = rs.getInt("manager_id");

                List<String> bugs = (bugsStr != null && !bugsStr.isEmpty())
                        ? Arrays.asList(bugsStr.split(","))
                        : new ArrayList<>();

                devStmt.setInt(1, id);
                ResultSet devRs = devStmt.executeQuery();
                List<Integer> developerIds = new ArrayList<>();
                while (devRs.next()) {
                    developerIds.add(devRs.getInt("developer_id"));
                }

                projects.add(new Project(id, name, description, bugs, managerId, developerIds));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    public boolean updateProject(Project project) {
        String updateProjectSQL = "UPDATE projects SET name = ?, description = ?, bugs = ?, manager_id = ? WHERE id = ?";
        String deleteOldDevelopersSQL = "DELETE FROM project_developers WHERE project_id = ?";
        String insertDeveloperSQL = "INSERT INTO project_developers (project_id, developer_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);  // Begin transaction

            try (PreparedStatement updateStmt = conn.prepareStatement(updateProjectSQL);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteOldDevelopersSQL);
                 PreparedStatement insertStmt = conn.prepareStatement(insertDeveloperSQL)) {

                // Update Projects
                updateStmt.setString(1, project.getter_name());
                updateStmt.setString(2, project.getter_description());
                updateStmt.setString(3, String.join(",", project.getter_bugs()));
                updateStmt.setInt(4, project.getter_managerId());
                updateStmt.setInt(5, project.getter_id());
                int rowsUpdated = updateStmt.executeUpdate();

                // Clear old developer assignments
                deleteStmt.setInt(1, project.getter_id());
                deleteStmt.executeUpdate();

                // Add updated developer list
                for (Integer devId : project.getter_developerIds()) {
                    insertStmt.setInt(1, project.getter_id());
                    insertStmt.setInt(2, devId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();

                conn.commit();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProject(int id) {
        String deleteDevelopersSQL = "DELETE FROM project_developers WHERE project_id = ?";
        String deleteProjectSQL = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement devStmt = conn.prepareStatement(deleteDevelopersSQL);
                 PreparedStatement projStmt = conn.prepareStatement(deleteProjectSQL)) {

                devStmt.setInt(1, id);
                devStmt.executeUpdate();

                projStmt.setInt(1, id);
                int deleted = projStmt.executeUpdate();

                conn.commit();
                return deleted > 0;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}