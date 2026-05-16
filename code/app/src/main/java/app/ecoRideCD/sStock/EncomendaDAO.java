package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
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
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.EstadoEncomenda;

public class EncomendaDAO implements Map<Integer, Encomenda> {

     private static EncomendaDAO instance;

     private EncomendaDAO() {}

     public static EncomendaDAO getInstance() {
          if (instance == null) instance = new EncomendaDAO();
          return instance;
     }

     private List<Integer> loadStocks(Connection c, int idEncomenda) throws SQLException {
          List<Integer> out = new ArrayList<>();
          String sql = "SELECT codStock FROM Encomenda_EntradaStock WHERE idEncomenda = ? ORDER BY ordem";
          try (PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, idEncomenda);
               try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) out.add(rs.getInt(1));
               }
          }
          return out;
     }

     private Encomenda buildFromRow(Connection c, ResultSet rs) throws SQLException {
          int id = rs.getInt("id");
          return new Encomenda(
                    id,
                    rs.getInt("codFornecedor"),
                    rs.getDate("data_criacao") != null ? rs.getDate("data_criacao").toLocalDate() : null,
                    rs.getDate("data_rececao") != null ? rs.getDate("data_rececao").toLocalDate() : null,
                    rs.getDate("data_envio")   != null ? rs.getDate("data_envio").toLocalDate()   : null,
                    EstadoEncomenda.valueOf(rs.getString("estado")),
                    loadStocks(c, id)
          );
     }

     private void updateBase(Connection c, int id, Encomenda value) throws SQLException {
          String sql = """
                    UPDATE Encomenda SET codFornecedor=?, data_criacao=?, data_rececao=?, data_envio=?, estado=?
                    WHERE id=?
                    """;
          try (PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, value.getCodFornecedor());
               ps.setDate(2, value.getData_criacao() != null ? Date.valueOf(value.getData_criacao()) : null);
               ps.setDate(3, value.getData_rececao() != null ? Date.valueOf(value.getData_rececao()) : null);
               ps.setDate(4, value.getData_envio()   != null ? Date.valueOf(value.getData_envio())   : null);
               ps.setString(5, value.getEstado().name());
               ps.setInt(6, id);
               ps.executeUpdate();
          }
     }

     private void clearStocks(Connection c, int idEncomenda) throws SQLException {
          try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM Encomenda_EntradaStock WHERE idEncomenda = ?")) {
               ps.setInt(1, idEncomenda);
               ps.executeUpdate();
          }
     }

     private void insertStocks(Connection c, int idEncomenda, List<Integer> codStocks) throws SQLException {
          if (codStocks == null || codStocks.isEmpty()) return;
          try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Encomenda_EntradaStock (idEncomenda, ordem, codStock) VALUES (?, ?, ?)")) {
               for (int i = 0; i < codStocks.size(); i++) {
                    ps.setInt(1, idEncomenda);
                    ps.setInt(2, i);
                    ps.setInt(3, codStocks.get(i));
                    ps.addBatch();
               }
               ps.executeBatch();
          }
     }

     @Override
     public int size() {
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Encomenda")) {
               return rs.next() ? rs.getInt(1) : 0;
          } catch (SQLException e) {
               throw new EcoRideException("Erro a contar encomendas", e);
          }
     }

     @Override public boolean isEmpty() { return size() == 0; }

     @Override
     public boolean containsKey(Object key) {
          if (!(key instanceof Integer id)) return false;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement("SELECT 1 FROM Encomenda WHERE id = ?")) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a verificar encomenda " + id, e);
          }
     }

     @Override
     public boolean containsValue(Object value) {
          if (!(value instanceof Encomenda e)) return false;
          return get(e.getId()) != null;
     }

     @Override
     public Encomenda get(Object key) {
          if (!(key instanceof Integer id)) return null;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement("SELECT * FROM Encomenda WHERE id = ?")) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? buildFromRow(c, rs) : null;
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomenda " + id, e);
          }
     }

     @Override
     public Encomenda put(Integer key, Encomenda value) {
          Encomenda prev = get(key);
          try (Connection c = ConnectionFactory.get()) {
               c.setAutoCommit(false);
               try {
                    updateBase(c, key, value);
                    clearStocks(c, key);
                    insertStocks(c, key, value.getCodStocks());
                    c.commit();
               } catch (SQLException e) {
                    c.rollback();
                    throw e;
               } finally {
                    c.setAutoCommit(true);
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gravar encomenda " + key, e);
          }
          return prev;
     }

     public int insert(Encomenda value) {
          String sql = """
                    INSERT INTO Encomenda (codFornecedor, data_criacao, data_rececao, data_envio, estado)
                    VALUES (?, ?, ?, ?, ?)
                    """;
          try (Connection c = ConnectionFactory.get()) {
               c.setAutoCommit(false);
               try {
                    int id;
                    try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                         ps.setInt(1, value.getCodFornecedor());
                         ps.setDate(2, value.getData_criacao() != null ? Date.valueOf(value.getData_criacao()) : null);
                         ps.setDate(3, value.getData_rececao() != null ? Date.valueOf(value.getData_rececao()) : null);
                         ps.setDate(4, value.getData_envio()   != null ? Date.valueOf(value.getData_envio())   : null);
                         ps.setString(5, value.getEstado().name());
                         ps.executeUpdate();
                         try (ResultSet rs = ps.getGeneratedKeys()) {
                              if (rs.next()) { id = rs.getInt(1); value.setId(id); }
                              else throw new EcoRideException("Sem ID gerado para encomenda");
                         }
                    }
                    insertStocks(c, id, value.getCodStocks());
                    c.commit();
                    return id;
               } catch (SQLException e) {
                    c.rollback();
                    throw new EcoRideException("Erro a inserir encomenda", e);
               } finally {
                    c.setAutoCommit(true);
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro de ligação ao inserir encomenda", e);
          }
     }

     @Override
     public Encomenda remove(Object key) {
          if (!(key instanceof Integer id)) return null;
          Encomenda prev = get(id);
          if (prev == null) return null;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement("DELETE FROM Encomenda WHERE id = ?")) {
               ps.setInt(1, id);
               ps.executeUpdate();
          } catch (SQLException e) {
               throw new EcoRideException("Erro a remover encomenda " + id, e);
          }
          return prev;
     }

     @Override public void putAll(Map<? extends Integer, ? extends Encomenda> m) { m.forEach(this::put); }

     @Override
     public void clear() {
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement()) {
               s.executeUpdate("DELETE FROM Encomenda");
          } catch (SQLException e) {
               throw new EcoRideException("Erro a limpar encomendas", e);
          }
     }

     @Override
     public Set<Integer> keySet() {
          Set<Integer> out = new LinkedHashSet<>();
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT id FROM Encomenda")) {
               while (rs.next()) out.add(rs.getInt(1));
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter ids de encomendas", e);
          }
          return out;
     }

     @Override
     public Collection<Encomenda> values() {
          Set<Encomenda> out = new LinkedHashSet<>();
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT * FROM Encomenda")) {
               while (rs.next()) out.add(buildFromRow(c, rs));
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomendas", e);
          }
          return out;
     }

     @Override
     public Set<Entry<Integer, Encomenda>> entrySet() {
          Set<Entry<Integer, Encomenda>> out = new HashSet<>();
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT * FROM Encomenda")) {
               while (rs.next()) {
                    Encomenda e = buildFromRow(c, rs);
                    out.add(new AbstractMap.SimpleEntry<>(e.getId(), e));
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomendas", e);
          }
          return out;
     }

     public List<Encomenda> getByEstado(EstadoEncomenda estado) {
          List<Encomenda> out = new ArrayList<>();
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement("SELECT * FROM Encomenda WHERE estado = ?")) {
               ps.setString(1, estado.name());
               try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) out.add(buildFromRow(c, rs));
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomendas por estado", e);
          }
          return out;
     }
}
