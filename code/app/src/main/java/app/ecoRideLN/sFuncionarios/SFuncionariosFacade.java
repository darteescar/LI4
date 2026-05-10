package app.ecoRideLN.sFuncionarios;

import java.time.LocalDate;
import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sFuncionarios.FuncionarioDAO;

public class SFuncionariosFacade implements ISFuncionarios {
     private final FuncionarioDAO funcionarioDAO;
     
     public SFuncionariosFacade() {
          this.funcionarioDAO = FuncionarioDAO.getInstance();
     }

     @Override
     public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal){
          int id = funcionarioDAO.generateNewId();
          Funcionario funcionario = new Funcionario(id, nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal);
          funcionarioDAO.put(id, funcionario);
          return funcionario;
     }

     @Override
     public Funcionario atualizarFuncionario(int id, String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal){
          Funcionario func = funcionarioDAO.get(id);
          if (func != null) {
               if (nome != null) func.setNome(nome);
               if (telemovel != null) func.setTelemovel(telemovel);
               if (email != null) func.setEmail(email);
               if (data_nascimento != null) func.setData_nascimento(data_nascimento);
               if (NISS != null) func.setNISS(NISS);
               if (NIF != null) func.setNIF(NIF);
               if (NUS != null) func.setNUS(NUS);
               if (IBAN != null) func.setIBAN(IBAN);
               if (salario_hora >= 0) func.setSalario_hora(salario_hora);
               if (salario_liquido >= 0) func.setSalario_liquido(salario_liquido);
               if (salario_bruto >= 0) func.setSalario_bruto(salario_bruto);
               if (horas_extra >= 0) func.setHoras_extra(horas_extra);
               if (numero_porta != null) func.setNumero_porta(numero_porta);
               if (rua != null) func.setRua(rua);
               if (localidade != null) func.setLocalidade(localidade);
               if (codigo_postal != null) func.setCodigo_postal(codigo_postal);
               funcionarioDAO.put(id, func);
               return func;
          } else {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
     }

     @Override
     public Funcionario obterFuncionario(int id) {
          return funcionarioDAO.get(id);
     }

     @Override
     public boolean existeFuncionario(int id) {
          return funcionarioDAO.containsKey(id);
     }

     @Override
     public boolean removerFuncionario(int id) {
          return funcionarioDAO.remove(id) != null;
     }

     @Override
     public List<Funcionario> obterFuncionarios(){
          return funcionarioDAO.values().stream().toList();
     }

     // Utilitários

     @Override
     public float calcularPagamentoFuncionario(int id) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          return funcionario.getSalario_liquido() + (funcionario.getHoras_extra() * funcionario.getSalario_hora());
     }

     @Override
     public void adicionarHorasExtra(int id, int horas_extra) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          int horas_totais = funcionario.getHoras_extra() + horas_extra;
          funcionario.setHoras_extra(horas_totais);
          funcionarioDAO.put(id, funcionario);
     }

     @Override
     public float registarPagamentoFuncionario(int id) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setHoras_extra(0);
          funcionarioDAO.put(id, funcionario);
          return calcularPagamentoFuncionario(id);
     }
}
