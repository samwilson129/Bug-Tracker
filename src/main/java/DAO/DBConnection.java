package main.java.DAO;

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

    static {
        System.out.println("[DEBUG] DB_USER: " + USER);
        System.out.println("[DEBUG] DB_PASSWORD: " + PASSWORD);
    }

    // Main method to get connection and ensure database + tables exist
    public static Connection getConnection() throws SQLException {
        System.out.println("[DEBUG] Checking if database exists or needs to be created...");

        // Step 1: Connect to MySQL without selecting a database
        try (Connection tempConn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = tempConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("[DEBUG] Database '" + DB_NAME + "' ensured to exist.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to create database:");
            e.printStackTrace();
        }

        // Step 2: Connect to the actual database
        System.out.println("[DEBUG] Connecting to the database: " + DB_NAME);
        Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
        System.out.println("[DEBUG] Connection successful!");

        // Step 3: Initialize tables
        initializeDatabase(conn);
        return conn;
    }

    // Create tables if they don't exist
    private static void initializeDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("[DEBUG] Initializing database and tables...");

            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) UNIQUE NOT NULL, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "role ENUM('Administrator', 'ProjectManager', 'Developer', 'Tester') NOT NULL)";
            stmt.executeUpdate(createUsersTable);
            System.out.println("[DEBUG] Users table created or already exists.");

            String createProjectsTable = "CREATE TABLE IF NOT EXISTS Projects ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "bugs TEXT)";
            stmt.executeUpdate(createProjectsTable);
            System.out.println("[DEBUG] Projects table created or already exists.");

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
            System.out.println("[DEBUG] Bugs table created or already exists.");

            String createReportsTable = "CREATE TABLE IF NOT EXISTS Reports ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "generated_by VARCHAR(255) NOT NULL, "
                    + "project VARCHAR(255) NOT NULL, "
                    + "bugs_summaries TEXT, "
                    + "generated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createReportsTable);
            System.out.println("[DEBUG] Reports table created or already exists.");

            System.out.println("[DEBUG] Database and all tables initialized successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] Database initialization failed:");
            e.printStackTrace();
        }
    }
}
