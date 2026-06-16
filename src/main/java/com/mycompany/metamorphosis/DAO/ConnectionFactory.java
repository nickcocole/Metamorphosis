package com.mycompany.metamorphosis.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author eu
 */
public class ConnectionFactory {
    
    private static final String URL =
             "jdbc:postgresql://localhost:5432/Metamorphosis";

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgresql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    

    
}
