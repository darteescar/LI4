package app.ecoRideCD.sOrdensServico;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sOrdensServico.CheckList;
import app.ecoRideLN.sOrdensServico.Conserto;
import app.ecoRideLN.sOrdensServico.Diagnostico;
import app.ecoRideLN.sOrdensServico.EstadoOS;
import app.ecoRideLN.sOrdensServico.Fotografia;
import app.ecoRideLN.sOrdensServico.Metodo_Pagamento;
import app.ecoRideLN.sOrdensServico.OrdemServico;

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

public class OrdemServicoDAO implements Map<Integer, OrdemServico> {

    private static OrdemServicoDAO instance;

    private OrdemServicoDAO() {}

    public static OrdemServicoDAO getInstance() {
        if (instance == null) instance = new OrdemServicoDAO();
        return instance;
    }

    // SELECT só dos campos da tabela base; os filhos são carregados em queries separadas.
    private static final String SELECT_BASE = """
            SELECT id, descricao, data_criacao, codTrotinete, codCliente,
                   codCriador, codMecanico, estado, metodo_pagamento
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

    private List<Fotografia> loadFotografias(Connection c, int idOS) throws SQLException {
        List<Fotografia> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT caminho FROM Fotografia WHERE idOS = ? ORDER BY id")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Fotografia(rs.getString("caminho")));
                }
            }
        }
        return out;
    }

    private Diagnostico loadDiagnostico(Connection c, int idOS) throws SQLException {
        String desc;
        float orc;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT descricao, orcamento FROM Diagnostico WHERE idOS = ?")) {
            ps.setInt(1, idOS);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                desc = rs.getString("descricao");
                orc = rs.getFloat("orcamento");
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
        return new Diagnostico(desc, reps, pecas, orc);
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
        OrdemServico os = new OrdemServico(
                id,
                rs.getString("descricao"),
                rs.getTimestamp("data_criacao").toLocalDateTime(),
                rs.getInt("codTrotinete"),
                rs.getInt("codCliente"),
                rs.getInt("codCriador"),
                loadFotografias(c, id),
                loadAcessorios(c, id));
        os.setEstado(EstadoOS.valueOf(rs.getString("estado")));
        os.setCodMecanico(rs.getObject("codMecanico", Integer.class));
        String mp = rs.getString("metodo_pagamento");
        if (mp != null) os.setMetodo_pagamento(Metodo_Pagamento.valueOf(mp));
        Diagnostico d = loadDiagnostico(c, id);
        if (d != null) os.setDiagnostico(d);
        Conserto con = loadConserto(c, id);
        if (con != null) os.setConserto(con);
        return os;
    }

    // ---------------- Helpers de escrita ----------------

    private void upsertBase(Connection c, int id, OrdemServico os) throws SQLException {
        String sql = """
                INSERT INTO OrdemServico (id, descricao, data_criacao, codTrotinete,
                                          codCliente, codCriador, codMecanico, estado, metodo_pagamento)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    descricao        = VALUES(descricao),
                    data_criacao     = VALUES(data_criacao),
                    codTrotinete     = VALUES(codTrotinete),
                    codCliente       = VALUES(codCliente),
                    codCriador       = VALUES(codCriador),
                    codMecanico      = VALUES(codMecanico),
                    estado           = VALUES(estado),
                    metodo_pagamento = VALUES(metodo_pagamento)
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, os.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(os.getDataCriacao()));
            ps.setInt(4, os.getCodTrotinete());
            ps.setInt(5, os.getCodCliente());
            ps.setInt(6, os.getCodCriador());
            if (os.getCodMecanico() == null)
                ps.setNull(7, java.sql.Types.INTEGER);
            else
                ps.setInt(7, os.getCodMecanico());
            ps.setString(8, os.getEstado().name());
            if (os.getMetodo_pagamento() == null)
                ps.setNull(9, java.sql.Types.VARCHAR);
            else
                ps.setString(9, os.getMetodo_pagamento().name());
            ps.executeUpdate();
        }
    }

    // Apaga directamente o que pendura na OS. CASCADE remove os "netos"
    // (Diagnostico_PecaOrcamento, Conserto_PecaUsada, etc.).
    private void clearChildren(Connection c, int id) throws SQLException {
        for (String tbl : new String[]{
                "Conserto", "Diagnostico", "Fotografia", "OrdemServico_Acessorio" }) {
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

    private void insertFotografias(Connection c, int idOS, List<Fotografia> fotos) throws SQLException {
        if (fotos == null || fotos.isEmpty()) return;
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO Fotografia (idOS, caminho) VALUES (?, ?)")) {
            for (Fotografia f : fotos) {
                ps.setInt(1, idOS);
                ps.setString(2, f.getCaminho());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertDiagnostico(Connection c, int idOS, Diagnostico d) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("""
                INSERT INTO Diagnostico (idOS, descricao, orcamento)
                VALUES (?, ?, ?)
                """)) {
            ps.setInt(1, idOS);
            ps.setString(2, d.getDescricao());
            ps.setFloat(3, d.getOrcamento());
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
                upsertBase(c, key, value);
                clearChildren(c, key);
                insertAcessorios(c, key, value.getAcessorios());
                insertFotografias(c, key, value.getFotografias());
                if (value.getDiagnostico() != null) insertDiagnostico(c, key, value.getDiagnostico());
                if (value.getConserto() != null)    insertConserto(c, key, value.getConserto());
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

    @Override
    public OrdemServico remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        OrdemServico prev = get(id);
        if (prev == null) return null;
        // ON DELETE CASCADE nas tabelas filhas faz a limpeza automaticamente.
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

    // ---------------- generateNewId + queries específicas ----------------

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM OrdemServico")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para OS", e);
        }
    }

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

    // Filtra por qualquer combinação dos critérios; passar null ignora o critério.
    public List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate,
                                         Integer idCliente, Integer idFuncionario) {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append(" WHERE 1 = 1");
        List<Object> params = new ArrayList<>();
        if (estado != null)        { sql.append(" AND estado = ?");        params.add(estado.name()); }
        if (desde != null)         { sql.append(" AND data_criacao >= ?"); params.add(Timestamp.valueOf(desde)); }
        if (ate != null)           { sql.append(" AND data_criacao <= ?"); params.add(Timestamp.valueOf(ate)); }
        if (idCliente != null)     { sql.append(" AND codCliente = ?");    params.add(idCliente); }
        if (idFuncionario != null) { sql.append(" AND codMecanico = ?"); params.add(idFuncionario); }
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
}
