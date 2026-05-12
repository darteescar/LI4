package app.ecoRideCD.sAutenticacao;

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
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;

public class UtilizadorDAO implements Map<Integer, Utilizador> {
    private static UtilizadorDAO instance;

    private UtilizadorDAO() {}

    public static UtilizadorDAO getInstance() {
        if (instance == null) instance = new UtilizadorDAO();
        return instance;
    }

    // --------- Map<Integer, Utilizador> ---------

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Utilizador")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar utilizadores", e);
        }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer id)) return false;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Utilizador WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar utilizador " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Utilizador u)) return false;
        Utilizador stored = get(u.getId());
        return stored != null && stored.getPassword().equals(u.getPassword())
                              && stored.getIdFuncionario() == u.getIdFuncionario()
                              && stored.getCargo() == u.getCargo()
                              && stored.getIdentificador().equals(u.getIdentificador());
    }

    @Override
    public Utilizador get(Object key) {
        if (!(key instanceof Integer id)) return null;
        String sql = "SELECT id, password, idFuncionario, cargo, identificador FROM Utilizador WHERE id = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Utilizador(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getInt("idFuncionario"),
                        Cargo.valueOf(rs.getString("cargo")),
                        rs.getString("identificador")
                );
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter utilizador " + id, e);
        }
    }

    @Override
    public Utilizador put(Integer key, Utilizador value) {
        Utilizador prev = get(key);
        String sql = """
                INSERT INTO Utilizador (id, password, idFuncionario, cargo, identificador)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    password      = VALUES(password),
                    idFuncionario = VALUES(idFuncionario),
                    cargo         = VALUES(cargo),
                    identificador = VALUES(identificador)
                """;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setString(2, value.getPassword());
            ps.setInt(3, value.getIdFuncionario());
            ps.setString(4, value.getCargo().name());
            ps.setString(5, value.getIdentificador());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar utilizador " + key, e);
        }
        return prev;
    }

    @Override
    public Utilizador remove(Object key) {
        if (!(key instanceof Integer id)) return null;
        Utilizador prev = get(id);
        if (prev == null) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Utilizador WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover utilizador " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Utilizador");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar utilizadores", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM Utilizador")) {
            while (rs.next()) out.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de utilizadores", e);
        }
        return out;
    }

    @Override
    public Collection<Utilizador> values() {
        Set<Utilizador> out = new LinkedHashSet<>();
        String sql = "SELECT id, password, idFuncionario, cargo, identificador FROM Utilizador";
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(new Utilizador(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getInt("idFuncionario"),
                        Cargo.valueOf(rs.getString("cargo")),
                        rs.getString("identificador")
                ));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter utilizadores", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Utilizador>> entrySet() {
        Set<Entry<Integer, Utilizador>> out = new HashSet<>();
        for (Utilizador u : values()) out.add(new AbstractMap.SimpleEntry<>(u.getId(), u));
        return out;
    }

    // --------- Aliases / domínio ---------

    public void add(Utilizador u)            { put(u.getId(), u); }

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Utilizador")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para utilizador", e);
        }
    }

    public boolean existeIdentificador(String identificador) {
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Utilizador WHERE identificador = ?")) {
            ps.setString(1, identificador);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar identificador", e);
        }
    }

    public Utilizador getByIdentificador(String identificador) {
        String sql = "SELECT id, password, idFuncionario, cargo, identificador FROM Utilizador WHERE identificador = ?";
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, identificador);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Utilizador(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getInt("idFuncionario"),
                        Cargo.valueOf(rs.getString("cargo")),
                        rs.getString("identificador")
                );
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter utilizador por identificador", e);
        }
    }

    public void updatePassword(int id, String novaPassword) {
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement("UPDATE Utilizador SET password = ? WHERE id = ?")) {
            ps.setString(1, novaPassword);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a actualizar password do utilizador " + id, e);
        }
    }
}
