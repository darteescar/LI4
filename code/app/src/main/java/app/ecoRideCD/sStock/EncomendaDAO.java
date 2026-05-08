package app.ecoRideCD.sStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
import app.ecoRideLN.sStock.ItemEncomenda;


public class EncomendaDAO implements Map<Integer, Encomenda> {

     private static EncomendaDAO instance;

     private EncomendaDAO() {}

     public static EncomendaDAO getInstance() {
          if (instance == null) instance = new EncomendaDAO();
          return instance;
     }

     private List<ItemEncomenda> loadItens(Connection c, int idEncomenda) throws SQLException {
          List<ItemEncomenda> out = new ArrayList<>();
          String sql = "SELECT codPeca, quantidade, preco_unitario FROM Encomenda_Item WHERE idEncomenda = ? ORDER BY ordem";
          try (PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, idEncomenda);
               try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                         out.add(new ItemEncomenda(rs.getInt("codPeca"), rs.getInt("quantidade"), rs.getFloat("preco_unitario")));
               }
          }
          return out;
     }

     private List<Integer> loadEntradasStock(Connection c, int idEncomenda) throws SQLException {
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
                    rs.getTimestamp("data_criacao") != null ? rs.getTimestamp("data_criacao").toLocalDateTime().toLocalDate() : null,
                    rs.getTimestamp("data_rececao") != null ? rs.getTimestamp("data_rececao").toLocalDateTime().toLocalDate() : null,
                    rs.getTimestamp("data_envio") != null ? rs.getTimestamp("data_envio").toLocalDateTime().toLocalDate() : null,
                    EstadoEncomenda.valueOf(rs.getString("estado")),
                    loadItens(c, id),
                    loadEntradasStock(c, id)
          );
     }

     private void upsertBase(Connection c, int id, Encomenda value) throws SQLException {
          String sql = """
                    INSERT INTO Encomenda (id, codFornecedor, data_criacao, data_rececao, data_envio, estado)
                    VALUES (?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                         codFornecedor = VALUES(codFornecedor), data_criacao = VALUES(data_criacao),
                         data_rececao = VALUES(data_rececao), data_envio = VALUES(data_envio), estado = VALUES(estado)
                    """;
          try (PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, id);
               ps.setInt(2, value.getCodFornecedor());
               ps.setTimestamp(3, value.getData_criacao() != null ? Timestamp.valueOf(value.getData_criacao().atStartOfDay()) : null);
               ps.setTimestamp(4, value.getData_rececao() != null ? Timestamp.valueOf(value.getData_rececao().atStartOfDay()) : null);
               ps.setTimestamp(5, value.getData_envio() != null ? Timestamp.valueOf(value.getData_envio().atStartOfDay()) : null);
               ps.setString(6, value.getEstado().name());
               ps.executeUpdate();
          }
     }

     private void clearItens(Connection c, int idEncomenda) throws SQLException {
          try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM Encomenda_Item WHERE idEncomenda = ?")) {
               ps.setInt(1, idEncomenda);
               ps.executeUpdate();
          }
     }

     private void insertItens(Connection c, int idEncomenda, List<ItemEncomenda> itens) throws SQLException {
          if (itens == null || itens.isEmpty()) return;
          try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Encomenda_Item (idEncomenda, ordem, codPeca, quantidade, preco_unitario) VALUES (?, ?, ?, ?, ?)")) {
               for (int i = 0; i < itens.size(); i++) {
                    ItemEncomenda item = itens.get(i);
                    ps.setInt(1, idEncomenda);
                    ps.setInt(2, i);
                    ps.setInt(3, item.getCodPeca());
                    ps.setInt(4, item.getQuantidade());
                    ps.setFloat(5, item.getPreco_unitario());
                    ps.addBatch();
               }
               ps.executeBatch();
          }
     }

     private void clearEntradas(Connection c, int idEncomenda) throws SQLException {
          try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM Encomenda_EntradaStock WHERE idEncomenda = ?")) {
               ps.setInt(1, idEncomenda);
               ps.executeUpdate();
          }
     }

     private void insertEntradas(Connection c, int idEncomenda, List<Integer> codEntradasStock) throws SQLException {
          if (codEntradasStock == null || codEntradasStock.isEmpty()) return;
          try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO Encomenda_EntradaStock (idEncomenda, ordem, codStock) VALUES (?, ?, ?)")) {
               for (int i = 0; i < codEntradasStock.size(); i++) {
                    ps.setInt(1, idEncomenda);
                    ps.setInt(2, i);
                    ps.setInt(3, codEntradasStock.get(i));
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
          String sql = "SELECT * FROM Encomenda WHERE id = ?";
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
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
                    upsertBase(c, key, value);
                    clearItens(c, key);
                    insertItens(c, key, value.getItensEncomendados());
                    clearEntradas(c, key);
                    insertEntradas(c, key, value.getCodEntradasStock());
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
          String sql = "SELECT * FROM Encomenda";
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery(sql)) {
               while (rs.next()) out.add(buildFromRow(c, rs));
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomendas", e);
          }
          return out;
     }

     @Override
     public Set<Entry<Integer, Encomenda>> entrySet() {
          Set<Entry<Integer, Encomenda>> out = new HashSet<>();
          String sql = "SELECT * FROM Encomenda";
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery(sql)) {
               while (rs.next()) {
                    Encomenda e = buildFromRow(c, rs);
                    out.add(new AbstractMap.SimpleEntry<>(e.getId(), e));
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomendas", e);
          }
          return out;
     }

     // --------- Aliases / domínio ---------

     public int generateNewId() {
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Encomenda")) {
               return rs.next() ? rs.getInt(1) + 1 : 1;
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gerar novo ID para encomenda", e);
          }
     }

     public List<Encomenda> getByEstado(EstadoEncomenda estado) {
          List<Encomenda> out = new ArrayList<>();
          String sql = "SELECT * FROM Encomenda WHERE estado = ?";
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
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
