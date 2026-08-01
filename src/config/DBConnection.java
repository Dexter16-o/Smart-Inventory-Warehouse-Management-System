package config;

import java.sql.*;

public class DBConnection {

    // Database Details
    private static final String URL = "jdbc:mysql://localhost:3306/smart_inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to Get Connection
    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver Not Found!");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
        }
        return null;
    }
}