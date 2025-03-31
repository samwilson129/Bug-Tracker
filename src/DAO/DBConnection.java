package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "bugtracker";
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Establish connection and initialize the database if needed
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
        initializeDatabase(conn);
        return conn;
    }

    // Database & Table Initialization Logic
    private static void initializeDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Create Users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) UNIQUE NOT NULL, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "role ENUM('Administrator', 'ProjectManager', 'Developer', 'Tester') NOT NULL)";
            stmt.executeUpdate(createUsersTable);

            // Create Projects table
            String createProjectsTable = "CREATE TABLE IF NOT EXISTS Projects ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "bugs TEXT)";
            stmt.executeUpdate(createProjectsTable);

            // Create Bugs table
            String createBugsTable = "CREATE TABLE IF NOT EXISTS Bugs ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "status ENUM('Reported', 'In Progress', 'Fixed', 'Verified', 'Closed') DEFAULT 'Reported', "
                    + "assigned_to INT, "
                    + "project_id INT, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (assigned_to) REFERENCES Users(id) ON DELETE SET NULL, "
                    + "FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createBugsTable);

            // Create Bug History table
            String createBugHistoryTable = "CREATE TABLE IF NOT EXISTS BugHistory ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "bug_id INT, "
                    + "status ENUM('Reported', 'In Progress', 'Fixed', 'Verified', 'Closed') NOT NULL, "
                    + "updated_by INT, "
                    + "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (bug_id) REFERENCES Bugs(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (updated_by) REFERENCES Users(id) ON DELETE SET NULL)";
            stmt.executeUpdate(createBugHistoryTable);

            System.out.println("Database and tables checked/created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
