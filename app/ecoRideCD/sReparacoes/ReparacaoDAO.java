package app.ecoRideCD.sReparacoes;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sReparacoes.Reparacao;

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

public class ReparacaoDAO implements Map<Integer, Reparacao> {

    private static ReparacaoDAO singleton = null;

    private ReparacaoDAO() {}

    public static ReparacaoDAO getInstance() {
        if (singleton == null) singleton = new ReparacaoDAO();
        return singleton;
    }

    public Optional<Reparacao> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public Optional<Reparacao> obterPorNomenclatura(String nomenclatura) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Reparacao WHERE nomenclatura=?")) {
            pstm.setString(1, nomenclatura);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return Optional.of(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return Optional.empty();
    }

    private Reparacao fromRS(ResultSet rs) throws SQLException {
        Reparacao r = new Reparacao(rs.getInt("id"), rs.getString("nomenclatura"),
                rs.getString("descricao"), rs.getFloat("preco"));
        r.setDisponivel(rs.getBoolean("disponivel"));
        return r;
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Reparacao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Reparacao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Reparacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Reparacao && containsKey(((Reparacao) value).getId());
    }

    @Override public Reparacao get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Reparacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Reparacao put(Integer key, Reparacao value) {
        Reparacao prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Reparacao SET nomenclatura=?, descricao=?, preco=?, disponivel=? WHERE id=?")) {
                    pstm.setString(1, value.getNomenclatura());
                    pstm.setString(2, value.getDescricao());
                    pstm.setFloat(3, value.getPreco());
                    pstm.setBoolean(4, value.isDisponivel());
                    pstm.setInt(5, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Reparacao (id, nomenclatura, descricao, preco, disponivel) VALUES (?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getNomenclatura());
                    pstm.setString(3, value.getDescricao());
                    pstm.setFloat(4, value.getPreco());
                    pstm.setBoolean(5, value.isDisponivel());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Reparacao remove(Object key) {
        Reparacao r = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Reparacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return r;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Reparacao> m) {
        for (Reparacao r : m.values()) put(r.getId(), r);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Reparacao");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Reparacao")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Reparacao> values() {
        Collection<Reparacao> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Reparacao")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Reparacao>> entrySet() {
        Set<Map.Entry<Integer, Reparacao>> res = new HashSet<>();
        for (Reparacao r : values()) res.add(new AbstractMap.SimpleEntry<>(r.getId(), r));
        return res;
    }
}
