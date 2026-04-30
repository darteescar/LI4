package app.ecoRideLN.sAutenticacao;

import java.util.Optional;

public interface ISAutenticacao {

    Utilizador criarUtilizador(int idFuncionario, String palavra_passe, Cargo cargo);

    Optional<Utilizador> obterDadosUtilizador(int id);

    Optional<Utilizador> obterUtilizadorPorIdFuncionario(int idFuncionario);

    void removerUtilizador(int id);

    boolean existeUtilizador(int id);

    boolean autenticar(int id, String palavra_passe);

    Cargo obterCargoUtilizador(int id);

    void atualizarPalavraPasseUtilizador(int id, String palavra_passe);
}
