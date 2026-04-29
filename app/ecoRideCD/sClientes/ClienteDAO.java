package app.ecoRideCD.sClientes;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sClientes.Cliente;

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

public class ClienteDAO implements Map<Integer, Cliente> {

    private static ClienteDAO singleton = null;

    private ClienteDAO() {}

    public static ClienteDAO getInstance() {
        if (singleton == null) singleton = new ClienteDAO();
        return singleton;
    }

    public Optional<Cliente> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public Optional<Cliente> obterPorNif(String nif) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Cliente WHERE NIF=?")) {
            pstm.setString(1, nif);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return Optional.of(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return Optional.empty();
    }

    private Cliente fromRS(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(rs.getInt("id"), rs.getString("nome"),
                rs.getString("email"), rs.getString("telemovel"), rs.getString("NIF"));
        // load trotinetes ids
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Trotinete WHERE codCliente_FK=?")) {
            pstm.setInt(1, c.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                while (rs2.next()) c.getCodsTrotinetes().add(rs2.getInt("id"));
            }
        }
        return c;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Cliente")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Cliente")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Cliente WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Cliente && containsKey(((Cliente) value).getId());
    }

    @Override public Cliente get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Cliente WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Cliente put(Integer key, Cliente value) {
        Cliente prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Cliente SET nome=?, email=?, telemovel=?, NIF=? WHERE id=?")) {
                    pstm.setString(1, value.getNome());
                    pstm.setString(2, value.getEmail());
                    pstm.setString(3, value.getTelemovel());
                    pstm.setString(4, value.getNIF());
                    pstm.setInt(5, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Cliente (id, nome, email, telemovel, NIF) VALUES (?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getNome());
                    pstm.setString(3, value.getEmail());
                    pstm.setString(4, value.getTelemovel());
                    pstm.setString(5, value.getNIF());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Cliente remove(Object key) {
        Cliente c = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Cliente WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return c;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Cliente> m) {
        for (Cliente c : m.values()) put(c.getId(), c);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Cliente");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Cliente")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Cliente> values() {
        Collection<Cliente> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Cliente")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Cliente>> entrySet() {
        Set<Map.Entry<Integer, Cliente>> res = new HashSet<>();
        for (Cliente c : values()) res.add(new AbstractMap.SimpleEntry<>(c.getId(), c));
        return res;
    }
}
