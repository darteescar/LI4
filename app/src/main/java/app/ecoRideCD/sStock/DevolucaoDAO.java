package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import app.common.EcoRideException;
import app.ecoRideCD.DAOconfig.ConnectionFactory;
import app.ecoRideLN.sStock.Devolucao;
import app.ecoRideLN.sStock.EstadoDevolucao;

public class DevolucaoDAO implements Map<Integer, Devolucao> {

    private static DevolucaoDAO instance;

    private DevolucaoDAO() {
    }

    public static DevolucaoDAO getInstance() {
        if (instance == null) {
            instance = new DevolucaoDAO();
        }
        return instance;
    }

    private Devolucao buildFromRow(ResultSet rs) throws SQLException {
        return new Devolucao(
                rs.getInt("id"),
                rs.getTimestamp("data").toLocalDateTime(),
                rs.getString("motivo"),
                EstadoDevolucao.valueOf(rs.getString("estado")),
                rs.getInt("codStock"));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Devolucao")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar devolucoes", e);
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
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Devolucao WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar devolucao " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Devolucao d)) {
            return false;
        }
        Devolucao stored = get(d.getId());
        return stored != null && stored.getCodStock() == d.getCodStock();
    }

    @Override
    public Devolucao get(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        String sql = "SELECT id, data, motivo, estado, codStock FROM Devolucao WHERE id = ?";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter devolucao " + id, e);
        }
    }

    @Override
    public Devolucao put(Integer key, Devolucao value) {
        Devolucao prev = get(key);
        String sql = """
                INSERT INTO Devolucao (id, data, motivo, estado, codStock)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    data = VALUES(data), motivo = VALUES(motivo),
                    estado = VALUES(estado), codStock = VALUES(codStock)
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setTimestamp(2, Timestamp.valueOf(value.getData()));
            ps.setString(3, value.getMotivo());
            ps.setString(4, value.getEstado().name());
            ps.setInt(5, value.getCodStock());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar devolucao " + key, e);
        }
        return prev;
    }

    @Override
    public Devolucao remove(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        Devolucao prev = get(id);
        if (prev == null) {
            return null;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM Devolucao WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover devolucao " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Devolucao> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Devolucao");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar devolucoes", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id FROM Devolucao")) {
            while (rs.next()) {
                out.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de devolucoes", e);
        }
        return out;
    }

    @Override
    public Collection<Devolucao> values() {
        Set<Devolucao> out = new LinkedHashSet<>();
        String sql = "SELECT id, data, motivo, estado, codStock FROM Devolucao";
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter devolucoes", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Devolucao>> entrySet() {
        Set<Entry<Integer, Devolucao>> out = new HashSet<>();
        for (Devolucao d : values()) {
            out.add(new AbstractMap.SimpleEntry<>(d.getId(), d));
        }
        return out;
    }

    // --------- Aliases / domínio ---------

    public Devolucao getDevolucaoById(int id) {
        return get(id);
    }

    public void add(Devolucao d) {
        put(d.getId(), d);
    }

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Devolucao")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para devolucao", e);
        }
    }
}
