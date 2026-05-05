package app.ecoRideLN.sFuncionarios;

import java.time.LocalDate;
import java.util.List;

public interface ISFuncionarios {

     Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     Funcionario obterDadosFuncionario(int id);

     List<Funcionario> obterTodosFuncionarios();

     boolean existeFuncionario(int id);

     boolean removerFuncionario(int id);

     void atualizarDadosFuncionario(int id, String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     float adicionarHorasExtra(int id, int horas_extra);

     void registarPagamentoFuncionario(int id);
}
