package app.ecoRideCD.sAutenticacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;

public class UtilizadorDAO {
    private static UtilizadorDAO instance;

    private UtilizadorDAO() {}

    public static UtilizadorDAO getInstance() {
        if (instance == null) instance = new UtilizadorDAO();
        return instance;
    }

    public boolean exists(int id) {
        String sql = "SELECT 1 FROM Utilizador WHERE id = ?";
        try (Connection conn = ConnectionFactory.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar utilizador " + id, e);
        }
    }

    public Utilizador findById(int id) {
        String sql = "SELECT id, password, idFuncionario, cargo FROM Utilizador WHERE id = ?";
        try (Connection conn = ConnectionFactory.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Utilizador(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getInt("idFuncionario"),
                        Cargo.valueOf(rs.getString("cargo"))
                );
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter utilizador " + id, e);
        }
    }

    public void add(Utilizador u) {
        String sql = """
                INSERT INTO Utilizador (id, password, idFuncionario, cargo)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    password      = VALUES(password),
                    idFuncionario = VALUES(idFuncionario),
                    cargo         = VALUES(cargo)
                """;
        try (Connection conn = ConnectionFactory.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, u.getId());
            ps.setString(2, u.getPassword());
            ps.setInt(3, u.getIdFuncionario());
            ps.setString(4, u.getCargo().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar utilizador " + u.getId(), e);
        }
    }

    public int generateNewId() {
        String sql = "SELECT MAX(id) FROM Utilizador";
        try (Connection conn = ConnectionFactory.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1) + 1;
            else return 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para utilizador", e);
        }
    }
}
