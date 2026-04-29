package app.ecoRideCD.sStock;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sStock.Peca;

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

public class PecaDAO implements Map<Integer, Peca> {

    private static PecaDAO singleton = null;

    private PecaDAO() {}

    public static PecaDAO getInstance() {
        if (singleton == null) singleton = new PecaDAO();
        return singleton;
    }

    public Optional<Peca> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public Optional<Peca> obterPorReferencia(String referencia) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Peca WHERE referencia=?")) {
            pstm.setString(1, referencia);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return Optional.of(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return Optional.empty();
    }

    private Peca fromRS(ResultSet rs) throws SQLException {
        Peca p = new Peca(rs.getInt("id"), rs.getString("referencia"),
                rs.getInt("stock_minimo"), rs.getFloat("preco_venda"), rs.getInt("codFornecedor_FK"));
        p.setDisponivel(rs.getBoolean("disponivel"));
        p.setAtiva(rs.getBoolean("ativa"));
        p.setQuantidade(rs.getInt("quantidade"));
        return p;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Peca")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Peca")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Peca WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Peca && containsKey(((Peca) value).getId());
    }

    @Override public Peca get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Peca WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Peca put(Integer key, Peca value) {
        try (Connection conn = DAOconfig.getConnection()) {
            return putWithConnection(conn, value);
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public Peca putWithConnection(Connection conn, Peca value) throws SQLException {
        Peca prev = getWithConnection(conn, value.getId());
        if (prev != null) {
            try (PreparedStatement pstm = conn.prepareStatement(
                    "UPDATE Peca SET referencia=?, stock_minimo=?, preco_venda=?, codFornecedor_FK=?, "
                            + "disponivel=?, ativa=?, quantidade=? WHERE id=?")) {
                pstm.setString(1, value.getReferencia());
                pstm.setInt(2, value.getStock_minimo());
                pstm.setFloat(3, value.getPreco_venda());
                pstm.setInt(4, value.getCodFornecedor());
                pstm.setBoolean(5, value.isDisponivel());
                pstm.setBoolean(6, value.isAtiva());
                pstm.setInt(7, value.getQuantidade());
                pstm.setInt(8, value.getId());
                pstm.executeUpdate();
            }
        } else {
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO Peca (id, referencia, stock_minimo, preco_venda, codFornecedor_FK, "
                            + "disponivel, ativa, quantidade) VALUES (?,?,?,?,?,?,?,?)")) {
                pstm.setInt(1, value.getId());
                pstm.setString(2, value.getReferencia());
                pstm.setInt(3, value.getStock_minimo());
                pstm.setFloat(4, value.getPreco_venda());
                pstm.setInt(5, value.getCodFornecedor());
                pstm.setBoolean(6, value.isDisponivel());
                pstm.setBoolean(7, value.isAtiva());
                pstm.setInt(8, value.getQuantidade());
                pstm.executeUpdate();
            }
        }
        return prev;
    }

    public Peca getWithConnection(Connection conn, int id) throws SQLException {
        try (PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Peca WHERE id=?")) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        }
        return null;
    }

    @Override public Peca remove(Object key) {
        Peca p = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Peca WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return p;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Peca> m) {
        for (Peca p : m.values()) put(p.getId(), p);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Peca");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Peca")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Peca> values() {
        Collection<Peca> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Peca")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Peca>> entrySet() {
        Set<Map.Entry<Integer, Peca>> res = new HashSet<>();
        for (Peca p : values()) res.add(new AbstractMap.SimpleEntry<>(p.getId(), p));
        return res;
    }
}
