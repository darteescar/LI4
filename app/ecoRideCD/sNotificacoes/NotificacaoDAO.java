package app.ecoRideCD.sNotificacoes;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sNotificacoes.Notificacao;

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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class NotificacaoDAO implements Map<Integer, Notificacao> {

    private static NotificacaoDAO singleton = null;

    private NotificacaoDAO() {
    }

    public static NotificacaoDAO getInstance() {
        if (singleton == null) singleton = new NotificacaoDAO();
        return singleton;
    }

    public Optional<Notificacao> obterPorId(int id) {
        return Optional.ofNullable(this.get(id));
    }

    public List<Notificacao> obterPorDestinatario(int idDestinatario) {
        List<Notificacao> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Notificacao WHERE id_destinatario=?")) {
            pstm.setInt(1, idDestinatario);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) res.add(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    public List<Notificacao> todas() {
        return new ArrayList<>(values());
    }

    private Notificacao fromRS(ResultSet rs) throws SQLException {
        Notificacao n = new Notificacao(rs.getInt("id"), rs.getString("descricao"),
                toLDT(rs.getTimestamp("data_emissao")),
                rs.getInt("id_remetente"), rs.getInt("id_destinatario"));
        n.setNotificacao_tratada(rs.getBoolean("tratada"));
        n.setData_horaTratada(toLDT(rs.getTimestamp("data_horaTratada")));
        return n;
    }

    private LocalDateTime toLDT(Timestamp t) { return t == null ? null : t.toLocalDateTime(); }
    private Timestamp toTS(LocalDateTime ldt) { return ldt == null ? null : Timestamp.valueOf(ldt); }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Notificacao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Notificacao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Notificacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof Notificacao && containsKey(((Notificacao) value).getId());
    }

    @Override
    public Notificacao get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Notificacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override
    public Notificacao put(Integer key, Notificacao value) {
        Notificacao prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Notificacao SET descricao=?, data_emissao=?, id_remetente=?, id_destinatario=?, "
                                + "tratada=?, data_horaTratada=? WHERE id=?")) {
                    pstm.setString(1, value.getDescricao());
                    pstm.setTimestamp(2, toTS(value.getData_emissao()));
                    pstm.setInt(3, value.getId_remetente());
                    pstm.setInt(4, value.getId_destinatario());
                    pstm.setBoolean(5, value.isNotificacao_tratada());
                    pstm.setTimestamp(6, toTS(value.getData_horaTratada()));
                    pstm.setInt(7, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Notificacao (id, descricao, data_emissao, id_remetente, id_destinatario, "
                                + "tratada, data_horaTratada) VALUES (?,?,?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getDescricao());
                    pstm.setTimestamp(3, toTS(value.getData_emissao()));
                    pstm.setInt(4, value.getId_remetente());
                    pstm.setInt(5, value.getId_destinatario());
                    pstm.setBoolean(6, value.isNotificacao_tratada());
                    pstm.setTimestamp(7, toTS(value.getData_horaTratada()));
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override
    public Notificacao remove(Object key) {
        Notificacao n = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Notificacao WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return n;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Notificacao> m) {
        for (Notificacao n : m.values()) put(n.getId(), n);
    }

    @Override
    public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Notificacao");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Notificacao")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override
    public Collection<Notificacao> values() {
        Collection<Notificacao> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Notificacao")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override
    public Set<Map.Entry<Integer, Notificacao>> entrySet() {
        Set<Map.Entry<Integer, Notificacao>> res = new HashSet<>();
        for (Notificacao n : values()) res.add(new AbstractMap.SimpleEntry<>(n.getId(), n));
        return res;
    }
}
