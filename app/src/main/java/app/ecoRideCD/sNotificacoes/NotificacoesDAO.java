package app.ecoRideCD.sNotificacoes;

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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sNotificacoes.Notificacao;

public class NotificacoesDAO implements Map<Integer, Notificacao> {

    private static NotificacoesDAO instance;

    private NotificacoesDAO() {
    }

    public static NotificacoesDAO getInstance() {
        if (instance == null) {
            instance = new NotificacoesDAO();
        }
        return instance;
    }

    private Notificacao buildFromRow(ResultSet rs) throws SQLException {
        Timestamp tratada = rs.getTimestamp("data_horaTratada");
        return new Notificacao(
                rs.getInt("id"),
                rs.getString("descricao"),
                rs.getTimestamp("data_emissao").toLocalDateTime(),
                rs.getInt("id_remetente"),
                rs.getInt("id_destinatario"),
                rs.getBoolean("notificacao_tratada"),
                tratada == null ? null : tratada.toLocalDateTime());
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Notificacao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar notificacoes", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) {
            return false;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Notificacao WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar notificacao " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Notificacao n)) {
            return false;
        }
        Notificacao stored = get(n.getId());
        return stored != null && stored.getId_destinatario() == n.getId_destinatario();
    }

    @Override
    public Notificacao get(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        String sql = """
                SELECT id, descricao, data_emissao, id_remetente, id_destinatario,
                       notificacao_tratada, data_horaTratada
                FROM Notificacao WHERE id = ?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacao " + id, e);
        }
    }

    @Override
    public Notificacao put(Integer key, Notificacao value) {
        Notificacao prev = get(key);
        String sql = """
                INSERT INTO Notificacao (id, descricao, data_emissao, id_remetente,
                                         id_destinatario, notificacao_tratada, data_horaTratada)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    descricao = VALUES(descricao), data_emissao = VALUES(data_emissao),
                    id_remetente = VALUES(id_remetente), id_destinatario = VALUES(id_destinatario),
                    notificacao_tratada = VALUES(notificacao_tratada),
                    data_horaTratada = VALUES(data_horaTratada)
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setString(2, value.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(value.getData_emissao()));
            ps.setInt(4, value.getId_remetente());
            ps.setInt(5, value.getId_destinatario());
            ps.setBoolean(6, value.isNotificacao_tratada());
            if (value.getData_horaTratada() == null) {
                ps.setNull(7, java.sql.Types.TIMESTAMP); 
            }else {
                ps.setTimestamp(7, Timestamp.valueOf(value.getData_horaTratada()));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar notificacao " + key, e);
        }
        return prev;
    }

    @Override
    public Notificacao remove(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        Notificacao prev = get(id);
        if (prev == null) {
            return null;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM Notificacao WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover notificacao " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Notificacao> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Notificacao");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar notificacoes", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id FROM Notificacao")) {
            while (rs.next()) {
                out.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de notificacoes", e);
        }
        return out;
    }

    @Override
    public Collection<Notificacao> values() {
        Set<Notificacao> out = new LinkedHashSet<>();
        String sql = """
                SELECT id, descricao, data_emissao, id_remetente, id_destinatario,
                       notificacao_tratada, data_horaTratada FROM Notificacao
                """;
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacoes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Notificacao>> entrySet() {
        Set<Entry<Integer, Notificacao>> out = new HashSet<>();
        for (Notificacao n : values()) {
            out.add(new AbstractMap.SimpleEntry<>(n.getId(), n));
        }
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Notificacao n) {
        put(n.getId(), n);
    }

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Notificacao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para notificacao", e);
        }
    }

    // Notificações em que o funcionário é remetente OU destinatário,
    // dentro do intervalo [data_inicio, data_fim] (inclusivo nas duas pontas).
    public List<Notificacao> getByEmployeeAndDateRange(int id_funcionario,
                                                       LocalDateTime data_inicio,
                                                       LocalDateTime data_fim) {
        List<Notificacao> out = new ArrayList<>();
        String sql = """
                SELECT id, descricao, data_emissao, id_remetente, id_destinatario,
                       notificacao_tratada, data_horaTratada
                FROM   Notificacao
                WHERE  (id_remetente = ? OR id_destinatario = ?)
                  AND  data_emissao BETWEEN ? AND ?
                ORDER BY data_emissao
                """;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_funcionario);
            ps.setInt(2, id_funcionario);
            ps.setTimestamp(3, Timestamp.valueOf(data_inicio));
            ps.setTimestamp(4, Timestamp.valueOf(data_fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException(
                    "Erro a obter notificacoes do funcionario " + id_funcionario, e);
        }
        return out;
    }

    // Todas as notificações emitidas dentro do intervalo [data_inicio, data_fim].
    public List<Notificacao> getByDateRange(LocalDateTime data_inicio, LocalDateTime data_fim) {
        List<Notificacao> out = new ArrayList<>();
        String sql = """
                SELECT id, descricao, data_emissao, id_remetente, id_destinatario,
                       notificacao_tratada, data_horaTratada
                FROM   Notificacao
                WHERE  data_emissao BETWEEN ? AND ?
                ORDER BY data_emissao
                """;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(data_inicio));
            ps.setTimestamp(2, Timestamp.valueOf(data_fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacoes no intervalo", e);
        }
        return out;
    }
}
