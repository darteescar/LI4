package app.ecoRideCD.sFuncionarios;

import java.sql.Connection;
import java.sql.Date;
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
import app.ecoRideLN.sFuncionarios.Funcionario;

public class FuncionarioDAO implements Map<Integer, Funcionario> {
     private static FuncionarioDAO instance;

     private FuncionarioDAO() {}

     public static FuncionarioDAO getInstance() {
          if (instance == null) instance = new FuncionarioDAO();
          return instance;
     }

     private Funcionario buildFromRow(ResultSet rs) throws SQLException {
          Date dn = rs.getDate("data_nascimento");
          return new Funcionario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("telemovel"),
                    rs.getString("email"),
                    dn == null ? null : dn.toLocalDate(),
                    rs.getString("NISS"),
                    rs.getString("NIF"),
                    rs.getString("NUS"),
                    rs.getString("IBAN"),
                    rs.getFloat("salario_hora"),
                    rs.getFloat("salario_liquido"),
                    rs.getFloat("salario_bruto"),
                    rs.getInt("horas_extra"),
                    rs.getString("numero_porta"),
                    rs.getString("rua"),
                    rs.getString("localidade"),
                    rs.getString("codigo_postal"));
     }

     @Override
     public int size() {
               try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Funcionario")) {
               return rs.next() ? rs.getInt(1) : 0;
          } catch (SQLException e) {
               throw new EcoRideException("Erro a contar funcionarios", e);
          }
     }

     @Override public boolean isEmpty() { return size() == 0; }

     @Override
     public boolean containsKey(Object key){
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(
                       "SELECT 1 FROM Funcionario WHERE id = ?")) {
               ps.setInt(1, (Integer) key);
               try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a verificar existencia de funcionario", e);
          }
     }

     @Override
     public boolean containsValue(Object value){
          return containsKey(((Funcionario) value).getId());
     }

     @Override
     public Funcionario get(Object key) {
          if (!(key instanceof Integer id)) return null;
          String sql = "SELECT id, nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal FROM Funcionario WHERE id = ?";
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? buildFromRow(rs) : null;
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter funcionario " + id, e);
          }
     }

     @Override
     public Funcionario put(Integer key, Funcionario value) {
          Funcionario prev = get(key);
          String sql = """
                    INSERT INTO Funcionario (id, nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                         nome = VALUES(nome),
                         telemovel = VALUES(telemovel),
                         email = VALUES(email),
                         data_nascimento = VALUES(data_nascimento),
                         NISS = VALUES(NISS),
                         NIF = VALUES(NIF),
                         NUS = VALUES(NUS),
                         IBAN = VALUES(IBAN),
                         salario_hora = VALUES(salario_hora),
                         salario_liquido = VALUES(salario_liquido),
                         salario_bruto = VALUES(salario_bruto),
                         horas_extra = VALUES(horas_extra),
                         numero_porta = VALUES(numero_porta),
                         rua = VALUES(rua),
                         localidade = VALUES(localidade),
                         codigo_postal = VALUES(codigo_postal)
                    """;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setInt(1, key);
               ps.setString(2, value.getNome());
               ps.setString(3, value.getTelemovel());
               ps.setString(4, value.getEmail());
               ps.setDate(5, value.getData_nascimento() == null ? null : Date.valueOf(value.getData_nascimento()));
               ps.setString(6, value.getNISS());
               ps.setString(7, value.getNIF());
               ps.setString(8, value.getNUS());
               ps.setString(9, value.getIBAN());
               ps.setDouble(10, value.getSalario_hora());
               ps.setDouble(11, value.getSalario_liquido());
               ps.setDouble(12, value.getSalario_bruto());
               ps.setInt(13, value.getHoras_extra());
               ps.setString(14, value.getNumero_porta());
               ps.setString(15, value.getRua());
               ps.setString(16, value.getLocalidade());
               ps.setString(17, value.getCodigo_postal());
               ps.executeUpdate();
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gravar funcionario " + key, e);
          }
          return prev;
     }

     @Override
     public Funcionario remove(Object key) {
          if (!(key instanceof Integer id)) return null;
          Funcionario prev = get(id);
          if (prev == null) return null;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement("DELETE FROM Funcionario WHERE id = ?")) {
               ps.setInt(1, id);
               ps.executeUpdate();
          } catch (SQLException e) {
               throw new EcoRideException("Erro a remover funcionario " + id, e);
          }
          return prev;
     }

     @Override public void putAll(Map<? extends Integer, ? extends Funcionario> m) { m.forEach(this::put); }

     @Override
     public void clear() {
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement()) {
               s.executeUpdate("DELETE FROM Funcionario");
          } catch (SQLException e) {
               throw new EcoRideException("Erro a limpar funcionarios", e);
          }
     }

     @Override
     public Set<Integer> keySet() {
          Set<Integer> out = new LinkedHashSet<>();
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT id FROM Funcionario")) {
               while (rs.next()) out.add(rs.getInt(1));
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter ids de funcionarios", e);
          }
          return out;
     }

     @Override
     public Collection<Funcionario> values() {
          Set<Funcionario> out = new LinkedHashSet<>();
          String sql = "SELECT id, nome, email, data_nascimento, telemovel, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal FROM Funcionario";
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery(sql)) {
               while (rs.next()) out.add(buildFromRow(rs));
          } catch (SQLException e) {
               throw new EcoRideException("Erro a obter funcionarios", e);
          }
          return out;
     }

     @Override
     public Set<Entry<Integer, Funcionario>> entrySet() {
          Set<Entry<Integer, Funcionario>> out = new HashSet<>();
          for (Funcionario func : values()) out.add(new AbstractMap.SimpleEntry<>(func.getId(), func));
          return out;
     }

     // --------- Aliases / domínio ---------

     public void add(Funcionario func)          { put(func.getId(), func); }

     public int generateNewId() {
          try (Connection c = ConnectionFactory.get();
               Statement s = c.createStatement();
               ResultSet rs = s.executeQuery("SELECT COALESCE(MAX(id), 0) FROM Funcionario")) {
               return rs.next() ? rs.getInt(1) + 1 : 1;
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gerar novo ID para funcionario", e);
          }
     }     
}
