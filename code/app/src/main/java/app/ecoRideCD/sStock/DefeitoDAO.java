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
import app.ecoRideLN.sStock.Defeito;
import app.ecoRideLN.sStock.EstadoStock;

public class DefeitoDAO implements Map<Integer, Defeito> {

    private static DefeitoDAO instance;

    protected DefeitoDAO() {}

    public static DefeitoDAO getInstance() {
        if (instance == null) instance = new DefeitoDAO();
        return instance;
    }

    private Defeito buildFromRow(ResultSet rs) throws SQLException {
        return new Defeito(
                rs.getInt("id"),
                rs.getInt("codStock"),
                rs.getString("motivo"),
                rs.getInt("idFuncionario"),
                EstadoStock.valueOf(rs.getString("estado_anterior")));
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Defeito")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar defeitos", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Defeito WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar defeito " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Defeito d)) return false;
        return get(d.getId()) != null;
    }

    @Override
    public Defeito get(Object key) {
        if (!(key instanceof Integer id)) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id, codStock, motivo, idFuncionario, estado_anterior FROM Defeito WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter defeito " + id, e);
        }
    }

    @Override
    public Defeito put(Integer key, Defeito value) {
        Defeito prev = get(key);
        String sql = "UPDATE Defeito SET codStock=?, motivo=?, idFuncionario=?, estado_anterior=? WHERE id=?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, value.getCodStock());
            ps.setString(2, value.getMotivo());
            ps.setInt(3, value.getIdFuncionario());
            ps.setString(4, value.getEstadoAnterior().name());
            ps.setInt(5, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar defeito " + key, e);
        }
        return prev;
    }

    public int insert(Defeito value) {
        String sql = "INSERT INTO Defeito (codStock, motivo, idFuncionario, estado_anterior) VALUES (?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, value.getCodStock());
            ps.setString(2, value.getMotivo());
            ps.setInt(3, value.getIdFuncionario());
            ps.setString(4, value.getEstadoAnterior().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para defeito");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir defeito", e);
        }
    }

    @Override
    public Defeito remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Defeito prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Defeito WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover defeito " + id, e);
        }
        return prev;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Defeito> m) { m.forEach(this::put); }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Defeito");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar defeitos", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Defeito")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de defeitos", e);
        }
        return out;
    }

    @Override
    public Collection<Defeito> values() {
        Set<Defeito> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id, codStock, motivo, idFuncionario, estado_anterior FROM Defeito")) {
            while (rs.next()) out.add(buildFromRow(rs));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter defeitos", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Defeito>> entrySet() {
        Set<Entry<Integer, Defeito>> out = new HashSet<>();
        for (Defeito d : values())
            out.add(new AbstractMap.SimpleEntry<>(d.getId(), d));
        return out;
    }

}
