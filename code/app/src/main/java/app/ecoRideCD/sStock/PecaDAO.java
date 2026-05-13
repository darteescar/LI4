package app.ecoRideCD.sStock;

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
import app.ecoRideLN.sStock.Peca;

public class PecaDAO implements Map<Integer, Peca> {
    private static PecaDAO instance;

    private PecaDAO() {}

    public static PecaDAO getInstance() {
        if (instance == null) instance = new PecaDAO();
        return instance;
    }

    private static final String BASE_SELECT =
            "SELECT id, referencia, marca, nome, descricao, stock_minimo, preco_venda, codFornecedor, ativa, garantia FROM Peca";

    private Peca buildFromRow(ResultSet rs) throws SQLException {
        return new Peca(
                rs.getInt("id"),
                rs.getString("referencia"),
                rs.getString("marca"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getInt("stock_minimo"),
                rs.getFloat("preco_venda"),
                rs.getInt("codFornecedor"),
                rs.getBoolean("ativa"),
                rs.getInt("garantia")
        );
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Peca")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar pecas", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Peca WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar peca " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Peca p)) return false;
        Peca stored = get(p.getId());
        return stored != null && stored.getReferencia().equals(p.getReferencia());
    }

    @Override
    public Peca get(Object key) {
        if (!(key instanceof Integer id)) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(BASE_SELECT + " WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter peca " + id, e);
        }
    }

    @Override
    public Peca put(Integer key, Peca value) {
        Peca prev = get(key);
        String sql = """
                INSERT INTO Peca (id, referencia, marca, nome, descricao, stock_minimo, preco_venda, codFornecedor, ativa, garantia)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    referencia = VALUES(referencia), marca = VALUES(marca), nome = VALUES(nome), descricao = VALUES(descricao),
                    stock_minimo = VALUES(stock_minimo), preco_venda = VALUES(preco_venda),
                    codFornecedor = VALUES(codFornecedor), ativa = VALUES(ativa), garantia = VALUES(garantia)
                """;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setString(2, value.getReferencia());
            ps.setString(3, value.getMarca());
            ps.setString(4, value.getNome());
            ps.setString(5, value.getDescricao());
            ps.setInt(6, value.getStock_minimo());
            ps.setFloat(7, value.getPreco_venda());
            ps.setInt(8, value.getCodFornecedor());
            ps.setBoolean(9, value.isAtiva());
            ps.setInt(10, value.getGarantia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar peca " + key, e);
        }
        return prev;
    }

    @Override
    public Peca remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Peca prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Peca WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover peca " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Peca> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Peca");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar pecas", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Peca")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de pecas", e);
        }
        return out;
    }

    @Override
    public Collection<Peca> values() {
        Set<Peca> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(BASE_SELECT)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter pecas", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Peca>> entrySet() {
        Set<Entry<Integer, Peca>> out = new HashSet<>();
        for (Peca p : values()) out.add(new AbstractMap.SimpleEntry<>(p.getId(), p));
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Peca p) { put(p.getId(), p); }

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Peca")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para peca", e);
        }
    }

    public boolean getByReference(String ref) {
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Peca WHERE referencia = ?")) {
            ps.setString(1, ref);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar peca por referencia " + ref, e);
        }
    }

}
