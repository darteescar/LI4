package app.ecoRideLN.sFuncionarios;

import java.util.Optional;

public interface ISFuncionarios {

    Funcionario registarFuncionario(String nome, String numero_porta, String rua, String localidade,
            String codigo_postal, String telemovel, String email,
            String data_nascimento, String niss, String nif, String nus,
            String iban, float salario_hora, float salario_bruto,
            float salario_liquido);

    Optional<Funcionario> obterDadosFuncionario(int id);

    void removerFuncionario(int id);

    boolean existeFuncionario(int id);

    void adicionarHorasExtra(int id, int horas_extra);

    void atualizarNomeFuncionario(int id, String nome);

    void atualizarNumeroPortaFuncionario(int id, String numero_porta);

    void atualizarRuaFuncionario(int id, String rua);

    void atualizarLocalidadeFuncionario(int id, String localidade);

    void atualizarCodigoPostalFuncionario(int id, String codigo_postal);

    void atualizarTelemovelFuncionario(int id, String telemovel);

    void atualizarEmailFuncionario(int id, String email);

    void atualizarDataNascimentoFuncionario(int id, String data_nascimento);

    void atualizarNIFFuncionario(int id, String nif);

    void atualizarNUSFuncionario(int id, String nus);

    void atualizarIBANFuncionario(int id, String iban);

    void atualizarSalarioHoraFuncionario(int id, float salario_hora);

    void atualizarSalarioBrutoFuncionario(int id, float salario_bruto);

    void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido);
}
