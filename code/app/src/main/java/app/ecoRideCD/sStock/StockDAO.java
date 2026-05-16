package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private Stock buildFromRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        float preco = rs.getFloat("preco_compra");
        int codPeca = rs.getInt("codPeca");
        var dataChegadaRaw = rs.getDate("data_chegada");
        var dataChegada = dataChegadaRaw != null ? dataChegadaRaw.toLocalDate() : null;
        int qtd = rs.getInt("quantidade");
        EstadoStock estado = rs.getString("estado") != null ? EstadoStock.valueOf(rs.getString("estado")) : null;
        var dataGarantiaRaw = rs.getDate("garantia");
        var dataGarantia = dataGarantiaRaw != null ? dataGarantiaRaw.toLocalDate() : null;
        return new Stock(id, preco, codPeca, dataChegada, qtd, estado, dataGarantia);
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
                UPDATE Stock SET preco_compra=?, codPeca=?, data_chegada=?, quantidade=?, garantia=?, estado=?
                WHERE id=?
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setFloat(1, value.getPreco_compra());
            ps.setInt(2, value.getCodPeca());
            if (value.getData_chegada() != null)
                ps.setDate(3, Date.valueOf(value.getData_chegada()));
            else
                ps.setNull(3, Types.DATE);
            ps.setInt(4, value.getQuantidade());
            if (value.getGarantia() != null)
                ps.setDate(5, Date.valueOf(value.getGarantia()));
            else
                ps.setNull(5, Types.DATE);
            ps.setString(6, value.getEstado().name());
            ps.setInt(7, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EcoRideException("Erro a gravar stock " + key, e);
        }
        return prev;
    }

    public int insert(Stock value) {
        String sql = """
                INSERT INTO Stock (preco_compra, codPeca, data_chegada, quantidade, garantia, estado)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setFloat(1, value.getPreco_compra());
            ps.setInt(2, value.getCodPeca());
            if (value.getData_chegada() != null)
                ps.setDate(3, Date.valueOf(value.getData_chegada()));
            else
                ps.setNull(3, Types.DATE);
            ps.setInt(4, value.getQuantidade());
            if (value.getGarantia() != null)
                ps.setDate(5, Date.valueOf(value.getGarantia()));
            else
                ps.setNull(5, Types.DATE);
            ps.setString(6, value.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                throw new EcoRideException("Sem ID gerado para stock");
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a inserir stock", e);
        }
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

    public List<Stock> getByPecaId(int id_peca) {
        List<Stock> out = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE codPeca = ? ORDER BY data_chegada ASC, id ASC";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id_peca);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks da peça " + id_peca, e);
        }
        return out;
    }

    public List<Stock> getOperacionais() {
        List<Stock> out = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE estado IN (?, ?) ORDER BY data_chegada ASC, id ASC";
        try (Connection c = ConnectionFactory.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, EstadoStock.StockEmArmazem.name());
            ps.setString(2, EstadoStock.StockEncomendado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(buildFromRow(rs));
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter stocks operacionais", e);
        }
        return out;
    }
}
