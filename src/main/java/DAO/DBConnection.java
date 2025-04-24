package main.java.DAO;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    // Static instance - the only instance that will exist
    private static DBConnection instance;

    // Configuration properties remain static and final
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "bugtracker";
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Private constructor prevents instantiation from outside
    private DBConnection() {
        try {
            // Initialize database and tables on singleton creation
            ensureDatabaseExists();
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static synchronized method to get the instance
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            System.out.println("[DEBUG] Creating new DBConnection instance");
            instance = new DBConnection();
        }
        return instance;
    }

    // Method to get a connection
    public Connection getConnection() throws SQLException {
        System.out.println("[DEBUG] Connecting to the database: " + DB_NAME);
        Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
        System.out.println("[DEBUG] Connection successful!");
        return conn;
    }

    // Private method to ensure database exists
    private void ensureDatabaseExists() throws SQLException {
        System.out.println("[DEBUG] Checking if database exists or needs to be created...");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("[DEBUG] Database '" + DB_NAME + "' ensured to exist.");
        }
    }

    // Private method to initialize database schema
    private void initializeDatabase() throws SQLException {
        System.out.println("[DEBUG] Initializing database and tables...");
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Create Users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) UNIQUE NOT NULL, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "userrole ENUM('admin', 'manager', 'developer', 'tester') NOT NULL)";
            stmt.executeUpdate(createUsersTable);
            System.out.println("[DEBUG] Users table created or already exists.");

            // Create Projects table
            String createProjectsTable = "CREATE TABLE IF NOT EXISTS projects ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "bugs TEXT, "
                    + "manager_id INT, "
                    + "FOREIGN KEY (manager_id) REFERENCES users(id))";
            stmt.executeUpdate(createProjectsTable);
            System.out.println("[DEBUG] Projects table created or already exists.");

            // Create Project_Developers junction table
            String createProjectDevelopersTable = "CREATE TABLE IF NOT EXISTS project_developers ("
                    + "project_id INT, "
                    + "developer_id INT, "
                    + "PRIMARY KEY (project_id, developer_id), "
                    + "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (developer_id) REFERENCES users(id))";
            stmt.executeUpdate(createProjectDevelopersTable);
            System.out.println("[DEBUG] Project_Developers table created or already exists.");

            // Create Bugs table
            String createBugsTable = "CREATE TABLE IF NOT EXISTS bugs ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "description TEXT, "
                    + "status ENUM('reported', 'in_progress', 'fixed', 'verified', 'closed') DEFAULT 'reported', "
                    + "priority ENUM('low', 'medium', 'high', 'critical') DEFAULT 'medium', "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "assigned_to VARCHAR(255), "
                    + "reported_by VARCHAR(255), "
                    + "project_id INT, "
                    + "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createBugsTable);
            System.out.println("[DEBUG] Bugs table created or already exists.");

            // Create Reports table
            String createReportsTable = "CREATE TABLE IF NOT EXISTS reports ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "generated_by VARCHAR(255) NOT NULL, "
                    + "project VARCHAR(255) NOT NULL, "
                    + "bugs_summaries TEXT, "
                    + "generated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createReportsTable);
            System.out.println("[DEBUG] Reports table created or already exists.");

            System.out.println("[DEBUG] Database and all tables initialized successfully.");
        }
    }
}
