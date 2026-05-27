package app.ecoRideCD.sFuncionarios;

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
import app.ecoRideCD.DAOconfig.CifraUtil;
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
          String dnRaw = rs.getString("data_nascimento");
          java.time.LocalDate dn = dnRaw == null ? null
              : java.time.LocalDate.parse(CifraUtil.decifrar(dnRaw));
          return new Funcionario(
                    rs.getInt("id"),
                    CifraUtil.decifrar(rs.getString("nome")),
                    CifraUtil.decifrar(rs.getString("telemovel")),
                    CifraUtil.decifrar(rs.getString("email")),
                    dn,
                    CifraUtil.decifrar(rs.getString("NISS")),
                    CifraUtil.decifrar(rs.getString("NIF")),
                    CifraUtil.decifrar(rs.getString("NUS")),
                    CifraUtil.decifrar(rs.getString("IBAN")),
                    (float) CifraUtil.decifrarFloat(rs.getString("salario_hora")),
                    (float) CifraUtil.decifrarFloat(rs.getString("salario_liquido")),
                    (float) CifraUtil.decifrarFloat(rs.getString("salario_bruto")),
                    rs.getInt("horas_extra"),
                    CifraUtil.decifrar(rs.getString("numero_porta")),
                    CifraUtil.decifrar(rs.getString("rua")),
                    CifraUtil.decifrar(rs.getString("localidade")),
                    CifraUtil.decifrar(rs.getString("codigo_postal")));
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
                    UPDATE Funcionario SET nome=?, telemovel=?, email=?, data_nascimento=?,
                         NISS=?, NIF=?, NUS=?, IBAN=?, salario_hora=?, salario_liquido=?,
                         salario_bruto=?, horas_extra=?, numero_porta=?, rua=?, localidade=?,
                         codigo_postal=? WHERE id=?
                    """;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql)) {
               ps.setString(1,  CifraUtil.cifrar(value.getNome()));
               ps.setString(2,  CifraUtil.cifrar(value.getTelemovel()));
               ps.setString(3,  CifraUtil.cifrar(value.getEmail()));
               ps.setString(4,  value.getData_nascimento() == null ? null
                                    : CifraUtil.cifrar(value.getData_nascimento().toString()));
               ps.setString(5,  CifraUtil.cifrar(value.getNISS()));
               ps.setString(6,  CifraUtil.cifrar(value.getNIF()));
               ps.setString(7,  CifraUtil.cifrar(value.getNUS()));
               ps.setString(8,  CifraUtil.cifrar(value.getIBAN()));
               ps.setString(9,  CifraUtil.cifrarFloat(value.getSalario_hora()));
               ps.setString(10, CifraUtil.cifrarFloat(value.getSalario_liquido()));
               ps.setString(11, CifraUtil.cifrarFloat(value.getSalario_bruto()));
               ps.setInt(12,    value.getHoras_extra());
               ps.setString(13, CifraUtil.cifrar(value.getNumero_porta()));
               ps.setString(14, CifraUtil.cifrar(value.getRua()));
               ps.setString(15, CifraUtil.cifrar(value.getLocalidade()));
               ps.setString(16, CifraUtil.cifrar(value.getCodigo_postal()));
               ps.setInt(17,    key);
               ps.executeUpdate();
          } catch (SQLException e) {
               throw new EcoRideException("Erro a gravar funcionario " + key, e);
          }
          return prev;
     }

     public int insert(Funcionario value) {
          String sql = """
                    INSERT INTO Funcionario (nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN,
                         salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
          try (Connection c = ConnectionFactory.get();
               PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
               ps.setString(1,  CifraUtil.cifrar(value.getNome()));
               ps.setString(2,  CifraUtil.cifrar(value.getTelemovel()));
               ps.setString(3,  CifraUtil.cifrar(value.getEmail()));
               ps.setString(4,  value.getData_nascimento() == null ? null
                                    : CifraUtil.cifrar(value.getData_nascimento().toString()));
               ps.setString(5,  CifraUtil.cifrar(value.getNISS()));
               ps.setString(6,  CifraUtil.cifrar(value.getNIF()));
               ps.setString(7,  CifraUtil.cifrar(value.getNUS()));
               ps.setString(8,  CifraUtil.cifrar(value.getIBAN()));
               ps.setString(9,  CifraUtil.cifrarFloat(value.getSalario_hora()));
               ps.setString(10, CifraUtil.cifrarFloat(value.getSalario_liquido()));
               ps.setString(11, CifraUtil.cifrarFloat(value.getSalario_bruto()));
               ps.setInt(12,    value.getHoras_extra());
               ps.setString(13, CifraUtil.cifrar(value.getNumero_porta()));
               ps.setString(14, CifraUtil.cifrar(value.getRua()));
               ps.setString(15, CifraUtil.cifrar(value.getLocalidade()));
               ps.setString(16, CifraUtil.cifrar(value.getCodigo_postal()));
               ps.executeUpdate();
               try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) { int id = rs.getInt(1); value.setId(id); return id; }
                    throw new EcoRideException("Sem ID gerado para funcionario");
               }
          } catch (SQLException e) {
               throw new EcoRideException("Erro a inserir funcionario", e);
          }
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
}
