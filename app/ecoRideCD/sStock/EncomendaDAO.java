package app.ecoRideCD.sStock;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.EstadoEncomenda;

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

public class EncomendaDAO implements Map<Integer, Encomenda> {

    private static EncomendaDAO singleton = null;

    private EncomendaDAO() {}

    public static EncomendaDAO getInstance() {
        if (singleton == null) singleton = new EncomendaDAO();
        return singleton;
    }

    public Optional<Encomenda> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    private Encomenda fromRS(ResultSet rs, Connection externalConn) throws SQLException {
        Encomenda e = new Encomenda(rs.getInt("id"), rs.getInt("codFornecedor_FK"));
        Timestamp env = rs.getTimestamp("data_envio");
        Timestamp rec = rs.getTimestamp("data_rececao");
        if (env != null) e.setData_envio(env.toLocalDateTime());
        if (rec != null) e.setData_rececao(rec.toLocalDateTime());
        e.setEstado(EstadoEncomenda.valueOf(rs.getString("estado")));
        // load stocks
        Connection conn = externalConn;
        boolean closeConn = false;
        if (conn == null) { conn = DAOconfig.getConnection(); closeConn = true; }
        try (PreparedStatement pstm = conn.prepareStatement(
                "SELECT idStock_FK FROM EncomendaStock WHERE idEncomenda_FK=?")) {
            pstm.setInt(1, e.getId());
            try (ResultSet rs2 = pstm.executeQuery()) {
                while (rs2.next()) e.getCodEntradasStock().add(rs2.getInt("idStock_FK"));
            }
        } finally {
            if (closeConn && conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        return e;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Encomenda")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Encomenda")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Encomenda WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Encomenda && containsKey(((Encomenda) value).getId());
    }

    @Override public Encomenda get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Encomenda WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs, conn);
            }
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
        return null;
    }

    @Override public Encomenda put(Integer key, Encomenda value) {
        Encomenda prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            Timestamp env = value.getData_envio() == null ? null : Timestamp.valueOf(value.getData_envio());
            Timestamp rec = value.getData_rececao() == null ? null : Timestamp.valueOf(value.getData_rececao());
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Encomenda SET codFornecedor_FK=?, data_envio=?, data_rececao=?, estado=? WHERE id=?")) {
                    pstm.setInt(1, value.getCodFornecedor());
                    pstm.setTimestamp(2, env);
                    pstm.setTimestamp(3, rec);
                    pstm.setString(4, value.getEstado().name());
                    pstm.setInt(5, value.getId());
                    pstm.executeUpdate();
                }
                try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM EncomendaStock WHERE idEncomenda_FK=?")) {
                    pstm.setInt(1, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Encomenda (id, codFornecedor_FK, data_envio, data_rececao, estado) VALUES (?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setInt(2, value.getCodFornecedor());
                    pstm.setTimestamp(3, env);
                    pstm.setTimestamp(4, rec);
                    pstm.setString(5, value.getEstado().name());
                    pstm.executeUpdate();
                }
            }
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO EncomendaStock (idEncomenda_FK, idStock_FK) VALUES (?,?)")) {
                for (Integer codStock : value.getCodEntradasStock()) {
                    pstm.setInt(1, value.getId());
                    pstm.setInt(2, codStock);
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
        return prev;
    }

    @Override public Encomenda remove(Object key) {
        Encomenda e = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM EncomendaStock WHERE idEncomenda_FK=?")) {
                pstm.setInt(1, (Integer) key);
                pstm.executeUpdate();
            }
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM Encomenda WHERE id=?")) {
                pstm.setInt(1, (Integer) key);
                pstm.executeUpdate();
            }
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
        return e;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Encomenda> m) {
        for (Encomenda e : m.values()) put(e.getId(), e);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM EncomendaStock");
            stm.executeUpdate("DELETE FROM Encomenda");
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Encomenda")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
        return res;
    }

    @Override public Collection<Encomenda> values() {
        Collection<Encomenda> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Encomenda")) {
            while (rs.next()) res.add(fromRS(rs, conn));
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Encomenda>> entrySet() {
        Set<Map.Entry<Integer, Encomenda>> res = new HashSet<>();
        for (Encomenda e : values()) res.add(new AbstractMap.SimpleEntry<>(e.getId(), e));
        return res;
    }
}
