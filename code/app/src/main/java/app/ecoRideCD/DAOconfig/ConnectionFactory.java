package app.ecoRideCD.DAOconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import app.common.EcoRideException;

public final class ConnectionFactory {
    private static final String HOST     = env("DB_HOST", "localhost");
    private static final String PORT     = env("DB_PORT", "3306");
    public  static final String DATABASE = env("DB_NAME", "EcoRide");
    public  static final String USERNAME = env("DB_USER", "ecoride");
    public  static final String PASSWORD = env("DB_PASS", "EcoRide123!");
    public  static final String URL      = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }

    private ConnectionFactory() {}

    public static Connection get() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new EcoRideException("Não foi possível obter ligação à BD", e);
        }
    }
}
