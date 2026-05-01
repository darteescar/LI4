package app.ecoRideLN.sAutenticacao;

import app.common.EcoRideException;
import app.ecoRideCD.sAutenticacao.UtilizadorDAO;

public class SAutenticacaoFacade implements ISAutenticacao {
    private final UtilizadorDAO utilizadoresDAO;

    public SAutenticacaoFacade() {
        this.utilizadoresDAO = UtilizadorDAO.getInstance();
    }

    @Override
    public boolean autenticar(int id, String password) {
        Utilizador u = utilizadoresDAO.findById(id);
        return u != null && u.getPassword().equals(password);
    }

    @Override
    public Cargo cargoDe(int id) {
        Utilizador u = utilizadoresDAO.findById(id);
        if (u == null) throw new EcoRideException("Utilizador inexistente: " + id);
        return u.getCargo();
    }
}
