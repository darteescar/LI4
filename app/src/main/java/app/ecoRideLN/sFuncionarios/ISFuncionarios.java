package app.ecoRideLN.sFuncionarios;

public interface ISFuncionarios {

     Funcionario obterDadosFuncionario(int id);

     boolean removerFuncionario(int id);

     boolean existeFuncionario(int id);

     Funcionario registarFuncionario(String nome, String telemovel, String email, String data_nascimento, String NISS, String NIF, String NUS, String IBAN, float salario_hora, float salario_liquido, float salario_bruto, int horas_extra, String numero_porta, String rua, String localidade, String codigo_postal);

     float adicionarHorasExtra(int id, int horas_extra);

     void atualizarNomeFuncionario(int id, String nome);

     void atualizarNumeroPortaFuncionario(int id, String numero_porta);

     void atualizarRuaFuncionario(int id, String rua);

     void atualizarLocalidadeFuncionario(int id, String localidade);

     void atualizarCodigoPostalFuncionario(int id, String codigo_postal);

     void atualizarTelemovelFuncionario(int id, String telemovel);

     void atualizarEmailFuncionario(int id, String email);

     void atualizarDataNascimentoFuncionario(int id, String data_nascimento);

     void atualizarNIFFuncionario(int id, String NIF);

     void atualizarNUSFuncionario(int id, String NUS);

     void atualizarIBANFuncionario(int id, String IBAN);

     void atualizarSalarioHoraFuncionario(int id, float salario_hora);

     void atualizarSalarioBrutoFuncionario(int id, float salario_bruto);

     void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido);

     void registarPagamentoFuncionario(int id);

     void atualizarNISSFuncionario(int id, String NISS);
}