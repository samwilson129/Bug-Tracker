package main.java.DAO;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.model.Priority;

public class BugDAO {

    // public int addBug(Bug bug) {
    // String sql = "insert into bugs
    // (title,description,status,priority,created_at,updated_at,assigned_to,reported_by,project_id)
    // values (?,?,?,?,?,?,?,?,?)";
    // try (Connection conn=DBConnection.getConnection();
    // PreparedStatement
    // stmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
    // // stmt.setInt(1,bug.getter_id());
    // stmt.setString(1,bug.getter_title());
    // stmt.setString(2,bug.getter_description());
    // stmt.setString(3,bug.getter_bugstatus().name());
    // stmt.setString(4, bug.getter_priority().name());
    // stmt.setTimestamp(5,Timestamp.valueOf(bug.getter_createdAt()));
    // stmt.setTimestamp(6,Timestamp.valueOf(bug.getter_updatedAt()));
    // stmt.setString(7,bug.getter_assignedTo());
    // stmt.setString(8,bug.getter_reportedBy());
    // stmt.setString(9, String.valueOf(bug.getter_projectId()));
    // stmt.executeUpdate();

    // ResultSet rs = stmt.getGeneratedKeys();
    // if (rs.next()) {
    // bug.setter_id(rs.getInt(1)); // update bug object with generated ID
    // }

    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return bug.getter_id();
    // }

    public int addBug(Bug bug) {
        String insertBugSQL = "INSERT INTO bugs (title, description, status, priority, created_at, updated_at, assigned_to, reported_by, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String fetchProjectBugsSQL = "SELECT bugs FROM projects WHERE id = ?";
        String updateProjectBugsSQL = "UPDATE projects SET bugs = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement insertBugStmt = conn.prepareStatement(insertBugSQL, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement fetchBugsStmt = conn.prepareStatement(fetchProjectBugsSQL);
                PreparedStatement updateBugsStmt = conn.prepareStatement(updateProjectBugsSQL)) {

            // Insert bug into the bugs table
            insertBugStmt.setString(1, bug.getter_title());
            insertBugStmt.setString(2, bug.getter_description());
            insertBugStmt.setString(3, bug.getter_bugstatus().name());
            insertBugStmt.setString(4, bug.getter_priority().name());
            insertBugStmt.setTimestamp(5, Timestamp.valueOf(bug.getter_createdAt()));
            insertBugStmt.setTimestamp(6, Timestamp.valueOf(bug.getter_updatedAt()));
            insertBugStmt.setString(7, bug.getter_assignedTo());
            insertBugStmt.setString(8, bug.getter_reportedBy());
            insertBugStmt.setString(9, String.valueOf(bug.getter_projectId()));
            insertBugStmt.executeUpdate();

            ResultSet rs = insertBugStmt.getGeneratedKeys();
            if (rs.next()) {
                bug.setter_id(rs.getInt(1));
            }

            // Fetch current bugs list from the project
            fetchBugsStmt.setInt(1, bug.getter_projectId());
            ResultSet bugsRs = fetchBugsStmt.executeQuery();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> bugList = new ArrayList<>();

            if (bugsRs.next()) {
                String bugsJson = bugsRs.getString("bugs");
                if (bugsJson != null && !bugsJson.isEmpty()) {
                    System.out.println("Fetching existing bugs from project JSON: " + bug.getter_projectId());
                    bugList = objectMapper.readValue(bugsJson, new TypeReference<List<Map<String, Object>>>() {
                    });
                }
            }

            // Add the new bug entry to the list
            Map<String, Object> newBugEntry = new HashMap<>();
            newBugEntry.put("id", bug.getter_id());
            newBugEntry.put("title", bug.getter_title());
            bugList.add(newBugEntry);

            // Update the project’s bugs field
            String updatedJson = objectMapper.writeValueAsString(bugList);
            System.out.println("Updating project bugs JSON: " + updatedJson);
            updateBugsStmt.setString(1, updatedJson);
            updateBugsStmt.setInt(2, bug.getter_projectId());
            updateBugsStmt.executeUpdate();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return bug.getter_id();
    }

    public Bug getBugById(int id) {
        String sql = "select * from bugs where id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                // Bug.BugStatus bugstatus = Bug.BugStatus.valueOf(rs.getString("status"));//
                // here changed from BugStatus.valueOf to Bug.BugStatus.valueOf
                BugStatus bugstatus = BugStatus.fromString(rs.getString("status"));
                Priority priority = Priority.valueOf(rs.getString("priority"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                String assignedTo = rs.getString("assigned_to");
                String reportedBy = rs.getString("reported_by");
                int projectId = rs.getInt("project_id");

                return new Bug(id, title, description, bugstatus, priority, createdAt, updatedAt, assignedTo,
                        reportedBy, projectId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Bug> getBugByReportedId(String reportedBy) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "select * from bugs where reported_by = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reportedBy);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                // Bug.BugStatus bugstatus = Bug.BugStatus.valueOf(rs.getString("status"));
                BugStatus bugstatus = BugStatus.fromString(rs.getString("status"));
                Priority priority = Priority.valueOf(rs.getString("priority"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                String assignedTo = rs.getString("assigned_to");
                int projectId = rs.getInt("project_id");

                bugs.add(new Bug(id, title, description, bugstatus, priority, createdAt, updatedAt, assignedTo,
                        reportedBy, projectId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public int getAssignedBugCount(int userId) {
        String sql = "SELECT COUNT(*) FROM Bugs WHERE assigned_to = ? AND status != 'CLOSED'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Bug> getUnassignedBugsForProject(int projectId) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM Bugs WHERE project_id = ? AND assigned_to IS NULL";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bug bug = new Bug();
                bug.setter_id(rs.getInt("id"));
                bug.setter_title(rs.getString("title"));
                bug.setter_description(rs.getString("description"));
                bug.setter_bugstatus(BugStatus.fromString(rs.getString("status")));
                bug.setter_priority(Priority.valueOf(rs.getString("priority")));
                bug.setter_createdAt(rs.getTimestamp("created_at").toLocalDateTime());
                bugs.add(bug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public int getCompletedBugCount(int userId) {
        String sql = "SELECT COUNT(*) FROM Bugs WHERE assigned_to = ? AND status = 'CLOSED'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Bug> getUnassignedBugs() {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM Bugs WHERE assigned_to IS NULL";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bug bug = new Bug();
                bug.setter_id(rs.getInt("id"));
                bug.setter_title(rs.getString("title"));
                bug.setter_description(rs.getString("description"));
                bug.setter_bugstatus(BugStatus.fromString(rs.getString("status")));
                bug.setter_priority(Priority.valueOf(rs.getString("priority")));
                bug.setter_createdAt(rs.getTimestamp("created_at").toLocalDateTime());
                bugs.add(bug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public boolean assignBugToUser(int bugId, int userId) {
        String sql = "UPDATE Bugs SET assigned_to = ?, status = 'IN_PROGRESS' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bugId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bug> getAllBugs() {
        List<Bug> bugs = new ArrayList<>();
        String sql = "select * from bugs";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                // Bug.BugStatus bugstatus = Bug.BugStatus.valueOf(rs.getString("status"));
                BugStatus bugstatus = BugStatus.fromString(rs.getString("status"));
                Priority priority = Priority.valueOf(rs.getString("priority"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                String assignedTo = rs.getString("assigned_to");
                String reportedBy = rs.getString("reported_by");
                int projectId = rs.getInt("project_id");

                bugs.add(new Bug(id, title, description, bugstatus, priority, createdAt, updatedAt, assignedTo,
                        reportedBy, projectId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    // public boolean updateBug(Bug bug) {
    // String sql="update bugs set title = ?, description = ?, status = ?,
    // priority=? ,created_at = ?, updated_at = ?, assigned_to = ?, reported_by = ?
    // , project_id = ? where id = ?";
    // try (Connection conn=DBConnection.getConnection();
    // PreparedStatement stmt=conn.prepareStatement(sql)) {
    // stmt.setString(1, bug.getter_title());
    // stmt.setString(2, bug.getter_description());
    // stmt.setString(3, bug.getter_bugstatus().name());
    // stmt.setString(4, bug.getter_priority().name());
    // stmt.setTimestamp(5, Timestamp.valueOf(bug.getter_createdAt()));
    // stmt.setTimestamp(6, Timestamp.valueOf(bug.getter_updatedAt()));
    // stmt.setString(7, bug.getter_assignedTo());
    // stmt.setString(8, bug.getter_reportedBy());
    // stmt.setString(9, String.valueOf(bug.getter_projectId()));
    // stmt.setInt(10, bug.getter_id());
    // int updated=stmt.executeUpdate();
    // return updated>0;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return false;
    // }

    public boolean updateBug(Bug bug) {
        String updateBugSQL = "UPDATE bugs SET title = ?, description = ?, status = ?, priority = ?, updated_at = ?, assigned_to = ? WHERE id = ?";
        String fetchProjectBugsSQL = "SELECT bugs FROM projects WHERE id = ?";
        String updateProjectBugsSQL = "UPDATE projects SET bugs = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement updateBugStmt = conn.prepareStatement(updateBugSQL);
                PreparedStatement fetchBugsStmt = conn.prepareStatement(fetchProjectBugsSQL);
                PreparedStatement updateBugsStmt = conn.prepareStatement(updateProjectBugsSQL)) {

            // Update the bug in the bugs table
            updateBugStmt.setString(1, bug.getter_title());
            updateBugStmt.setString(2, bug.getter_description());
            updateBugStmt.setString(3, bug.getter_bugstatus().name().toLowerCase());
            updateBugStmt.setString(4, bug.getter_priority().name());
            updateBugStmt.setTimestamp(5, Timestamp.valueOf(bug.getter_updatedAt()));
            updateBugStmt.setString(6, bug.getter_assignedTo());
            updateBugStmt.setInt(7, bug.getter_id());
            updateBugStmt.executeUpdate();

            // Fetch the current bugs JSON from the project
            fetchBugsStmt.setInt(1, bug.getter_projectId());
            ResultSet rs = fetchBugsStmt.executeQuery();

            if (rs.next()) {
                String bugsJson = rs.getString("bugs");

                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> bugList;

                if (bugsJson != null && !bugsJson.isEmpty()) {
                    bugList = objectMapper.readValue(bugsJson, new TypeReference<List<Map<String, Object>>>() {
                    });
                    for (Map<String, Object> bugObj : bugList) {
                        if (((Integer) bugObj.get("id")) == bug.getter_id()) {
                            System.out.println("Updating bug in project JSON: " + bug.getter_id());
                            bugObj.put("title", bug.getter_title());
                            break;
                        }
                    }
                    // Update the project table with modified bug list
                    String updatedJson = objectMapper.writeValueAsString(bugList);
                    updateBugsStmt.setString(1, updatedJson);
                    updateBugsStmt.setInt(2, bug.getter_projectId());
                    updateBugsStmt.executeUpdate();
                }
            }
            return true;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBug(int id) {
        String sql = "delete from bugs where id = ?";
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

    public int getActiveBugCount(int projectId) {
        String sql = "SELECT COUNT(*) FROM Bugs WHERE project_id = ? " +
                "AND status NOT IN ('FIXED', 'VERIFIED', 'CLOSED')";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("[DEBUG] Active bug count for project " + projectId + ": " + count);
                return count;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Error counting active bugs: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int getActiveProjectBugCount(int projectId) {
        String sql = "SELECT COUNT(*) FROM Bugs WHERE project_id = ? " +
                "AND status IN ('REPORTED', 'IN_PROGRESS')";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
