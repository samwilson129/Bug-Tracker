package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = "jdbc:mysql://localhost:3306/bugtracker";
    private static final String USER = dotenv.get("DB_USER"); // from .env
    private static final String PASSWORD = dotenv.get("DB_PASSWORD"); // from .env

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}