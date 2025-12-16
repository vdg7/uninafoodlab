package it.uninafoodlab.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
	
	private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) throw new IllegalStateException("application.properties non trovato in resources");
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Errore caricamento configurazione", e);
        }
    }

    public static String dbUrl() { return PROPS.getProperty("db.url"); }
    public static String dbUser() { return PROPS.getProperty("db.user"); }
    public static String dbPassword() { return PROPS.getProperty("db.password"); }

    private AppConfig() {}
    
}
