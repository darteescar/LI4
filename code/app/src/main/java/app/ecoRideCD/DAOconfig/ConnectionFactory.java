package app.ecoRideCD.DAOconfig;

import app.common.EcoRideException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    public static final String USERNAME = "me";
    public static final String PASSWORD = "Mypass12345678!";
    public static final String DATABASE = "EcoRide";
    private static final String DRIVER  = "jdbc:mysql";
    public static final String URL = DRIVER + "://localhost:3306/" + DATABASE
            + "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC";

    private ConnectionFactory() {}

    public static Connection get() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new EcoRideException("Não foi possível obter ligação à BD", e);
        }
    }
}
