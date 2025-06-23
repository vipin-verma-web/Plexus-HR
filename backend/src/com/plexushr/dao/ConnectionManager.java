package com.plexushr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    // !!! IMPORTANT: UPDATE THESE WITH YOUR ORACLE DATABASE CREDENTIALS !!!
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE"; // Replace XE with your SID/Service Name
    private static final String DB_USER = "vipinverma"; // Your Oracle Username
    private static final String DB_PASSWORD = "vipin"; // Your Oracle Password

    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the JDBC driver is loaded
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found. Make sure ojdbcX.jar is in the classpath.");
            e.printStackTrace();
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}