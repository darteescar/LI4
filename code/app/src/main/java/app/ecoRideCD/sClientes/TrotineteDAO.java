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
import app.ecoRideLN.sClientes.Trotinete;

public class TrotineteDAO implements Map<Integer, Trotinete> {
    private static TrotineteDAO instance;

    private TrotineteDAO() {}

    public static TrotineteDAO getInstance() {
        if (instance == null) instance = new TrotineteDAO();
        return instance;
    }

    // codsOrdensServico deriva de OrdemServico.cod_trotinete.
    // Devolve [] enquanto a tabela OrdemServico não existir (subsistema futuro).
    private List<Integer> osDaTrotinete(int idTrotinete) {
        List<Integer> out = new ArrayList<>();
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id FROM OrdemServico WHERE codTrotinete = ?")) {
            ps.setInt(1, idTrotinete);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getInt(1));
            }
        } catch (SQLException ignore) {
            // tabela OrdemServico ainda não criada; lista vazia
        }
        return out;
    }

    private Trotinete buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return new Trotinete(
                id,
                rs.getString("modelo"),
                rs.getString("marca"),
                rs.getString("num_serie"),
                rs.getString("tipo_motor"),
                rs.getInt("cod_cliente"),
                osDaTrotinete(id));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Trotinete")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar trotinetes", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Trotinete WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar trotinete " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsKey(((Trotinete) value).getId());
    }

    @Override
    public Trotinete get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = "SELECT id, modelo, marca, num_serie, tipo_motor, cod_cliente FROM Trotinete WHERE id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter trotinete " + id, e);
        }
    }

    @Override
    public Trotinete put(Integer key, Trotinete value) {
        Trotinete prev = get(key);
        String sql = "UPDATE Trotinete SET modelo=?, marca=?, num_serie=?, tipo_motor=?, cod_cliente=? WHERE id=?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value.getModelo());
            ps.setString(2, value.getMarca());
            ps.setString(3, value.getNum_serie());
            ps.setString(4, value.getTipo_motor());
            ps.setInt(5, value.getCod_cliente());
            ps.setInt(6, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar trotinete " + key, e);
        }
        return prev;
    }

    public int insert(Trotinete value) {
        String sql = "INSERT INTO Trotinete (modelo, marca, num_serie, tipo_motor, cod_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, value.getModelo());
            ps.setString(2, value.getMarca());
            ps.setString(3, value.getNum_serie());
            ps.setString(4, value.getTipo_motor());
            ps.setInt(5, value.getCod_cliente());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para trotinete");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir trotinete", e);
        }
    }

    @Override
    public Trotinete remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Trotinete prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Trotinete WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover trotinete " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Trotinete> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Trotinete");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar trotinetes", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Trotinete")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de trotinetes", e);
        }
        return out;
    }

    @Override
    public Collection<Trotinete> values() {
        Set<Trotinete> out = new LinkedHashSet<>();
        String sql = "SELECT id, modelo, marca, num_serie, tipo_motor, cod_cliente FROM Trotinete";
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter trotinetes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Trotinete>> entrySet() {
        Set<Entry<Integer, Trotinete>> out = new HashSet<>();
        for (Trotinete t : values()) out.add(new AbstractMap.SimpleEntry<>(t.getId(), t));
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Trotinete t)               { put(t.getId(), t); }
}
