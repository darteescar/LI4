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
import app.ecoRideLN.sNotificacoes.EstadoNotificacao;
import app.ecoRideLN.sNotificacoes.Notificacao;
import app.ecoRideLN.sNotificacoes.NotificacaoOS;
import app.ecoRideLN.sNotificacoes.NotificacaoStock;

public class NotificacoesDAO implements Map<Integer, Notificacao> {

    private static NotificacoesDAO instance;

    private NotificacoesDAO() {}

    public static NotificacoesDAO getInstance() {
        if (instance == null) instance = new NotificacoesDAO();
        return instance;
    }

    // Colunas base + LEFT JOINs para os subtipos — usado em todos os SELECTs.
    private static final String BASE_SELECT = """
            SELECT n.id, n.descricao, n.data_emissao, n.id_remetente, n.id_destinatario,
                   n.estado, n.data_horaTratada,
                   nos.id_os, nst.id_peca
            FROM Notificacao n
            LEFT JOIN NotificacaoOS nos ON n.id = nos.id
            LEFT JOIN NotificacaoStock nst ON n.id = nst.id
            """;

    private Notificacao buildFromRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("data_horaTratada");
        LocalDateTime dataTratada = ts == null ? null : ts.toLocalDateTime();

        int id             = rs.getInt("id");
        String descricao   = rs.getString("descricao");
        LocalDateTime data = rs.getTimestamp("data_emissao").toLocalDateTime();
        int idRem          = rs.getInt("id_remetente"); if (rs.wasNull()) idRem = 0;
        int idDest         = rs.getInt("id_destinatario");
        EstadoNotificacao estado = EstadoNotificacao.valueOf(rs.getString("estado"));

        int id_os = rs.getInt("id_os");
        if (!rs.wasNull()) {
            return new NotificacaoOS(id, descricao, data, idRem, idDest, estado, dataTratada, id_os);
        }
        int id_peca = rs.getInt("id_peca");
        if (!rs.wasNull()) {
            return new NotificacaoStock(id, descricao, data, idRem, idDest, estado, dataTratada, id_peca);
        }
        return new Notificacao(id, descricao, data, idRem, idDest, estado, dataTratada);
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Notificacao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar notificacoes", e);
        }
    }

    @Override
    public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Notificacao WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar notificacao " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Notificacao n)) return false;
        Notificacao stored = get(n.getId());
        return stored != null && stored.getId_destinatario() == n.getId_destinatario();
    }

    @Override
    public Notificacao get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = BASE_SELECT + "WHERE n.id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
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
        String sqlBase = """
                UPDATE Notificacao SET descricao=?, data_emissao=?, id_remetente=?,
                    id_destinatario=?, estado=?, data_horaTratada=? WHERE id=?
                """;
        try (Connection c = ConnectionFactory.get()) {
            try (PreparedStatement ps = c.prepareStatement(sqlBase)) {
                ps.setString(1, value.getDescricao());
                ps.setTimestamp(2, Timestamp.valueOf(value.getData_emissao()));
                if (value.getId_remetente() == 0)
                    ps.setNull(3, java.sql.Types.INTEGER);
                else
                    ps.setInt(3, value.getId_remetente());
                ps.setInt(4, value.getId_destinatario());
                ps.setString(5, value.getEstado().name());
                if (value.getData_horaTratada() == null)
                    ps.setNull(6, java.sql.Types.TIMESTAMP);
                else
                    ps.setTimestamp(6, Timestamp.valueOf(value.getData_horaTratada()));
                ps.setInt(7, key);
                ps.executeUpdate();
            }
            if (value instanceof NotificacaoOS nos) {
                String sqlChild = "UPDATE NotificacaoOS SET id_os=? WHERE id=?";
                try (PreparedStatement ps = c.prepareStatement(sqlChild)) {
                    ps.setInt(1, nos.getId_os());
                    ps.setInt(2, key);
                    ps.executeUpdate();
                }
            } else if (value instanceof NotificacaoStock nst) {
                String sqlChild = "UPDATE NotificacaoStock SET id_peca=? WHERE id=?";
                try (PreparedStatement ps = c.prepareStatement(sqlChild)) {
                    ps.setInt(1, nst.getId_peca());
                    ps.setInt(2, key);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar notificacao " + key, e);
        }
        return prev;
    }

    public int insert(Notificacao value) {
        String sqlBase = """
                INSERT INTO Notificacao (descricao, data_emissao, id_remetente,
                                         id_destinatario, estado, data_horaTratada)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                int id = -1;
                try (PreparedStatement ps = c.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, value.getDescricao());
                    ps.setTimestamp(2, Timestamp.valueOf(value.getData_emissao()));
                    if (value.getId_remetente() == 0)
                        ps.setNull(3, java.sql.Types.INTEGER);
                    else
                        ps.setInt(3, value.getId_remetente());
                    ps.setInt(4, value.getId_destinatario());
                    ps.setString(5, value.getEstado().name());
                    if (value.getData_horaTratada() == null)
                        ps.setNull(6, java.sql.Types.TIMESTAMP);
                    else
                        ps.setTimestamp(6, Timestamp.valueOf(value.getData_horaTratada()));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) { id = rs.getInt(1); value.setId(id); }
                        else throw new EcoRideException("Sem ID gerado para notificacao");
                    }
                }
                if (value instanceof NotificacaoOS nos) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO NotificacaoOS (id, id_os) VALUES (?, ?)")) {
                        ps.setInt(1, id);
                        ps.setInt(2, nos.getId_os());
                        ps.executeUpdate();
                    }
                } else if (value instanceof NotificacaoStock nst) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO NotificacaoStock (id, id_peca) VALUES (?, ?)")) {
                        ps.setInt(1, id);
                        ps.setInt(2, nst.getId_peca());
                        ps.executeUpdate();
                    }
                }
                c.commit();
                return id;
            } catch (SQLException e) {
                c.rollback();
                throw new EcoRideException("Erro a inserir notificacao", e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação ao inserir notificacao", e);
        }
    }

    @Override
    public Notificacao remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Notificacao prev = get(id);
        if (prev == null) return null;
        // ON DELETE CASCADE limpa as linhas filhas automaticamente
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Notificacao WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover notificacao " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Notificacao> m) { m.forEach(this::put); }

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
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Notificacao")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de notificacoes", e);
        }
        return out;
    }

    @Override
    public Collection<Notificacao> values() {
        List<Notificacao> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(BASE_SELECT)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacoes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Notificacao>> entrySet() {
        Set<Entry<Integer, Notificacao>> out = new HashSet<>();
        for (Notificacao n : values()) out.add(new AbstractMap.SimpleEntry<>(n.getId(), n));
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Notificacao n) { put(n.getId(), n); }

    public List<Notificacao> getByDestinatario(int id_destinatario) {
        List<Notificacao> out = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE n.id_destinatario = ? ORDER BY n.data_emissao DESC";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_destinatario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacoes do destinatario " + id_destinatario, e);
        }
        return out;
    }

    public List<Notificacao> getUntreatedByDestinatario(int id_destinatario) {
        List<Notificacao> out = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE n.id_destinatario = ? AND n.estado != 'TRATADA' ORDER BY n.data_emissao DESC";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_destinatario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter notificacoes nao tratadas do destinatario " + id_destinatario, e);
        }
        return out;
    }

    public List<Notificacao> getByEmployeeAndDateRange(int id_funcionario, LocalDateTime data_inicio, LocalDateTime data_fim) {
        List<Notificacao> out = new ArrayList<>();
        String sql = BASE_SELECT + """
                WHERE (n.id_remetente = ? OR n.id_destinatario = ?)
                  AND n.data_emissao BETWEEN ? AND ?
                ORDER BY n.data_emissao
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
            throw new EcoRideException("Erro a obter notificacoes do funcionario " + id_funcionario, e);
        }
        return out;
    }

    public List<Notificacao> getByDateRange(LocalDateTime data_inicio, LocalDateTime data_fim) {
        List<Notificacao> out = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE n.data_emissao BETWEEN ? AND ? ORDER BY n.data_emissao";
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
