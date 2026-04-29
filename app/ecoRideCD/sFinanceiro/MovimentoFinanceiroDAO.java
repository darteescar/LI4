package app.ecoRideCD.sFinanceiro;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MovimentoFinanceiroDAO implements Map<Integer, MovimentoFinanceiro> {

    private static MovimentoFinanceiroDAO singleton = null;

    private MovimentoFinanceiroDAO() {}

    public static MovimentoFinanceiroDAO getInstance() {
        if (singleton == null) singleton = new MovimentoFinanceiroDAO();
        return singleton;
    }

    public Optional<MovimentoFinanceiro> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    private MovimentoFinanceiro fromRS(ResultSet rs) throws SQLException {
        return new MovimentoFinanceiro(rs.getInt("id"), rs.getFloat("valor"),
                toLDT(rs.getTimestamp("data")), rs.getString("descricao"),
                TipoMovimento.valueOf(rs.getString("tipo")), rs.getInt("codEntidade"));
    }

    private LocalDateTime toLDT(Timestamp t) { return t == null ? null : t.toLocalDateTime(); }
    private Timestamp toTS(LocalDateTime ldt) { return ldt == null ? null : Timestamp.valueOf(ldt); }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM MovimentoFinanceiro")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM MovimentoFinanceiro")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM MovimentoFinanceiro WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof MovimentoFinanceiro && containsKey(((MovimentoFinanceiro) value).getId());
    }

    @Override public MovimentoFinanceiro get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM MovimentoFinanceiro WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public MovimentoFinanceiro put(Integer key, MovimentoFinanceiro value) {
        MovimentoFinanceiro prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE MovimentoFinanceiro SET valor=?, data=?, descricao=?, codEntidade=?, tipo=? WHERE id=?")) {
                    pstm.setFloat(1, value.getValor());
                    pstm.setTimestamp(2, toTS(value.getData()));
                    pstm.setString(3, value.getDescricao());
                    pstm.setInt(4, value.getCodEntidade());
                    pstm.setString(5, value.getTipo().name());
                    pstm.setInt(6, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO MovimentoFinanceiro (id, valor, data, descricao, codEntidade, tipo) VALUES (?,?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setFloat(2, value.getValor());
                    pstm.setTimestamp(3, toTS(value.getData()));
                    pstm.setString(4, value.getDescricao());
                    pstm.setInt(5, value.getCodEntidade());
                    pstm.setString(6, value.getTipo().name());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public MovimentoFinanceiro remove(Object key) {
        MovimentoFinanceiro m = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM MovimentoFinanceiro WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return m;
    }

    @Override public void putAll(Map<? extends Integer, ? extends MovimentoFinanceiro> m) {
        for (MovimentoFinanceiro mv : m.values()) put(mv.getId(), mv);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM MovimentoFinanceiro");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM MovimentoFinanceiro")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<MovimentoFinanceiro> values() {
        Collection<MovimentoFinanceiro> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM MovimentoFinanceiro")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, MovimentoFinanceiro>> entrySet() {
        Set<Map.Entry<Integer, MovimentoFinanceiro>> res = new HashSet<>();
        for (MovimentoFinanceiro mv : values()) res.add(new AbstractMap.SimpleEntry<>(mv.getId(), mv));
        return res;
    }
}
