package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Report;

public class ReportDAO {

    // Add a new report
    public void addReport(Report report) {
        String sql = "INSERT INTO report (generated_by, project, bugs_summaries, generated_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, report.getter_generatedBy());
            stmt.setString(2, report.getter_project());
            stmt.setString(3, String.join(", ", report.getter_bugs_summaries())); // Convert List to CSV String
            stmt.setTimestamp(4, Timestamp.valueOf(report.getter_generatedDate()));
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get a report by ID
    public Report getReportById(int id) {
        String sql = "SELECT * FROM report WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                List<String> bugsSummaries = Arrays.asList(rs.getString("bugs_summaries").split(", "));
                return new Report(
                    rs.getInt("id"),
                    rs.getString("generated_by"),
                    rs.getString("project"),
                    bugsSummaries,
                    rs.getTimestamp("generated_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all reports
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM report";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                List<String> bugsSummaries = Arrays.asList(rs.getString("bugs_summaries").split(", "));
                reports.add(new Report(
                    rs.getInt("id"),
                    rs.getString("generated_by"),
                    rs.getString("project"),
                    bugsSummaries,
                    rs.getTimestamp("generated_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    // Delete a report by ID
    public boolean deleteReport(int id) {
        String sql = "DELETE FROM report WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // Returns true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
