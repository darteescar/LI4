package app.ecoRideLN.sAutenticacao;

import app.common.EcoRideException;
import app.common.Validacoes;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;
import app.ecoRideLN.sFuncionarios.ISFuncionarios;

import java.util.Optional;

public class SAutenticacaoFacade implements ISAutenticacao {

    private final UtilizadorDAO utilizadoresDAO = new UtilizadorDAO();
    private int idUtilizador = 1;

    private final ISFuncionarios sFuncionarios;

    public SAutenticacaoFacade(ISFuncionarios sFuncionarios) {
        this.sFuncionarios = sFuncionarios;
    }

    @Override
    public Utilizador criarUtilizador(int idFuncionario, String palavra_passe, Cargo cargo) {
        Validacoes.inteiroPositivo(idFuncionario, "Id do funcionário");
        Validacoes.naoVazio(palavra_passe, "Palavra-passe");
        Validacoes.naoNulo(cargo, "Cargo");

        if (!sFuncionarios.existeFuncionario(idFuncionario))
            throw new EcoRideException("Funcionário associado não existe.");

        if (utilizadoresDAO.obterPorIdFuncionario(idFuncionario).isPresent())
            throw new EcoRideException("Já existe utilizador para este funcionário.");

        Utilizador u = new Utilizador(idUtilizador++, palavra_passe, idFuncionario, cargo);
        utilizadoresDAO.put(u.getId(), u);
        return u;
    }

    @Override
    public Optional<Utilizador> obterDadosUtilizador(int id) {
        return utilizadoresDAO.obterPorId(id);
    }

    @Override
    public void removerUtilizador(int id) {
        if (!existeUtilizador(id))
            throw new EcoRideException("Utilizador não encontrado.");
        utilizadoresDAO.remove(id);
    }

    @Override
    public boolean existeUtilizador(int id) {
        return utilizadoresDAO.containsKey(id);
    }

    @Override
    public boolean autenticar(int id, String palavra_passe) {
        Validacoes.naoVazio(palavra_passe, "Palavra-passe");
        return utilizadoresDAO.obterPorId(id)
                .map(u -> u.getPassword().equals(palavra_passe))
                .orElse(false);
    }

    @Override
    public Cargo obterCargoUtilizador(int id) {
        return utilizadoresDAO.obterPorId(id)
                .map(Utilizador::getCargo)
                .orElseThrow(() -> new EcoRideException("Utilizador não encontrado."));
    }

    @Override
    public void atualizarPalavraPasseUtilizador(int id, String palavra_passe) {
        Validacoes.naoVazio(palavra_passe, "Palavra-passe");
        Utilizador u = utilizadoresDAO.obterPorId(id)
                .orElseThrow(() -> new EcoRideException("Utilizador não encontrado."));
        u.setPassword(palavra_passe);
        utilizadoresDAO.put(u.getId(), u);
    }
}
