package app.ecoRideCD.sStock;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.EstadoDevolucao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DevolucaoDAO implements Map<Integer, Devolucao> {

    private static DevolucaoDAO singleton = null;

    private DevolucaoDAO() {}

    public static DevolucaoDAO getInstance() {
        if (singleton == null) singleton = new DevolucaoDAO();
        return singleton;
    }

    public Optional<Devolucao> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    private Devolucao fromRS(ResultSet rs) throws SQLException {
        Timestamp data = rs.getTimestamp("data");
        Devolucao d = new Devolucao(rs.getInt("id"),
                data == null ? null : data.toLocalDateTime(),
                rs.getString("motivo"), rs.getInt("codStock_FK"));
        d.setEstado(EstadoDevolucao.valueOf(rs.getString("estado")));
        return d;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Devolucao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Devolucao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Devolucao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Devolucao && containsKey(((Devolucao) value).getId());
    }

    @Override public Devolucao get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Devolucao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Devolucao put(Integer key, Devolucao value) {
        Devolucao prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            Timestamp data = value.getData() == null ? null : Timestamp.valueOf(value.getData());
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Devolucao SET data=?, motivo=?, codStock_FK=?, estado=? WHERE id=?")) {
                    pstm.setTimestamp(1, data);
                    pstm.setString(2, value.getMotivo());
                    pstm.setInt(3, value.getCodStock());
                    pstm.setString(4, value.getEstado().name());
                    pstm.setInt(5, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Devolucao (id, data, motivo, codStock_FK, estado) VALUES (?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setTimestamp(2, data);
                    pstm.setString(3, value.getMotivo());
                    pstm.setInt(4, value.getCodStock());
                    pstm.setString(5, value.getEstado().name());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Devolucao remove(Object key) {
        Devolucao d = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Devolucao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return d;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Devolucao> m) {
        for (Devolucao d : m.values()) put(d.getId(), d);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Devolucao");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Devolucao")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Devolucao> values() {
        Collection<Devolucao> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Devolucao")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Devolucao>> entrySet() {
        Set<Map.Entry<Integer, Devolucao>> res = new HashSet<>();
        for (Devolucao d : values()) res.add(new AbstractMap.SimpleEntry<>(d.getId(), d));
        return res;
    }
}
