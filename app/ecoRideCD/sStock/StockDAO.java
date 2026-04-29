package app.ecoRideCD.sStock;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.Stock;
import app.ecoRideLN.sStock.StockComGarantia;

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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class StockDAO implements Map<Integer, Stock> {

    private static StockDAO singleton = null;

    private StockDAO() {}

    public static StockDAO getInstance() {
        if (singleton == null) singleton = new StockDAO();
        return singleton;
    }

    public Optional<Stock> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public List<Stock> obterPorPeca(int codPeca) {
        List<Stock> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Stock WHERE codPeca_FK=?")) {
            pstm.setInt(1, codPeca);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) res.add(loadFromRSWithGarantia(rs, null));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    public List<Stock> obterPorPecaEEstado(int codPeca, EstadoStock estado) {
        List<Stock> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Stock WHERE codPeca_FK=? AND estado=?")) {
            pstm.setInt(1, codPeca);
            pstm.setString(2, estado.name());
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) res.add(loadFromRSWithGarantia(rs, null));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    public long quantidadePorPecaEEstado(int codPeca, EstadoStock estado) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement(
                     "SELECT count(*) FROM Stock WHERE codPeca_FK=? AND estado=?")) {
            pstm.setInt(1, codPeca);
            pstm.setString(2, estado.name());
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private Stock loadFromRSWithGarantia(ResultSet rs, Connection externalConn) throws SQLException {
        int id = rs.getInt("id");
        float preco = rs.getFloat("preco_compra");
        int codPeca = rs.getInt("codPeca_FK");
        Timestamp dc = rs.getTimestamp("data_chegada");
        EstadoStock estado = EstadoStock.valueOf(rs.getString("estado"));

        Connection conn = externalConn;
        boolean closeConn = false;
        if (conn == null) { conn = DAOconfig.getConnection(); closeConn = true; }
        try (PreparedStatement pstm = conn.prepareStatement(
                "SELECT nr_serie, garantia FROM StockComGarantia WHERE idStock_FK=?")) {
            pstm.setInt(1, id);
            try (ResultSet rs2 = pstm.executeQuery()) {
                if (rs2.next()) {
                    StockComGarantia s = new StockComGarantia(id, preco, codPeca,
                            dc == null ? null : dc.toLocalDateTime(),
                            rs2.getString("nr_serie"), rs2.getInt("garantia"));
                    s.setEstado(estado);
                    return s;
                }
            }
        } finally {
            if (closeConn && conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }
        Stock s = new Stock(id, preco, codPeca, dc == null ? null : dc.toLocalDateTime());
        s.setEstado(estado);
        return s;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Stock")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Stock")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Stock WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Stock && containsKey(((Stock) value).getId());
    }

    @Override public Stock get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Stock WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return loadFromRSWithGarantia(rs, conn);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Stock put(Integer key, Stock value) {
        try (Connection conn = DAOconfig.getConnection()) {
            return putWithConnection(conn, value);
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public Stock putWithConnection(Connection conn, Stock value) throws SQLException {
        Stock prev = getWithConnection(conn, value.getId());
        Timestamp ts = value.getData_chegada() == null ? null : Timestamp.valueOf(value.getData_chegada());
        if (prev != null) {
            try (PreparedStatement pstm = conn.prepareStatement(
                    "UPDATE Stock SET preco_compra=?, codPeca_FK=?, data_chegada=?, estado=? WHERE id=?")) {
                pstm.setFloat(1, value.getPreco_compra());
                pstm.setInt(2, value.getCodPeca());
                pstm.setTimestamp(3, ts);
                pstm.setString(4, value.getEstado().name());
                pstm.setInt(5, value.getId());
                pstm.executeUpdate();
            }
        } else {
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO Stock (id, preco_compra, codPeca_FK, data_chegada, estado) VALUES (?,?,?,?,?)")) {
                pstm.setInt(1, value.getId());
                pstm.setFloat(2, value.getPreco_compra());
                pstm.setInt(3, value.getCodPeca());
                pstm.setTimestamp(4, ts);
                pstm.setString(5, value.getEstado().name());
                pstm.executeUpdate();
            }
        }
        if (value instanceof StockComGarantia) {
            StockComGarantia scg = (StockComGarantia) value;
            // upsert garantia
            try (PreparedStatement check = conn.prepareStatement(
                    "SELECT idStock_FK FROM StockComGarantia WHERE idStock_FK=?")) {
                check.setInt(1, scg.getId());
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement pstm = conn.prepareStatement(
                                "UPDATE StockComGarantia SET nr_serie=?, garantia=? WHERE idStock_FK=?")) {
                            pstm.setString(1, scg.getNr_serie());
                            pstm.setInt(2, scg.getGarantia());
                            pstm.setInt(3, scg.getId());
                            pstm.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement pstm = conn.prepareStatement(
                                "INSERT INTO StockComGarantia (idStock_FK, nr_serie, garantia) VALUES (?,?,?)")) {
                            pstm.setInt(1, scg.getId());
                            pstm.setString(2, scg.getNr_serie());
                            pstm.setInt(3, scg.getGarantia());
                            pstm.executeUpdate();
                        }
                    }
                }
            }
        }
        return prev;
    }

    public Stock getWithConnection(Connection conn, int id) throws SQLException {
        try (PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Stock WHERE id=?")) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return loadFromRSWithGarantia(rs, conn);
            }
        }
        return null;
    }

    @Override public Stock remove(Object key) {
        Stock s = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM StockComGarantia WHERE idStock_FK=?")) {
                pstm.setInt(1, (Integer) key);
                pstm.executeUpdate();
            }
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM Stock WHERE id=?")) {
                pstm.setInt(1, (Integer) key);
                pstm.executeUpdate();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return s;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Stock> m) {
        for (Stock s : m.values()) put(s.getId(), s);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM StockComGarantia");
            stm.executeUpdate("DELETE FROM Stock");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Stock")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Stock> values() {
        Collection<Stock> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Stock")) {
            while (rs.next()) res.add(loadFromRSWithGarantia(rs, conn));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Stock>> entrySet() {
        Set<Map.Entry<Integer, Stock>> res = new HashSet<>();
        for (Stock s : values()) res.add(new AbstractMap.SimpleEntry<>(s.getId(), s));
        return res;
    }
}
