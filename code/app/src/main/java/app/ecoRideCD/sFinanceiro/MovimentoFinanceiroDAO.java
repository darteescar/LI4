package app.ecoRideCD.sFinanceiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.MovimentoFuncionario;
import app.ecoRideLN.sFinanceiro.MovimentoPeca;
import app.ecoRideLN.sFinanceiro.MovimentoReparacao;
import app.ecoRideLN.sFinanceiro.TipoMovimento;

public class MovimentoFinanceiroDAO implements Map<Integer, MovimentoFinanceiro> {

    private static MovimentoFinanceiroDAO instance;

    private MovimentoFinanceiroDAO() {}

    public static MovimentoFinanceiroDAO getInstance() {
        if (instance == null) instance = new MovimentoFinanceiroDAO();
        return instance;
    }

    // Uma única query traz a base + os 3 filhos via LEFT JOIN.
    // Em cada linha apenas uma das colunas codXxx será não-nula
    // (a que corresponde ao `tipo`).
    private static final String SELECT_JOIN = """
            SELECT mf.id, mf.valor, mf.data, mf.descricao, mf.tipo,
                   mfn.codFuncionario,
                   mr.codReparacao,
                   mp.codStock
            FROM   MovimentoFinanceiro mf
            LEFT   JOIN MovimentoFuncionario mfn ON mf.id = mfn.id
            LEFT   JOIN MovimentoReparacao   mr  ON mf.id = mr.id
            LEFT   JOIN MovimentoPeca        mp  ON mf.id = mp.id
            """;

    private MovimentoFinanceiro buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        float valor = rs.getFloat("valor");
        LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
        String desc = rs.getString("descricao");
        TipoMovimento tipo = TipoMovimento.valueOf(rs.getString("tipo"));

        return switch (tipo) {
            case Salario      -> new MovimentoFuncionario(id, desc, valor, data, tipo, rs.getInt("codFuncionario"));
            case LucroMaoObra -> new MovimentoReparacao(id, desc, valor, data, tipo, rs.getInt("codReparacao"));
            case GastoPecas, LucroVendaPecas
                              -> new MovimentoPeca(id, desc, valor, data, tipo, rs.getInt("codStock"));
        };
    }

    // --------- Map<Integer, MovimentoFinanceiro> ---------

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM MovimentoFinanceiro")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar movimentos financeiros", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM MovimentoFinanceiro WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar movimento " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof MovimentoFinanceiro m)) return false;
        MovimentoFinanceiro stored = get(m.getId());
        return stored != null && stored.getTipo() == m.getTipo()
                              && Float.compare(stored.getValor(), m.getValor()) == 0;
    }

    @Override
    public MovimentoFinanceiro get(Object key) {
        if (!(key instanceof Integer id)) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(SELECT_JOIN + " WHERE mf.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter movimento " + id, e);
        }
    }

    // Insert/update da tabela base.
    private void upsertBase(Connection c, int id, MovimentoFinanceiro m) throws SQLException {
        String sql = """
                INSERT INTO MovimentoFinanceiro (id, valor, data, descricao, tipo)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    valor     = VALUES(valor),
                    data      = VALUES(data),
                    descricao = VALUES(descricao),
                    tipo      = VALUES(tipo)
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setFloat(2, m.getValor());
            ps.setTimestamp(3, Timestamp.valueOf(m.getData()));
            ps.setString(4, m.getDescricao());
            ps.setString(5, m.getTipo().name());
            ps.executeUpdate();
        }
    }

    // Garante que apenas a tabela filha do subtipo atual tem linha para este id.
    // É chamado antes de inserir o filho novo para tratar do caso em que o subtipo mudou.
    private void clearChildren(Connection c, int id) throws SQLException {
        for (String tabela : new String[]{ "MovimentoFuncionario", "MovimentoReparacao", "MovimentoPeca" }) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM " + tabela + " WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    private void insertChild(Connection c, int id, MovimentoFinanceiro m) throws SQLException {
        switch (m) {
            case MovimentoFuncionario mf -> {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO MovimentoFuncionario (id, codFuncionario) VALUES (?, ?)")) {
                    ps.setInt(1, id);
                    ps.setInt(2, mf.getCodFuncionario());
                    ps.executeUpdate();
                }
            }
            case MovimentoReparacao mr -> {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO MovimentoReparacao (id, codReparacao) VALUES (?, ?)")) {
                    ps.setInt(1, id);
                    ps.setInt(2, mr.getCodReparacao());
                    ps.executeUpdate();
                }
            }
            case MovimentoPeca mp -> {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO MovimentoPeca (id, codStock) VALUES (?, ?)")) {
                    ps.setInt(1, id);
                    ps.setInt(2, mp.getCodStock());
                    ps.executeUpdate();
                }
            }
            default -> throw new EcoRideException(
                    "Subtipo desconhecido de MovimentoFinanceiro: " + m.getClass().getName());
        }
    }

    @Override
    public MovimentoFinanceiro put(Integer key, MovimentoFinanceiro value) {
        MovimentoFinanceiro prev = get(key);
        try (Connection c = ConnectionFactory.get()) {
            c.setAutoCommit(false);
            try {
                upsertBase(c, key, value);
                clearChildren(c, key);
                insertChild(c, key, value);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw new EcoRideException("Erro a gravar movimento " + key, e);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro de ligação ao gravar movimento " + key, e);
        }
        return prev;
    }

    @Override
    public MovimentoFinanceiro remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        MovimentoFinanceiro prev = get(id);
        if (prev == null) return null;
        // ON DELETE CASCADE nas tabelas filhas faz a limpeza automaticamente.
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM MovimentoFinanceiro WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover movimento " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends MovimentoFinanceiro> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM MovimentoFinanceiro");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar movimentos", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM MovimentoFinanceiro")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de movimentos", e);
        }
        return out;
    }

    @Override
    public Collection<MovimentoFinanceiro> values() {
        Set<MovimentoFinanceiro> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(SELECT_JOIN + " ORDER BY mf.id")) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter movimentos", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, MovimentoFinanceiro>> entrySet() {
        Set<Entry<Integer, MovimentoFinanceiro>> out = new HashSet<>();
        for (MovimentoFinanceiro m : values()) {
            out.add(new AbstractMap.SimpleEntry<>(m.getId(), m));
        }
        return out;
    }

    // --------- Aliases / domínio ---------

    public boolean exists(int id)                       { return containsKey(id); }
    public void add(MovimentoFinanceiro m)              { put(m.getId(), m); }

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM MovimentoFinanceiro")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para movimento", e);
        }
    }
    public void removeByStock(int codStock) {
        String sql = "SELECT id FROM MovimentoPeca WHERE codStock = ?";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, codStock);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    remove(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter movimentos por stock " + codStock, e);
        }
    }}
