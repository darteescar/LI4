package app.ecoRideCD.sStock;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sStock.Fornecedor;

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

public class FornecedorDAO implements Map<Integer, Fornecedor> {

    private static FornecedorDAO singleton = null;

    private FornecedorDAO() {}

    public static FornecedorDAO getInstance() {
        if (singleton == null) singleton = new FornecedorDAO();
        return singleton;
    }

    public Optional<Fornecedor> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    private Fornecedor fromRS(ResultSet rs) throws SQLException {
        return new Fornecedor(rs.getInt("id"), rs.getString("nome"),
                rs.getString("email"), rs.getString("telemovel"));
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Fornecedor")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Fornecedor")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Fornecedor WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Fornecedor && containsKey(((Fornecedor) value).getId());
    }

    @Override public Fornecedor get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Fornecedor WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Fornecedor put(Integer key, Fornecedor value) {
        Fornecedor prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Fornecedor SET nome=?, email=?, telemovel=? WHERE id=?")) {
                    pstm.setString(1, value.getNome());
                    pstm.setString(2, value.getEmail());
                    pstm.setString(3, value.getTelemovel());
                    pstm.setInt(4, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Fornecedor (id, nome, email, telemovel) VALUES (?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getNome());
                    pstm.setString(3, value.getEmail());
                    pstm.setString(4, value.getTelemovel());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Fornecedor remove(Object key) {
        Fornecedor f = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Fornecedor WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return f;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Fornecedor> m) {
        for (Fornecedor f : m.values()) put(f.getId(), f);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fornecedor");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Fornecedor")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Fornecedor> values() {
        Collection<Fornecedor> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Fornecedor")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Fornecedor>> entrySet() {
        Set<Map.Entry<Integer, Fornecedor>> res = new HashSet<>();
        for (Fornecedor f : values()) res.add(new AbstractMap.SimpleEntry<>(f.getId(), f));
        return res;
    }
}
