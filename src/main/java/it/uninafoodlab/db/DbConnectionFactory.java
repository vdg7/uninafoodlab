package it.uninafoodlab.db;

import it.uninafoodlab.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnectionFactory {
	
	public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                AppConfig.dbUrl(),
                AppConfig.dbUser(),
                AppConfig.dbPassword()
        );
    }

    private DbConnectionFactory() {}
}
