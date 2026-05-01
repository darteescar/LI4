package app.ecoRideLN.sFuncionarios;

import java.time.LocalDate;

import app.common.EcoRideException;

public class SFuncionariosFacade implements ISFuncionarios {
     private final FuncionarioDAO funcionarioDAO;
     
     public SFuncionariosFacade() {
          this.funcionarioDAO = FuncionarioDAO.getInstance();
     }

     @Override
     public Funcionario obterDadosFuncionario(int id) {
          return funcionarioDAO.get(id);
     }

     @Override
     public boolean removerFuncionario(int id) {
          return funcionarioDAO.delete(id);
     }

     @Override
     public boolean existeFuncionario(int id) {
          return funcionarioDAO.exists(id);
     }

     @Override
     public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal){
          Funcionario funcionario = new Funcionario(funcionarioDAO.generateNewId(), nome, telemovel, email, data_nascimento, NISS, NIF, NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra, numero_porta, rua, localidade, codigo_postal);
          funcionarioDAO.add(funcionario);
          return funcionario;
     }

     @Override
     public float adicionarHorasExtra(int id, int horas_extra) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          int horas_totais = funcionario.getHoras_extra() + horas_extra;
          funcionario.setHoras_extra(horas_totais);
          funcionarioDAO.update(funcionario);
          float valor_a_pagar = horas_extra * funcionario.getSalario_hora() + funcionario.getSalario_liquido();
          return valor_a_pagar;
     }

     @Override
     void atualizarNomeFuncionario(int id, String nome) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setNome(nome);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarNumeroPortaFuncionario(int id, String numero_porta) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setNumero_porta(numero_porta);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarRuaFuncionario(int id, String rua) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setRua(rua);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarLocalidadeFuncionario(int id, String localidade) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setLocalidade(localidade);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarCodigoPostalFuncionario(int id, String codigo_postal) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setCodigo_postal(codigo_postal);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarTelemovelFuncionario(int id, String telemovel) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setTelemovel(telemovel);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarEmailFuncionario(int id, String email) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setEmail(email);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarDataNascimentoFuncionario(int id, String data_nascimento) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setData_nascimento(LocalDate.parse(data_nascimento));
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarNIFFuncionario(int id, String NIF) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setNIF(NIF);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarNUSFuncionario(int id, String NUS) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setNUS(NUS);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarIBANFuncionario(int id, String IBAN) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setIBAN(IBAN);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarSalarioHoraFuncionario(int id, float salario_hora) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setSalario_hora(salario_hora);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarSalarioBrutoFuncionario(int id, float salario_bruto) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setSalario_bruto(salario_bruto);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setSalario_liquido(salario_liquido);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void registarPagamentoFuncionario(int id) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setHoras_extra(0);
          funcionarioDAO.update(funcionario);
     }

     @Override
     void atualizarNISSFuncionario(int id, String NISS) {
          Funcionario funcionario = funcionarioDAO.get(id);
          if (funcionario == null) {
               throw new EcoRideException("Funcionário com ID " + id + " não existe.");
          }
          funcionario.setNISS(NISS);
          funcionarioDAO.update(funcionario);
     }
}
