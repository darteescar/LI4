package app.ecoRideLN.sAutenticacao;

import java.util.List;

public interface ISAutenticacao {

     Utilizador criarUtilizador(String password, int idFuncionario, Cargo cargo);

     Utilizador obterDadosUtilizador(int id);

     List<Utilizador> obterUtilizadoresPorCargo(Cargo cargo);

     boolean existeUtilizador(int id);

     boolean removerUtilizador(int id);

     boolean autenticar(int id, String password);

     Cargo obterCargoUtilizador(int id);

     void atualizarPalavraPasseUtilizador(int id, String novaPassword);
}
