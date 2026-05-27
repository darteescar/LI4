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
import app.ecoRideLN.sStock.Fornecedor;

public class FornecedorDAO implements Map<Integer, Fornecedor> {
    private static FornecedorDAO instance;

    protected FornecedorDAO() {}

    public static FornecedorDAO getInstance() {
        if (instance == null) instance = new FornecedorDAO();
        return instance;
    }

    private Fornecedor buildFromRow(ResultSet rs) throws SQLException {
        return new Fornecedor(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("telemovel"),
                rs.getString("email"));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Fornecedor")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar fornecedores", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Fornecedor WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar fornecedor " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Fornecedor f)) return false;
        Fornecedor stored = get(f.getId());
        return stored != null && stored.getNome().equals(f.getNome());
    }

    @Override
    public Fornecedor get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = "SELECT id, nome, telemovel, email FROM Fornecedor WHERE id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter fornecedor " + id, e);
        }
    }

    @Override
    public Fornecedor put(Integer key, Fornecedor value) {
        Fornecedor prev = get(key);
        String sql = "UPDATE Fornecedor SET nome=?, telemovel=?, email=? WHERE id=?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value.getNome());
            ps.setString(2, value.getTelemovel());
            ps.setString(3, value.getEmail());
            ps.setInt(4, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar fornecedor " + key, e);
        }
        return prev;
    }

    public int insert(Fornecedor value) {
        String sql = "INSERT INTO Fornecedor (nome, telemovel, email) VALUES (?, ?, ?)";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, value.getNome());
            ps.setString(2, value.getTelemovel());
            ps.setString(3, value.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para fornecedor");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir fornecedor", e);
        }
    }

    @Override
    public Fornecedor remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Fornecedor prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Fornecedor WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover fornecedor " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Fornecedor> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Fornecedor");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar fornecedores", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Fornecedor")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de fornecedores", e);
        }
        return out;
    }

    @Override
    public Collection<Fornecedor> values() {
        Set<Fornecedor> out = new LinkedHashSet<>();
        String sql = "SELECT id, nome, telemovel, email FROM Fornecedor";
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter fornecedores", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Fornecedor>> entrySet() {
        Set<Entry<Integer, Fornecedor>> out = new HashSet<>();
        for (Fornecedor f : values()) out.add(new AbstractMap.SimpleEntry<>(f.getId(), f));
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Fornecedor f)                  { put(f.getId(), f); }
}
