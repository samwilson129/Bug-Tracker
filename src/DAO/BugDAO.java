package DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Bug;

public class BugDAO {

    public void addBug(Bug bug) {
        String sql = "insert into bugs (title,description,bugstatus,createdAt,updatedAt,assignedTo,reportedBy) values (?,?,?,?,?,?,?,?)";
        try (Connection conn=DBConnection.getConnection();
             PreparedStatement stmt=conn.prepareStatement(sql)) {
            // stmt.setInt(1,bug.getter_id());
            stmt.setString(1,bug.getter_title());
            stmt.setString(2,bug.getter_description());
            stmt.setString(3,bug.getter_bugstatus().name());
            stmt.setTimestamp(4,Timestamp.valueOf(bug.getter_createdAt()));
            stmt.setTimestamp(5,Timestamp.valueOf(bug.getter_updatedAt()));
            stmt.setString(6,bug.getter_assignedTo());
            stmt.setString(7,bug.getter_reportedBy());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Bug getBugById(int id) {
        String sql="select * from bugs where id = ?";
        try (Connection conn=DBConnection.getConnection();
             PreparedStatement stmt=conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String title=rs.getString("title");
                String description=rs.getString("description");
                Bug.BugStatus bugstatus=Bug.BugStatus.valueOf(rs.getString("bugstatus"));
                LocalDateTime createdAt=rs.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt=rs.getTimestamp("updatedAt").toLocalDateTime();
                String assignedTo=rs.getString("assignedTo");
                String reportedBy=rs.getString("reportedBy");
                return new Bug(id,title,description,bugstatus,createdAt,updatedAt,assignedTo,reportedBy);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Bug> getAllBugs() {
        List<Bug> bugs=new ArrayList<>();
        String sql="select * from bugs";
        try (Connection conn=DBConnection.getConnection();
             Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id=rs.getInt("id");
                String title=rs.getString("title");
                String description=rs.getString("description");
                Bug.BugStatus bugstatus=Bug.BugStatus.valueOf(rs.getString("bugstatus"));
                LocalDateTime createdAt=rs.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt=rs.getTimestamp("updatedAt").toLocalDateTime();
                String assignedTo=rs.getString("assignedTo");
                String reportedBy=rs.getString("reportedBy");
                bugs.add(new Bug(id,title,description,bugstatus,createdAt,updatedAt,assignedTo,reportedBy));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public boolean updateBug(Bug bug) {
        String sql="update bugs set title = ?, description = ?, bugstatus = ?, createdAt = ?, updatedAt = ?, assignedTo = ?, reportedBy = ? where id = ?";
        try (Connection conn=DBConnection.getConnection();
             PreparedStatement stmt=conn.prepareStatement(sql)) {
            stmt.setString(1,bug.getter_title());
            stmt.setString(2,bug.getter_description());
            stmt.setString(3,bug.getter_bugstatus().name());
            stmt.setTimestamp(4,Timestamp.valueOf(bug.getter_createdAt()));
            stmt.setTimestamp(5,Timestamp.valueOf(bug.getter_updatedAt()));
            stmt.setString(6,bug.getter_assignedTo());
            stmt.setString(7,bug.getter_reportedBy());
            stmt.setInt(8,bug.getter_id());
            int updated=stmt.executeUpdate();
            return updated>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBug(int id) {
        String sql="delete from bugs where id = ?";
        try (Connection conn=DBConnection.getConnection();
             PreparedStatement stmt=conn.prepareStatement(sql)) {
            stmt.setInt(1,id);
            int deleted=stmt.executeUpdate();
            return deleted>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
