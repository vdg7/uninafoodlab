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
                    System.err.println("File db.properties non trovato!");
                    return null;
                }

                dbProperties.load(input);


            } catch (IOException e) {
                System.err.println("Errore caricamento db.properties");
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

                String url = "jdbc:postgresql://localhost:5432/uninafoodlab_app";
                String user = "postgres";
                String password = "";

                if (props != null) {
                    url = props.getProperty("db.url", url);
                    user = props.getProperty("db.user", user);
                    password = props.getProperty("db.password", password);

                    Class.forName(props.getProperty("db.driver", "org.postgresql.Driver"));
                } else {
                    // Se il file properties non esiste, carica comunque il driver PostgreSQL
                    Class.forName("org.postgresql.Driver");
                }

                connection = DriverManager.getConnection(url, user, password);
            }

            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL non trovato!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Errore connessione database");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connessione chiusa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    System.out.println("Test connessione riuscito!");
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Test connessione fallito!");
                e.printStackTrace();
            }
        }
        return false;
    }
}
