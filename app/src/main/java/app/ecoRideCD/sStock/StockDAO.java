package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
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
import app.ecoRideLN.sStock.EstadoStock;
import app.ecoRideLN.sStock.Stock;
import app.ecoRideLN.sStock.StockComGarantia;

public class StockDAO implements Map<Integer, Stock> {

    private static StockDAO instance;

    private StockDAO() {
    }

    public static StockDAO getInstance() {
        if (instance == null) {
            instance = new StockDAO();
        }
        return instance;
    }

    // Single-table inheritance: se nr_serie != null, instancia StockComGarantia.
    private Stock buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        float preco = rs.getFloat("preco_compra");
        int codPeca = rs.getInt("codPeca");
        var dataChegada = rs.getTimestamp("data_chegada").toLocalDateTime();
        int qtd = rs.getInt("quantidade");
        String nrSerie = rs.getString("nr_serie");
        Date garantia = rs.getDate("garantia");
        EstadoStock estado = rs.getString("estado") != null ? EstadoStock.valueOf(rs.getString("estado")) : null;

        if (nrSerie != null) {
            return new StockComGarantia(id, preco, codPeca, dataChegada, nrSerie, garantia == null ? null : garantia.toLocalDate());
        }
        return new Stock(id, preco, codPeca, dataChegada, qtd, estado);
    }

    @Override
    public int size() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Stock")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a contar stocks", e);
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
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Stock WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a verificar stock " + id, e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Stock s)) {
            return false;
        }
        Stock stored = get(s.getId());
        return stored != null && stored.getCodPeca() == s.getCodPeca();
    }

    @Override
    public Stock get(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        String sql = """
                SELECT * FROM Stock WHERE id = ?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stock " + id, e);
        }
    }

    @Override
    public Stock put(Integer key, Stock value) {
        Stock prev = get(key);
        String sql = """
                INSERT INTO Stock (id, preco_compra, codPeca, data_chegada, quantidade, nr_serie, garantia, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    preco_compra = VALUES(preco_compra), codPeca = VALUES(codPeca),
                    data_chegada = VALUES(data_chegada), quantidade = VALUES(quantidade),
                    nr_serie = VALUES(nr_serie), garantia = VALUES(garantia), estado = VALUES(estado)
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, key);
            ps.setFloat(2, value.getPreco_compra());
            ps.setInt(3, value.getCodPeca());
            ps.setTimestamp(4, Timestamp.valueOf(value.getData_chegada()));
            ps.setInt(5, value.getQuantidade());
            if (value instanceof StockComGarantia g) {
                ps.setString(6, g.getNr_serie());
                if (g.getGarantia() == null) {
                    ps.setNull(7, Types.DATE);
                } else {
                    ps.setDate(7, Date.valueOf(g.getGarantia()));
                }
                ps.setString(8, g.getEstado().name());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.DATE);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar stock " + key, e);
        }
        return prev;
    }

    @Override
    public Stock remove(Object key) {
        if (!(key instanceof Integer id)) {
            return null;
        }
        Stock prev = get(id);
        if (prev == null) {
            return null;
        }
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM Stock WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a remover stock " + id, e);
        }
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM Stock");
        } catch (SQLException e) {
            throw new EcoRideException("Erro a limpar stocks", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> out = new LinkedHashSet<>();
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id FROM Stock")) {
            while (rs.next()) {
                out.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter ids de stocks", e);
        }
        return out;
    }

    @Override
    public Collection<Stock> values() {
        Set<Stock> out = new LinkedHashSet<>();
        String sql = """
                SELECT * FROM Stock
                """;
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks", e);
        }
        return out;
    }

    @Override
    public Set<Entry<Integer, Stock>> entrySet() {
        Set<Entry<Integer, Stock>> out = new HashSet<>();
        for (Stock st : values()) {
            out.add(new AbstractMap.SimpleEntry<>(st.getId(), st));
        }
        return out;
    }

    // --------- Aliases / domínio ---------

    public int generateNewId() {
        try (Connection c = ConnectionFactory.get(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Stock")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gerar novo ID para stock", e);
        }
    }

    public List<Stock> getByPecaId(int id_peca) {
        List<Stock> out = new ArrayList<>();
        String sql = """
                SELECT * FROM Stock WHERE codPeca = ?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_peca);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(buildFromRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks da peça " + id_peca, e);
        }
        return out;
    }
}
