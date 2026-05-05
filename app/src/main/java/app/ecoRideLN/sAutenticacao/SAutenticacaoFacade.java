package app.ecoRideLN.sAutenticacao;

import java.util.List;

import app.common.EcoRideException;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;

public class SAutenticacaoFacade implements ISAutenticacao {
    private final UtilizadorDAO utilizadoresDAO;

    public SAutenticacaoFacade() {
        this.utilizadoresDAO = UtilizadorDAO.getInstance();
    }

    @Override
    public Utilizador criarUtilizador(String password, int idFuncionario, Cargo cargo) {
        if (utilizadoresDAO.containsKey(idFuncionario)) {
            throw new EcoRideException("Utilizador com ID " + idFuncionario + " já existe.");
        }
        Utilizador utilizador = new Utilizador(utilizadoresDAO.generateNewId(), password, idFuncionario, cargo);
        utilizadoresDAO.add(utilizador);
        return utilizador;
    }

    @Override
    public Utilizador obterDadosUtilizador(int id) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.get(id);
    }

    @Override
    public List<Utilizador> obterUtilizadoresPorCargo(Cargo cargo) {
        return utilizadoresDAO.values().stream()
                .filter(u -> u.getCargo() == cargo)
                .toList();
    }

    @Override
    public boolean existeUtilizador(int id) {
        return utilizadoresDAO.containsKey(id);
    }

    @Override
    public boolean removerUtilizador(int id) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        utilizadoresDAO.remove(id);
        return true;
    }

    @Override
    public boolean autenticar(int id, String password) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.authenticate(id, password);
    }

    @Override
    public Cargo obterCargoUtilizador(int id) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.get(id).getCargo();
    }

    @Override
    public void atualizarPalavraPasseUtilizador(int id, String novaPassword) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        utilizadoresDAO.updatePassword(id, novaPassword);
    }
}
