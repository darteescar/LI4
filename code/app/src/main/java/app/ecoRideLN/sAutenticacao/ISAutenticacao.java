package app.ecoRideLN.sAutenticacao;

import java.util.List;

public interface ISAutenticacao {

     public Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador);

     public Utilizador atualizarUtilizador(int id, int idFuncionario, Cargo cargo, String identificador);

     public Utilizador obterUtilizador(int id);

     public boolean existeUtilizador(int id);

     public boolean removerUtilizador(int id);

     public List<Utilizador> obterUtilizadores();

     // Utilitários

     public List<Integer> obterUtilizadoresPorCargo(Cargo ... cargo);

     public boolean autenticar(int id, String password);

     public Cargo obterCargoUtilizador(int id);

     public boolean atualizarPalavraPasseUtilizador(int id, String passwordvelha, String novaPassword);

     public void atualizarCargoUtilizador(int id, Cargo novoCargo);
}
