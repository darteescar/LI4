package app.ecoRideLN.sAutenticacao;

import java.util.List;

public interface ISAutenticacao {

     Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo);

     Utilizador obterUtilizador(int id);

     boolean existeUtilizador(int id);

     boolean removerUtilizador(int id);

     List<Utilizador> obterUtilizadores();

     // Utilitários

     List<Utilizador> obterUtilizadoresPorCargo(Cargo cargo);

     boolean autenticar(int id, String password);

     Cargo obterCargoUtilizador(int id);

     void atualizarPalavraPasseUtilizador(int id, String novaPassword);

     void atualizarCargoUtilizador(int id, Cargo novoCargo);
}
