package app.ecoRideLN.sFuncionarios;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sFuncionarios.FuncionarioDAO;
import app.ecoRideLN.sNotificacoes.ISNotificacoes;
import java.util.Optional;

public class SFuncionariosFacade implements ISFuncionarios {

    private final FuncionarioDAO funcionariosDAO = new FuncionarioDAO();
    private int proximoId = 1;

    private final ISNotificacoes sNotificacoes;

    public SFuncionariosFacade(ISNotificacoes sNotificacoes) {
        this.sNotificacoes = sNotificacoes;
    }

    @Override
    public Funcionario registarFuncionario(String nome, String numero_porta, String rua, String localidade,
            String codigo_postal, String telemovel, String email,
            String data_nascimento, String niss, String nif, String nus,
            String iban, float salario_hora, float salario_bruto,
            float salario_liquido) {
        Validacoes.naoVazio(nome, "Nome");
        Validacoes.naoVazio(numero_porta, "Número de porta");
        Validacoes.naoVazio(rua, "Rua");
        Validacoes.naoVazio(localidade, "Localidade");
        Validacoes.codigoPostal(codigo_postal);
        Validacoes.telemovel(telemovel);
        Validacoes.emailValido(email);
        Validacoes.naoVazio(data_nascimento, "Data de nascimento");
        Validacoes.niss(niss);
        Validacoes.nif(nif);
        Validacoes.naoVazio(nus, "NUS");
        Validacoes.iban(iban);
        Validacoes.valorMonetario(salario_hora, "Salário/hora");
        Validacoes.valorMonetario(salario_bruto, "Salário bruto");
        Validacoes.valorMonetario(salario_liquido, "Salário líquido");

        if (funcionariosDAO.obterPorNif(nif).isPresent()) {
            throw new EcoRideException("Já existe um funcionário com este NIF.");
        }

        Morada morada = new Morada(numero_porta, rua, localidade, codigo_postal);
        Funcionario f = new Funcionario(proximoId++, nome, telemovel, email, data_nascimento,
                niss, nif, nus, iban, salario_hora, salario_bruto, salario_liquido, morada);
        funcionariosDAO.put(f.getId(), f);
        return f;
    }

    @Override
    public Optional<Funcionario> obterDadosFuncionario(int id) {
        return funcionariosDAO.obterPorId(id);
    }

    @Override
    public void removerFuncionario(int id) {
        if (!existeFuncionario(id)) {
            throw new EcoRideException("Funcionário não encontrado.");
        }
        funcionariosDAO.remove(id);
    }

    @Override
    public boolean existeFuncionario(int id) {
        return funcionariosDAO.containsKey(id);
    }

    private Funcionario obterOuFalhar(int id) {
        return funcionariosDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Funcionário não encontrado."));
    }

    @Override
    public void adicionarHorasExtra(int id, int horas_extra) {
        Validacoes.inteiroPositivo(horas_extra, "Horas extra");
        Funcionario f = obterOuFalhar(id);
        f.setHoras_extra(f.getHoras_extra() + horas_extra);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarNomeFuncionario(int id, String nome) {
        Validacoes.naoVazio(nome, "Nome");
        Funcionario f = obterOuFalhar(id);
        f.setNome(nome);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarNumeroPortaFuncionario(int id, String numero_porta) {
        Validacoes.naoVazio(numero_porta, "Número de porta");
        Funcionario f = obterOuFalhar(id);
        f.getMorada().setNumero_porta(numero_porta);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarRuaFuncionario(int id, String rua) {
        Validacoes.naoVazio(rua, "Rua");
        Funcionario f = obterOuFalhar(id);
        f.getMorada().setRua(rua);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarLocalidadeFuncionario(int id, String localidade) {
        Validacoes.naoVazio(localidade, "Localidade");
        Funcionario f = obterOuFalhar(id);
        f.getMorada().setLocalidade(localidade);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarCodigoPostalFuncionario(int id, String codigo_postal) {
        Validacoes.codigoPostal(codigo_postal);
        Funcionario f = obterOuFalhar(id);
        f.getMorada().setCodigo_postal(codigo_postal);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarTelemovelFuncionario(int id, String telemovel) {
        Validacoes.telemovel(telemovel);
        Funcionario f = obterOuFalhar(id);
        f.setTelemovel(telemovel);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarEmailFuncionario(int id, String email) {
        Validacoes.emailValido(email);
        Funcionario f = obterOuFalhar(id);
        f.setEmail(email);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarDataNascimentoFuncionario(int id, String data_nascimento) {
        Validacoes.naoVazio(data_nascimento, "Data de nascimento");
        Funcionario f = obterOuFalhar(id);
        f.setData_nascimento(data_nascimento);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarNIFFuncionario(int id, String nif) {
        Validacoes.nif(nif);
        Funcionario f = obterOuFalhar(id);
        if (funcionariosDAO.obterPorNif(nif).filter(o -> o.getId() != id).isPresent()) {
            throw new EcoRideException("Já existe um funcionário com este NIF.");
        }
        f.setNIF(nif);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarNUSFuncionario(int id, String nus) {
        Validacoes.naoVazio(nus, "NUS");
        Funcionario f = obterOuFalhar(id);
        f.setNUS(nus);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarIBANFuncionario(int id, String iban) {
        Validacoes.iban(iban);
        Funcionario f = obterOuFalhar(id);
        f.setIBAN(iban);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarSalarioHoraFuncionario(int id, float salario_hora) {
        Validacoes.valorMonetario(salario_hora, "Salário/hora");
        Funcionario f = obterOuFalhar(id);
        f.setSalario_hora(salario_hora);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarSalarioBrutoFuncionario(int id, float salario_bruto) {
        Validacoes.valorMonetario(salario_bruto, "Salário bruto");
        Funcionario f = obterOuFalhar(id);
        f.setSalario_bruto(salario_bruto);
        funcionariosDAO.put(f.getId(), f);
    }

    @Override
    public void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido) {
        Validacoes.valorMonetario(salario_liquido, "Salário líquido");
        Funcionario f = obterOuFalhar(id);
        f.setSalario_liquido(salario_liquido);
        funcionariosDAO.put(f.getId(), f);
    }
}
