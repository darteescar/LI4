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
    public Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador) {
        validaDadosUtilizador(password, idFuncionario, cargo, identificador);
        if (utilizadoresDAO.existeIdentificador(identificador)) {
            throw new EcoRideException("Identificador '" + identificador + "' já está em uso.");
        }
        int newId = utilizadoresDAO.generateNewId();
        Utilizador utilizador = new Utilizador(newId, password, idFuncionario, cargo, identificador);
        utilizadoresDAO.put(newId, utilizador);
        return utilizador;
    }

    @Override
    public Utilizador atualizarUtilizador(int id, int idFuncionario, Cargo cargo, String identificador) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        Utilizador u = utilizadoresDAO.get(id);
        if (identificador != null && !identificador.isEmpty()
                && !identificador.equals(u.getIdentificador())
                && utilizadoresDAO.existeIdentificador(identificador)) {
            throw new EcoRideException("Identificador '" + identificador + "' já está em uso.");
        }

        if (idFuncionario >= 0) {
            u.setIdFuncionario(idFuncionario);
        }
        if (cargo != null) {
            u.setCargo(cargo);
        }
        if (identificador != null && !identificador.isEmpty()) {
            u.setIdentificador(identificador);
        }
        utilizadoresDAO.put(id, u);
        return u;
    }

    @Override
    public Utilizador obterUtilizador(int id) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.get(id);
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
    public List<Utilizador> obterUtilizadores() {
        return utilizadoresDAO.values().stream().toList();
    }

    // Utilitários

    @Override
    public List<Integer> obterUtilizadoresPorCargo(Cargo ... cargo) {
        return utilizadoresDAO.values().stream()
                .filter(u -> java.util.Arrays.asList(cargo).contains(u.getCargo()))
                .map(Utilizador::getId)
                .toList();
    }

    @Override
    public boolean autenticar(int id, String password) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        Utilizador u = utilizadoresDAO.get(id);
        return u.getPassword().equals(password);
    }

    @Override
    public Cargo obterCargoUtilizador(int id) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        return utilizadoresDAO.get(id).getCargo();
    }

    @Override
    public boolean atualizarPalavraPasseUtilizador(int id, String passwordvelha, String novaPassword) {
        if (!utilizadoresDAO.containsKey(id)) {
            throw new EcoRideException("Utilizador com ID " + id + " não existe.");
        }
        Utilizador u = utilizadoresDAO.get(id);
        if (!u.getPassword().equals(passwordvelha)) {
            throw new EcoRideException("Password antiga incorreta.");
        }
        utilizadoresDAO.updatePassword(id, novaPassword);
        return true;
    }

    @Override
    public Utilizador obterUtilizadorPorIdentificador(String identificador) {
        return utilizadoresDAO.getByIdentificador(identificador);
    }

    @Override
    public int obterIdUserPorIdFuncionario(int idFuncionario) {
        return utilizadoresDAO.getIdByIdFuncionario(idFuncionario);
    }

    private void validaDadosUtilizador(String password, int idFuncionario, Cargo cargo, String identificador) {
        if (password == null || password.isEmpty()) {
            throw new EcoRideException("A password não pode ser vazia.");
        }
        if (idFuncionario < 0) {
            throw new EcoRideException("O ID do funcionário deve ser um número positivo.");
        }
        if (cargo == null) {
            throw new EcoRideException("O cargo do utilizador não pode ser nulo.");
        }
        if (identificador == null || identificador.isEmpty()) {
            throw new EcoRideException("O identificador do utilizador não pode ser vazio.");
        }
    }
}
