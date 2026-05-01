package app.ecoRideCD.DAOconfig;

import app.common.EcoRideException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DAOconfig {
    private static final String SCHEMA_RESOURCE = "/schema.sql";

    private DAOconfig() {}

    public static void CreateBD() {
        String script = readSchema();
        try (Connection conn = ConnectionFactory.get();
             Statement stm = conn.createStatement()) {
            for (String stmt : splitStatements(script)) {
                if (!stmt.isBlank()) stm.executeUpdate(stmt);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a criar a base de dados", e);
        }
    }

    private static String readSchema() {
        try (InputStream in = DAOconfig.class.getResourceAsStream(SCHEMA_RESOURCE)) {
            if (in == null) throw new EcoRideException("Recurso não encontrado: " + SCHEMA_RESOURCE);
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new EcoRideException("Não foi possível ler " + SCHEMA_RESOURCE, e);
        }
    }

    private static String[] splitStatements(String script) {
        String stripped = script.replaceAll("(?m)--.*$", "");
        return stripped.split(";\\s*\\R");
    }
}
