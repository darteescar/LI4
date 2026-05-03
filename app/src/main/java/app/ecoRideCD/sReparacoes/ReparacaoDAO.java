package app.ecoRideCD.sReparacoes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sReparacoes.Reparacao;

public class ReparacaoDAO implements Map<Integer, Reparacao> {
    private static ReparacaoDAO instance;

    private ReparacaoDAO() {}

    public static ReparacaoDAO getInstance() {
        if (instance == null) instance = new ReparacaoDAO();
        return instance;
    }

    private Reparacao buildFromRow(ResultSet rs) throws SQLException {
        return new Reparacao(
                rs.getInt("id"),
                rs.getString("nomenclatura"),
                rs.getString("descricao"),
                rs.getFloat("preco"),
                rs.getBoolean("disponivel"));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Reparacao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar reparacoes", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Reparacao WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar reparacao " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Reparacao r)) return false;
        Reparacao stored = get(r.getId());
        return stored != null && stored.getNomenclatura().equals(r.getNomenclatura());
    }

    @Override
    public Reparacao get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = "SELECT id, nomenclatura, descricao, preco, disponivel FROM Reparacao WHERE id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter reparacao " + id, e);
        }
    }

    @Override
    public Reparacao put(Integer key, Reparacao value) {
        Reparacao prev = get(key);
        String sql = """
                INSERT INTO Reparacao (id, nomenclatura, descricao, preco, disponivel)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    nomenclatura = VALUES(nomenclatura), descricao = VALUES(descricao),
                    preco = VALUES(preco), disponivel = VALUES(disponivel)
                """;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setString(2, value.getNomenclatura());
            ps.setString(3, value.getDescricao());
            ps.setFloat(4, value.getPreco());
            ps.setBoolean(5, value.isDisponivel());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar reparacao " + key, e);
        }
        return prev;
    }

    @Override
    public Reparacao remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Reparacao prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Reparacao WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover reparacao " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Reparacao> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Reparacao");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar reparacoes", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Reparacao")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de reparacoes", e);
        }
        return out;
    }

    @Override
    public Collection<Reparacao> values() {
        Set<Reparacao> out = new LinkedHashSet<>();
        String sql = "SELECT id, nomenclatura, descricao, preco, disponivel FROM Reparacao";
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter reparacoes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Reparacao>> entrySet() {
        Set<Entry<Integer, Reparacao>> out = new HashSet<>();
        for (Reparacao r : values()) out.add(new AbstractMap.SimpleEntry<>(r.getId(), r));
        return out;
    }

    // --------- Aliases / domínio ---------

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Reparacao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para reparacao", e);
        }
    }
}
