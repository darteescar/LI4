package app.ecoRideCD.sOrdens;

import app.ecoRideCD.DAOconfig;
import app.ecoRideLN.sOrdens.Fotografia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FotografiaDAO implements Map<Integer, Fotografia> {

    private static FotografiaDAO singleton = null;

    private FotografiaDAO() {}

    public static FotografiaDAO getInstance() {
        if (singleton == null) singleton = new FotografiaDAO();
        return singleton;
    }

    public Optional<Fotografia> obterPorId(int id) { return Optional.ofNullable(this.get(id)); }

    public List<Fotografia> obterPorOS(int idOS) {
        List<Fotografia> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Fotografia WHERE idOS_FK=?")) {
            pstm.setInt(1, idOS);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) res.add(fromRS(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    private Fotografia fromRS(ResultSet rs) throws SQLException {
        return new Fotografia(rs.getInt("id"), rs.getBytes("conteudo"),
                rs.getString("formato"), rs.getLong("tamanho"));
    }

    public int generateNewId() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COALESCE(MAX(id),0) FROM Fotografia")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public Fotografia putWithOS(Fotografia value, int idOS) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO Fotografia (id, idOS_FK, conteudo, formato, tamanho) VALUES (?,?,?,?,?)")) {
            pstm.setInt(1, value.getId());
            pstm.setInt(2, idOS);
            pstm.setBytes(3, value.getConteudo());
            pstm.setString(4, value.getFormato());
            pstm.setLong(5, value.getTamanho());
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return value;
    }

    @Override public int size() {
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Fotografia")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean isEmpty() { return size() == 0; }

    @Override public boolean containsKey(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM Fotografia WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public boolean containsValue(Object value) {
        return value instanceof Fotografia && containsKey(((Fotografia) value).getId());
    }

    @Override public Fotografia get(Object key) {
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM Fotografia WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return fromRS(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return null;
    }

    @Override public Fotografia put(Integer key, Fotografia value) {
        // Use putWithOS for new fotografias; this method only updates content/formato/tamanho.
        Fotografia prev = this.get(key);
        try (Connection conn = DAOconfig.getConnection()) {
            if (prev != null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Fotografia SET conteudo=?, formato=?, tamanho=? WHERE id=?")) {
                    pstm.setBytes(1, value.getConteudo());
                    pstm.setString(2, value.getFormato());
                    pstm.setLong(3, value.getTamanho());
                    pstm.setInt(4, value.getId());
                    pstm.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return prev;
    }

    @Override public Fotografia remove(Object key) {
        Fotografia f = this.get(key);
        try (Connection conn = DAOconfig.getConnection();
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM Fotografia WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            pstm.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return f;
    }

    @Override public void putAll(Map<? extends Integer, ? extends Fotografia> m) {
        for (Fotografia f : m.values()) put(f.getId(), f);
    }

    @Override public void clear() {
        try (Connection conn = DAOconfig.getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Fotografia");
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM Fotografia")) {
            while (rs.next()) res.add(rs.getInt("id"));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Collection<Fotografia> values() {
        Collection<Fotografia> res = new ArrayList<>();
        try (Connection conn = DAOconfig.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Fotografia")) {
            while (rs.next()) res.add(fromRS(rs));
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        return res;
    }

    @Override public Set<Map.Entry<Integer, Fotografia>> entrySet() {
        Set<Map.Entry<Integer, Fotografia>> res = new HashSet<>();
        for (Fotografia f : values()) res.add(new AbstractMap.SimpleEntry<>(f.getId(), f));
        return res;
    }
}
