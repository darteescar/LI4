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



public class EncomendaDAO implements Map<Integer, Encomenda> {

     private static EncomendaDAO instance;

     private EncomendaDAO() {}

     public static EncomendaDAO getInstance() {
          if (instance == null) instance = new EncomendaDAO();
          return instance;
     }

     private List<Integer> getCodPecasEncomenda(int idEncomenda) {
          List<Integer> codPecas = new ArrayList<>();
          String sql = "SELECT codPeca FROM PecasEncomenda WHERE idEncomenda = ?";
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, idEncomenda);
               try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) codPecas.add(rs.getInt(1));
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter peças da encomenda " + idEncomenda, e);
          }
          return codPecas;
     }

     private Encomenda buildFromRow(ResultSet rs) throws SQLException {
          return new Encomenda(
                    rs.getInt("id"),
                    rs.getInt("codFornecedor"),
                    rs.getTimestamp("data_criacao") != null ? rs.getTimestamp("data_criacao").toLocalDateTime() : null,
                    rs.getTimestamp("data_rececao") != null ? rs.getTimestamp("data_rececao").toLocalDateTime() : null,
                    rs.getTimestamp("data_envio") != null ? rs.getTimestamp("data_envio").toLocalDateTime() : null,
                    EstadoEncomenda.valueOf(rs.getString("estado")),
                    getCodPecasEncomenda(rs.getInt("id"))
          );

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
          Encomenda stored = get(e.getId());
          return stored != null;
     }

     @Override
     public Encomenda get(Object key) {
          if (!(key instanceof Integer id)) return null;
          String sql = "SELECT * FROM Encomenda WHERE id = ?";
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? buildFromRow(rs) : null;
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter encomenda " + id, e);
          }
     }

     @Override
     public Encomenda put(Integer key, Encomenda value) {
          Encomenda prev = get(key);
          String sql = """
                    INSERT INTO Encomenda (id, codFornecedor, data_criacao, data_rececao, data_envio, estado)
                    VALUES (?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                         codFornecedor = VALUES(codFornecedor), data_criacao = VALUES(data_criacao),
                         data_rececao = VALUES(data_rececao), data_envio = VALUES(data_envio), estado = VALUES(estado)
                    """;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, key);
               ps.setInt(2, value.getCodFornecedor());
               ps.setTimestamp(3, value.getData_criacao() != null ? Timestamp.valueOf(value.getData_criacao()) : null);
               ps.setTimestamp(4, value.getData_rececao() != null ? Timestamp.valueOf(value.getData_rececao()) : null);
               ps.setTimestamp(5, value.getData_envio() != null ? Timestamp.valueOf(value.getData_envio()) : null);
               ps.setString(6, value.getEstado().toString());
               ps.executeUpdate();
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
               while (rs.next()) out.add(buildFromRow(rs));
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
                    Encomenda encomenda = buildFromRow(rs);
                    out.add(new AbstractMap.SimpleEntry<>(encomenda.getId(), encomenda));
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
               ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Reparacao")) {
               return rs.next() ? rs.getInt(1) + 1 : 1;
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gerar novo ID para reparacao", e);
          }
     }

}
