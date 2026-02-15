package it.uninafoodlab.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static Connection connection = null;
    private static Properties dbProperties = null;
    
    private DatabaseConnection() {}
    
    /**
     * Carica le proprietà dal file db.properties
     */
    private static Properties loadProperties() {
        if (dbProperties == null) {
            dbProperties = new Properties();
            try (InputStream input = DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {
                
                if (input == null) {
                    System.err.println("✗ File db.properties non trovato!");
                    return null;
                }
                
                dbProperties.load(input);
                System.out.println("✓ Configurazione database caricata");
                
            } catch (IOException e) {
                System.err.println("✗ Errore caricamento db.properties");
                e.printStackTrace();
                return null;
            }
        }
        return dbProperties;
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties props = loadProperties();
                
                if (props == null) {
                    // Fallback a valori hardcoded
                    connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/UninaFoodLab_App",
                        "root",
                        ""
                    );
                } else {
                    String url = props.getProperty("db.url");
                    String user = props.getProperty("db.user");
                    String password = props.getProperty("db.password");
                    
                    Class.forName(props.getProperty("db.driver"));
                    connection = DriverManager.getConnection(url, user, password);
                }
                
                System.out.println("✓ Connessione al database stabilita");
            }
            
            return connection;
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("✗ Errore connessione database");
            e.printStackTrace();
            return null;
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connessione chiusa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT 1");
                if (rs.next()) {
                    System.out.println("✓ Test connessione riuscito!");
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("✗ Test connessione fallito!");
                e.printStackTrace();
            }
        }
        return false;
    }
}