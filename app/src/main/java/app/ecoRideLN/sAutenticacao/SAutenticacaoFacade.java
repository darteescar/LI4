package app.ecoRideLN.sAutenticacao;

import app.common.EcoRideException;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;

public class SAutenticacaoFacade implements ISAutenticacao {
    private final UtilizadorDAO utilizadoresDAO;
    

    public SAutenticacaoFacade() {
        this.utilizadoresDAO = UtilizadorDAO.getInstance();
    }

    @Override
    public void atualizarPalavraPasseUtilizador(int id, String novaPassword){
        if (!utilizadoresDAO.exists(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        utilizadoresDAO.updatePassword(id, novaPassword);
    }

    @Override
    public Utilizador criarUtilizador(String password, int idFuncionario, Cargo cargo) {
        if (utilizadoresDAO.exists(idFuncionario)) {
            throw new EcoRideException("Utilizador com ID " + idFuncionario + " já existe.");
        }
        Utilizador utilizador = new Utilizador(utilizadoresDAO.generateNewId(), password, idFuncionario, cargo);
        utilizadoresDAO.add(utilizador);
        return utilizador;
    }

    @Override
    public Utilizador obterDadosUtilizador(int id) {
        if (!utilizadoresDAO.exists(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.findById(id);
    }

    @Override
    public boolean removerUtilizador(int id) {
        if (!utilizadoresDAO.exists(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        utilizadoresDAO.remove(id);
        return true;
    }

    @Override
    public boolean existeUtilizador(int id) {
        return utilizadoresDAO.exists(id);
    }

    @Override
    public boolean autenticar(int id, String password) {
        if (!utilizadoresDAO.exists(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.authenticate(id, password);
    }

    @Override
    public Cargo obterCargoUtilizador(int id) {
        if (!utilizadoresDAO.exists(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.findById(id).getCargo();
    }
}
