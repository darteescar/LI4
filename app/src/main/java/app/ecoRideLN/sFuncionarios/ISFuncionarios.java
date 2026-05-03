package app.ecoRideLN.sFuncionarios;

import java.time.LocalDate;

public interface ISFuncionarios {

     public Funcionario obterDadosFuncionario(int id);

     public boolean removerFuncionario(int id);

     public boolean existeFuncionario(int id);

     public Funcionario registarFuncionario(String nome, String telemovel, String email, LocalDate data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     public float adicionarHorasExtra(int id, int horas_extra);

     public void atualizarNomeFuncionario(int id, String nome);

     public void atualizarNumeroPortaFuncionario(int id, String numero_porta);

     public void atualizarRuaFuncionario(int id, String rua);

     public void atualizarLocalidadeFuncionario(int id, String localidade);

     public void atualizarCodigoPostalFuncionario(int id, String codigo_postal);

     public void atualizarTelemovelFuncionario(int id, String telemovel);

     public void atualizarEmailFuncionario(int id, String email);

     public void atualizarDataNascimentoFuncionario(int id, LocalDate data_nascimento);

     public void atualizarNIFFuncionario(int id, String NIF);

     public void atualizarNUSFuncionario(int id, String NUS);

     public void atualizarIBANFuncionario(int id, String IBAN);

     public void atualizarSalarioHoraFuncionario(int id, float salario_hora);

     public void atualizarSalarioBrutoFuncionario(int id, float salario_bruto);

     public void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido);

     public void registarPagamentoFuncionario(int id);

     public void atualizarNISSFuncionario(int id, String NISS);
}