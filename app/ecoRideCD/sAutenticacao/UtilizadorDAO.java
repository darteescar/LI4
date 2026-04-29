package app.ecoRideCD.sAutenticacao;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UtilizadorDAO implements Map<Integer, Utilizador> {

    private static UtilizadorDAO singleton = null;

    private UtilizadorDAO() {
    }

    public static UtilizadorDAO getInstance() {
        if (singleton == null) singleton = new UtilizadorDAO();
        return singleton;
    }

    public Optional<Utilizador> obterPorId(int id) {
        return Optional.ofNullable(this.get(id));
    }

    public Optional<Utilizador> obterPorIdFuncionario(int idFuncionario) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Utilizador WHERE idFuncionario_FK=?")) {
            pstm.setInt(1, idFuncionario);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return Optional.of(fromRS(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Utilizador fromRS(ResultSet rs) throws SQLException {
        return new Utilizador(rs.getInt("id"), rs.getString("password"),
                rs.getInt("idFuncionario_FK"), Cargo.valueOf(rs.getString("cargo")));
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Utilizador")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Utilizador")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Utilizador WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof Utilizador && containsKey(((Utilizador) value).getId());
    }

    @Override
    public Utilizador get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Utilizador WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Utilizador put(Integer key, Utilizador value) {
        Utilizador prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Utilizador SET password=?, idFuncionario_FK=?, cargo=? WHERE id=?")) {
                    pstm.setString(1, value.getPassword());
                    pstm.setInt(2, value.getIdFuncionario());
                    pstm.setString(3, value.getCargo().name());
                    pstm.setInt(4, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Utilizador (id, password, idFuncionario_FK, cargo) VALUES (?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getPassword());
                    pstm.setInt(3, value.getIdFuncionario());
                    pstm.setString(4, value.getCargo().name());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return prev;
    }

    @Override
    public Utilizador remove(Object key) {
        Utilizador u = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Utilizador WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return u;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> m) {
        for (Utilizador u : m.values()) put(u.getId(), u);
    }

    @Override
    public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Utilizador");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Utilizador")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override
    public Collection<Utilizador> values() {
        Collection<Utilizador> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override
    public Set<Map.Entry<Integer, Utilizador>> entrySet() {
        Set<Map.Entry<Integer, Utilizador>> res = new HashSet<>();
        for (Utilizador u : values()) res.add(new AbstractMap.SimpleEntry<>(u.getId(), u));
        return res;
    }
}
