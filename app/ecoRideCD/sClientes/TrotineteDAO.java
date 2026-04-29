package app.ecoRideCD.sClientes;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sClientes.Trotinete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TrotineteDAO implements Map<Integer, Trotinete> {

    private static TrotineteDAO singleton = null;

    private TrotineteDAO() {}

    public static TrotineteDAO getInstance() {
        if (singleton == null) singleton = new TrotineteDAO();
        return singleton;
    }

    public Optional<Trotinete> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public List<Trotinete> obterPorCliente(int idCliente) {
        List<Trotinete> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Trotinete WHERE codCliente_FK=?")) {
            pstm.setInt(1, idCliente);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) res.add(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    private Trotinete fromRS(ResultSet rs) throws SQLException {
        return new Trotinete(rs.getInt("id"), rs.getString("modelo"), rs.getString("marca"),
                rs.getString("num_serie"), rs.getString("tipo_motor"), rs.getInt("codCliente_FK"));
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Trotinete")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Trotinete")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Trotinete WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Trotinete && containsKey(((Trotinete) value).getId());
    }

    @Override public Trotinete get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Trotinete WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Trotinete put(Integer key, Trotinete value) {
        Trotinete prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Trotinete SET modelo=?, marca=?, num_serie=?, tipo_motor=?, codCliente_FK=? WHERE id=?")) {
                    pstm.setString(1, value.getModelo());
                    pstm.setString(2, value.getMarca());
                    pstm.setString(3, value.getNum_serie());
                    pstm.setString(4, value.getTipo_motor());
                    pstm.setInt(5, value.getCodCliente());
                    pstm.setInt(6, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Trotinete (id, modelo, marca, num_serie, tipo_motor, codCliente_FK) VALUES (?,?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getModelo());
                    pstm.setString(3, value.getMarca());
                    pstm.setString(4, value.getNum_serie());
                    pstm.setString(5, value.getTipo_motor());
                    pstm.setInt(6, value.getCodCliente());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Trotinete remove(Object key) {
        Trotinete t = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Trotinete WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return t;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Trotinete> m) {
        for (Trotinete t : m.values()) put(t.getId(), t);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Trotinete");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Trotinete")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Trotinete> values() {
        Collection<Trotinete> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Trotinete")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Trotinete>> entrySet() {
        Set<Map.Entry<Integer, Trotinete>> res = new HashSet<>();
        for (Trotinete t : values()) res.add(new AbstractMap.SimpleEntry<>(t.getId(), t));
        return res;
    }
}
