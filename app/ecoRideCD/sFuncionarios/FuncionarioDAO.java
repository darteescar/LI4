package app.ecoRideCD.sFuncionarios;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sFuncionarios.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FuncionarioDAO implements Map<Integer, Funcionario> {

    private static FuncionarioDAO singleton = null;

    private FuncionarioDAO() {
    }

    public static FuncionarioDAO getInstance() {
        if (singleton == null) {
            singleton = new FuncionarioDAO();
        }
        return singleton;
    }

    public Optional<Funcionario> obterPorId(int id) {
        return Optional.ofNullable(this.get(id));
    }

    public Optional<Funcionario> obterPorNif(String nif) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Funcionario WHERE NIF=?")) {
            pstm.setString(1, nif);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return obterPorId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Funcionario fromRS(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("telemovel"),
                rs.getString("email"),
                rs.getString("data_nascimento"),
                rs.getString("NISS"),
                rs.getString("NIF"),
                rs.getString("NUS"),
                rs.getString("IBAN"),
                rs.getFloat("salario_hora"),
                rs.getFloat("salario_bruto"),
                rs.getFloat("salario_liquido"),
                rs.getString("numero_porta"),
                rs.getString("rua"),
                rs.getString("localidade"),
                rs.getString("codigo_postal"));
        f.setHoras_extra(rs.getInt("horas_extra"));
        return f;
    }

    @Override
    public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Funcionario")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Funcionario WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof Funcionario && containsKey(((Funcionario) value).getId());
    }

    @Override
    public Funcionario get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Funcionario WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return fromRS(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Funcionario put(Integer key, Funcionario value) {
        Funcionario prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Funcionario SET nome=?, telemovel=?, email=?, data_nascimento=?, NISS=?, NIF=?, NUS=?, IBAN=?, "
                                + "salario_hora=?, salario_bruto=?, salario_liquido=?, horas_extra=?, "
                                + "numero_porta=?, rua=?, localidade=?, codigo_postal=? WHERE id=?")) {
                    bindFields(pstm, value);
                    pstm.setInt(17, value.getId());
                    pstm.executeUpdate();
                }
            } else {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO Funcionario (id, nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, "
                                + "salario_hora, salario_bruto, salario_liquido, horas_extra, "
                                + "numero_porta, rua, localidade, codigo_postal) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    pstm.setInt(1, value.getId());
                    pstm.setString(2, value.getNome());
                    pstm.setString(3, value.getTelemovel());
                    pstm.setString(4, value.getEmail());
                    pstm.setString(5, value.getData_nascimento());
                    pstm.setString(6, value.getNISS());
                    pstm.setString(7, value.getNIF());
                    pstm.setString(8, value.getNUS());
                    pstm.setString(9, value.getIBAN());
                    pstm.setFloat(10, value.getSalario_hora());
                    pstm.setFloat(11, value.getSalario_bruto());
                    pstm.setFloat(12, value.getSalario_liquido());
                    pstm.setInt(13, value.getHoras_extra());
                    pstm.setString(14, value.getNumero_porta());
                    pstm.setString(15, value.getRua());
                    pstm.setString(16, value.getLocalidade());
                    pstm.setString(17, value.getCodigo_postal());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return prev;
    }

    private void bindFields(PreparedStatement pstm, Funcionario value) throws SQLException {
        pstm.setString(1, value.getNome());
        pstm.setString(2, value.getTelemovel());
        pstm.setString(3, value.getEmail());
        pstm.setString(4, value.getData_nascimento());
        pstm.setString(5, value.getNISS());
        pstm.setString(6, value.getNIF());
        pstm.setString(7, value.getNUS());
        pstm.setString(8, value.getIBAN());
        pstm.setFloat(9, value.getSalario_hora());
        pstm.setFloat(10, value.getSalario_bruto());
        pstm.setFloat(11, value.getSalario_liquido());
        pstm.setInt(12, value.getHoras_extra());
        pstm.setString(13, value.getNumero_porta());
        pstm.setString(14, value.getRua());
        pstm.setString(15, value.getLocalidade());
        pstm.setString(16, value.getCodigo_postal());
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Funcionario")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Funcionario remove(Object key) {
        Funcionario f = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Funcionario WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return f;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Funcionario> m) {
        for (Funcionario f : m.values())
            put(f.getId(), f);
    }

    @Override
    public void clear() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Funcionario");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Funcionario")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    @Override
    public Collection<Funcionario> values() {
        Collection<Funcionario> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Funcionario")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    @Override
    public Set<Map.Entry<Integer, Funcionario>> entrySet() {
        Set<Map.Entry<Integer, Funcionario>> res = new HashSet<>();
        for (Funcionario f : values()) {
            res.add(new AbstractMap.SimpleEntry<>(f.getId(), f));
        }
        return res;
    }
}
