package app.ecoRideCD.DAOconfig;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class DBInitializer {

    private DBInitializer() {}

    public static void run() {
        runScript("schema.sql");
        runScript("seed.sql");
    }

    private static void runScript(String resource) {
        try (InputStream in = DBInitializer.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) return;
            String content = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            // Remove linhas de comentário antes de dividir por ";"
            String cleaned = Arrays.stream(content.split("\n"))
                    .filter(line -> !line.strip().startsWith("--"))
                    .collect(Collectors.joining("\n"));

            try (Connection c = ConnectionFactory.get();
                 Statement s = c.createStatement()) {
                for (String stmt : cleaned.split(";")) {
                    String trimmed = stmt.strip();
                    if (!trimmed.isEmpty()) {
                        s.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DBInitializer] Aviso ao executar " + resource + ": " + e.getMessage());
        }
    }
}
