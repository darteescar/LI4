package app.ecoRideCD.sOrdensServico;

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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Diagnostico;
import app.ecoRideLN.sOrdensServico.EstadoOS;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sOrdensServico.Pagamento;
import app.ecoRideLN.sOrdensServico.Registo;

public class OrdemServicoDAO implements Map<Integer, OrdemServico> {

    private static OrdemServicoDAO instance;

    private OrdemServicoDAO() {}

    public static OrdemServicoDAO getInstance() {
        if (instance == null) instance = new OrdemServicoDAO();
        return instance;
    }

    // Campos da tabela base — metodo_pagamento foi movido para tabela Pagamento
    private static final String SELECT_BASE = """
            SELECT id, descricao, data_criacao, codTrotinete, codCliente,
                   codCriador, codMecanico, estado
            FROM   OrdemServico
            """;

    // ---------------- Helpers de leitura ----------------

    private List<String> loadAcessorios(Connection c, int idOS) throws SQLException {
        List<String> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT valor FROM OrdemServico_Acessorio WHERE idOS = ? ORDER BY ordem")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString(1));
            }
        }
        return out;
    }

    private Pagamento loadPagamento(Connection c, int idOS) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT metodo, dataPagamento, clienteNotificado, dataNotificacao FROM Pagamento WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String metodoStr = rs.getString("metodo");
                Metodo_Pagamento metodo = metodoStr != null ? Metodo_Pagamento.valueOf(metodoStr) : null;
                Timestamp dp = rs.getTimestamp("dataPagamento");
                boolean cn = rs.getBoolean("clienteNotificado");
                Timestamp dn = rs.getTimestamp("dataNotificacao");
                return new Pagamento(
                        metodo,
                        dp != null ? dp.toLocalDateTime() : null,
                        cn,
                        dn != null ? dn.toLocalDateTime() : null);
            }
        }
    }

    private Diagnostico loadDiagnostico(Connection c, int idOS) throws SQLException {
        String desc;
        float orc;
        boolean aprovado;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT descricao, orcamento, aprovado FROM Diagnostico WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                desc = rs.getString("descricao");
                orc = rs.getFloat("orcamento");
                aprovado = rs.getBoolean("aprovado");
            }
        }
        List<Integer> reps = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT codReparacao FROM Diagnostico_Reparacao WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reps.add(rs.getInt(1));
            }
        }
        Map<Integer, Integer> pecas = new LinkedHashMap<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT codPeca, quantidade FROM Diagnostico_PecaOrcamento WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) pecas.put(rs.getInt("codPeca"), rs.getInt("quantidade"));
            }
        }
        return new Diagnostico(desc, reps, pecas, orc, aprovado);
    }

    private Conserto loadConserto(Connection c, int idOS) throws SQLException {
        float preco;
        CheckList chk = new CheckList();
        try (PreparedStatement ps = c.prepareStatement("""
                SELECT preco_total,
                       chk_luzes, chk_pneus, chk_aceleracao,
                       chk_travagem, chk_visor, chk_teste_pratico
                FROM   Conserto WHERE idOS = ?
                """)) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                preco = rs.getFloat("preco_total");
                chk.setLuzes(rs.getBoolean("chk_luzes"));
                chk.setPneus(rs.getBoolean("chk_pneus"));
                chk.setAceleracao(rs.getBoolean("chk_aceleracao"));
                chk.setTravagem(rs.getBoolean("chk_travagem"));
                chk.setVisor(rs.getBoolean("chk_visor"));
                chk.setTeste_pratico(rs.getBoolean("chk_teste_pratico"));
            }
        }
        List<Integer> reps = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT codReparacao FROM Conserto_Reparacao WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reps.add(rs.getInt(1));
            }
        }
        Map<Integer, Integer> stocksUsados = new LinkedHashMap<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT codStock, quantidade FROM Conserto_PecaUsada WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) stocksUsados.put(rs.getInt("codStock"), rs.getInt("quantidade"));
            }
        }
        Conserto con = new Conserto(stocksUsados, reps, preco);
        con.setCheckList(chk);
        return con;
    }

    private OrdemServico buildFromRow(Connection c, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Registo registo = new Registo(
                rs.getString("descricao"),
                rs.getTimestamp("data_criacao").toLocalDateTime(),
                rs.getInt("codTrotinete"),
                rs.getInt("codCliente"),
                rs.getInt("codCriador"),
                loadAcessorios(c, id));
        OrdemServico os = new OrdemServico(id, registo);
        os.setEstado(EstadoOS.valueOf(rs.getString("estado")));
        os.setCodMecanico(rs.getObject("codMecanico", Integer.class));
        Pagamento pag = loadPagamento(c, id);
        if (pag != null) os.setPagamento(pag);
        Diagnostico d = loadDiagnostico(c, id);
        if (d != null) os.setDiagnostico(d);
        Conserto con = loadConserto(c, id);
        if (con != null) os.setConserto(con);
        return os;
    }

    // ---------------- Helpers de escrita ----------------

    private void updateBase(Connection c, int id, OrdemServico os) throws SQLException {
        String sql = """
                UPDATE OrdemServico SET descricao=?, data_criacao=?, codTrotinete=?,
                    codCliente=?, codCriador=?, codMecanico=?, estado=?
                WHERE id=?
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            Registo r = os.getRegisto();
            ps.setString(1, r.getDescricao());
            ps.setTimestamp(2, Timestamp.valueOf(r.getDataCriacao()));
            ps.setInt(3, r.getCodTrotinete());
            ps.setInt(4, r.getCodCliente());
            ps.setInt(5, r.getCodCriador());
            if (os.getCodMecanico() == null)
                ps.setNull(6, java.sql.Types.INTEGER);
            else
                ps.setInt(6, os.getCodMecanico());
            ps.setString(7, os.getEstado().name());
            ps.setInt(8, id);
            ps.executeUpdate();
        }
    }

    // Apaga o que pendura na OS. CASCADE remove os "netos".
    private void clearChildren(Connection c, int id) throws SQLException {
        for (String tbl : new String[]{
                "Conserto", "Diagnostico", "Pagamento", "OrdemServico_Acessorio"}) {
            try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM " + tbl + " WHERE idOS = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    private void insertAcessorios(Connection c, int idOS, List<String> acessorios) throws SQLException {
        if (acessorios == null || acessorios.isEmpty()) return;
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO OrdemServico_Acessorio (idOS, ordem, valor) VALUES (?, ?, ?)")) {
            int i = 0;
            for (String a : acessorios) {
                ps.setInt(1, idOS);
                ps.setInt(2, i++);
                ps.setString(3, a);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertPagamento(Connection c, int idOS, Pagamento p) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO Pagamento (idOS, metodo, dataPagamento, clienteNotificado, dataNotificacao) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, idOS);
            if (p.getMetodo() == null)
                ps.setNull(2, java.sql.Types.VARCHAR);
            else
                ps.setString(2, p.getMetodo().name());
            if (p.getDataPagamento() == null)
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(3, Timestamp.valueOf(p.getDataPagamento()));
            ps.setBoolean(4, p.isClienteNotificado());
            if (p.getDataNotificacao() == null)
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(5, Timestamp.valueOf(p.getDataNotificacao()));
            ps.executeUpdate();
        }
    }

    private void insertDiagnostico(Connection c, int idOS, Diagnostico d) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("""
                INSERT INTO Diagnostico (idOS, descricao, orcamento, aprovado)
                VALUES (?, ?, ?, ?)
                """)) {
            ps.setInt(1, idOS);
            ps.setString(2, d.getDescricao());
            ps.setFloat(3, d.getOrcamento());
            ps.setBoolean(4, d.isAprovado());
            ps.executeUpdate();
        }
        if (!d.getCod_reparacoes().isEmpty()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Diagnostico_Reparacao (idOS, codReparacao) VALUES (?, ?)")) {
                for (Integer cod : d.getCod_reparacoes()) {
                    ps.setInt(1, idOS);
                    ps.setInt(2, cod);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
        if (!d.getPecasOrcamento().isEmpty()) {
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO Diagnostico_PecaOrcamento (idOS, codPeca, quantidade)
                    VALUES (?, ?, ?)
                    """)) {
                for (Map.Entry<Integer, Integer> e : d.getPecasOrcamento().entrySet()) {
                    ps.setInt(1, idOS);
                    ps.setInt(2, e.getKey());
                    ps.setInt(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void insertConserto(Connection c, int idOS, Conserto con) throws SQLException {
        CheckList chk = con.getCheckList();
        try (PreparedStatement ps = c.prepareStatement("""
                INSERT INTO Conserto (idOS, preco_total,
                                      chk_luzes, chk_pneus, chk_aceleracao,
                                      chk_travagem, chk_visor, chk_teste_pratico)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """)) {
            ps.setInt(1, idOS);
            ps.setFloat(2, con.getPreco_total());
            ps.setBoolean(3, chk.getLuzes());
            ps.setBoolean(4, chk.getPneus());
            ps.setBoolean(5, chk.getAceleracao());
            ps.setBoolean(6, chk.getTravagem());
            ps.setBoolean(7, chk.getVisor());
            ps.setBoolean(8, chk.getTeste_pratico());
            ps.executeUpdate();
        }
        if (!con.getCod_reparacoes().isEmpty()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Conserto_Reparacao (idOS, codReparacao) VALUES (?, ?)")) {
                for (Integer cod : con.getCod_reparacoes()) {
                    ps.setInt(1, idOS);
                    ps.setInt(2, cod);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
        if (!con.getStocksUsados().isEmpty()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Conserto_PecaUsada (idOS, codStock, quantidade) VALUES (?, ?, ?)")) {
                for (Map.Entry<Integer, Integer> e : con.getStocksUsados().entrySet()) {
                    ps.setInt(1, idOS);
                    ps.setInt(2, e.getKey());
                    ps.setInt(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    // ---------------- Map<Integer, OrdemServico> ----------------

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM OrdemServico")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar ordens de serviço", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM OrdemServico WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar OS " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof OrdemServico os)) return false;
        OrdemServico stored = get(os.getId());
        return stored != null && stored.getEstado() == os.getEstado();
    }

    @Override
    public OrdemServico get(Object key) {
        if (!(key instanceof Integer id)) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(c, rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OS " + id, e);
        }
    }

    @Override
    public OrdemServico put(Integer key, OrdemServico value) {
        OrdemServico prev = get(key);
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                updateBase(c, key, value);
                clearChildren(c, key);
                insertAcessorios(c, key, value.getRegisto().getAcessorios());
                if (value.getDiagnostico() != null) insertDiagnostico(c, key, value.getDiagnostico());
                if (value.getConserto() != null)    insertConserto(c, key, value.getConserto());
                if (value.getPagamento() != null)   insertPagamento(c, key, value.getPagamento());
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw new EcoRideException("Erro a gravar OS " + key, e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação ao gravar OS " + key, e);
        }
        return prev;
    }

    public int insert(OrdemServico value) {
        String sql = """
                INSERT INTO OrdemServico (descricao, data_criacao, codTrotinete,
                                          codCliente, codCriador, codMecanico, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    Registo r = value.getRegisto();
                    ps.setString(1, r.getDescricao());
                    ps.setTimestamp(2, Timestamp.valueOf(r.getDataCriacao()));
                    ps.setInt(3, r.getCodTrotinete());
                    ps.setInt(4, r.getCodCliente());
                    ps.setInt(5, r.getCodCriador());
                    if (value.getCodMecanico() == null)
                        ps.setNull(6, java.sql.Types.INTEGER);
                    else
                        ps.setInt(6, value.getCodMecanico());
                    ps.setString(7, value.getEstado().name());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) { id = rs.getInt(1); value.setId(id); }
                        else throw new EcoRideException("Sem ID gerado para OS");
                    }
                }
                insertAcessorios(c, id, value.getRegisto().getAcessorios());
                if (value.getDiagnostico() != null) insertDiagnostico(c, id, value.getDiagnostico());
                if (value.getConserto() != null)    insertConserto(c, id, value.getConserto());
                if (value.getPagamento() != null)   insertPagamento(c, id, value.getPagamento());
                c.commit();
                return id;
            } catch (SQLException e) {
                c.rollback();
                throw new EcoRideException("Erro a inserir OS", e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação ao inserir OS", e);
        }
    }

    @Override
    public OrdemServico remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        OrdemServico prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM OrdemServico WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover OS " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends OrdemServico> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM OrdemServico");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar ordens de serviço", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM OrdemServico")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de OSs", e);
        }
        return out;
    }

    @Override
    public Collection<OrdemServico> values() {
        Set<OrdemServico> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(SELECT_BASE + " ORDER BY id")) {
            while (rs.next()) out.add(buildFromRow(c, rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OSs", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, OrdemServico>> entrySet() {
        Set<Entry<Integer, OrdemServico>> out = new HashSet<>();
        for (OrdemServico os : values()) {
            out.add(new AbstractMap.SimpleEntry<>(os.getId(), os));
        }
        return out;
    }

    // ---------------- queries específicas ----------------

    public List<OrdemServico> getOSDoCliente(int idCliente) {
        List<OrdemServico> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_BASE + " WHERE codCliente = ? ORDER BY id")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(c, rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OSs do cliente " + idCliente, e);
        }
        return out;
    }

    public List<OrdemServico> getOSDoTrotinete(int idTrotinete) {
        List<OrdemServico> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_BASE + " WHERE codTrotinete = ? ORDER BY id")) {
            ps.setInt(1, idTrotinete);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(c, rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OSs da trotinete " + idTrotinete, e);
        }
        return out;
    }

    public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate,
                                         Integer idCliente, Integer idFuncionario) {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append(" WHERE 1 = 1");
        List<Object> params = new ArrayList<>();
        if (estado != null)        { sql.append(" AND estado = ?");        params.add(estado.name()); }
        if (desde != null)         { sql.append(" AND data_criacao >= ?"); params.add(Timestamp.valueOf(desde)); }
        if (ate != null)           { sql.append(" AND data_criacao <= ?"); params.add(Timestamp.valueOf(ate)); }
        if (idCliente != null)     { sql.append(" AND codCliente = ?");    params.add(idCliente); }
        if (idFuncionario != null) { sql.append(" AND codMecanico = ?");   params.add(idFuncionario); }
        sql.append(" ORDER BY id");

        List<OrdemServico> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(c, rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a filtrar OSs", e);
        }
        return out;
    }

    public List<OrdemServico> getAvailableOSs() {
        List<OrdemServico> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_BASE + " WHERE estado IN (?, ?, ?, ?) ORDER BY id")) {
            ps.setString(1, EstadoOS.PendenteDiagnostico.name());
            ps.setString(2, EstadoOS.PendenteAprovacaoOrcamento.name());
            ps.setString(3, EstadoOS.PendenteReparacao.name());
            ps.setString(4, EstadoOS.AguardarPecas.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(c, rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OSs disponíveis", e);
        }
        return out;
    }

    public List<OrdemServico> getOSsAtivas() {
        List<OrdemServico> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_BASE + " WHERE estado IN (?, ?, ?, ?, ?, ?) ORDER BY id")) {
            ps.setString(1, EstadoOS.PendenteDiagnostico.name());
            ps.setString(2, EstadoOS.PendenteAprovacaoOrcamento.name());
            ps.setString(3, EstadoOS.PendenteReparacao.name());
            ps.setString(4, EstadoOS.AguardarPecas.name());
            ps.setString(5, EstadoOS.ClienteNotificado.name());
            ps.setString(6, EstadoOS.PendentePagamento.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(c, rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OSs ativas", e);
        }
        return out;
    }
}
