package app.ecoRideCD.sClientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import app.ecoRideLN.sClientes.Cliente;

public class ClienteDAO implements Map<Integer, Cliente> {
    private static ClienteDAO instance;

    protected ClienteDAO() {}

    public static ClienteDAO getInstance() {
        if (instance == null) instance = new ClienteDAO();
        return instance;
    }

    // codsTrotinetes deriva de Trotinete.cod_cliente; consultado aqui.
    private List<Integer> trotinetesDoCliente(int idCliente) throws SQLException {
        List<Integer> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT id FROM Trotinete WHERE cod_cliente = ?")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getInt(1));
            }
        }
        return out;
    }

    private Cliente buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return new Cliente(
                id,
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telemovel"),
                rs.getString("NIF"),
                trotinetesDoCliente(id));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Cliente")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar clientes", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Cliente WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar cliente " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Cliente cli)) return false;
        Cliente stored = get(cli.getId());
        return stored != null && stored.getNIF() != null && stored.getNIF().equals(cli.getNIF());
    }

    @Override
    public Cliente get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = "SELECT id, nome, email, telemovel, NIF FROM Cliente WHERE id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter cliente " + id, e);
        }
    }

    @Override
    public Cliente put(Integer key, Cliente value) {
        Cliente prev = get(key);
        String sql = "UPDATE Cliente SET nome=?, email=?, telemovel=?, NIF=? WHERE id=?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value.getNome());
            ps.setString(2, value.getEmail());
            ps.setString(3, value.getTelemovel());
            ps.setString(4, value.getNIF());
            ps.setInt(5, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar cliente " + key, e);
        }
        return prev;
    }

    public int insert(Cliente value) {
        String sql = "INSERT INTO Cliente (nome, email, telemovel, NIF) VALUES (?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, value.getNome());
            ps.setString(2, value.getEmail());
            ps.setString(3, value.getTelemovel());
            ps.setString(4, value.getNIF());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para cliente");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir cliente", e);
        }
    }

    @Override
    public Cliente remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Cliente prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Cliente WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover cliente " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Cliente> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Cliente");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar clientes", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Cliente")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de clientes", e);
        }
        return out;
    }

    @Override
    public Collection<Cliente> values() {
        Set<Cliente> out = new LinkedHashSet<>();
        String sql = "SELECT id, nome, email, telemovel, NIF FROM Cliente";
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter clientes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Cliente>> entrySet() {
        Set<Entry<Integer, Cliente>> out = new HashSet<>();
        for (Cliente cli : values()) out.add(new AbstractMap.SimpleEntry<>(cli.getId(), cli));
        return out;
    }

    // --------- Aliases / domínio ---------

}
