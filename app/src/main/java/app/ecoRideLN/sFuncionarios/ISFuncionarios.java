package app.ecoRideLN.sFuncionarios;

import java.time.LocalDate;
import java.util.List;

public interface ISFuncionarios {

     public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     public Funcionario atualizarFuncionario(int id, String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     public Funcionario obterFuncionario(int id);

     public boolean existeFuncionario(int id);

     public boolean removerFuncionario(int id);

     public List<Funcionario> obterFuncionarios();

     // Utilitários

     public float calcularPagamentoFuncionario(int id);

     public void adicionarHorasExtra(int id, int horas_extra);

     public void registarPagamentoFuncionario(int id);
}
