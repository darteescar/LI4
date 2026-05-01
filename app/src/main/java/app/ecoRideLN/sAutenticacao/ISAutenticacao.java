package app.ecoRideLN.sAutenticacao;

public interface ISAutenticacao {

    
    void atualizarPalavraPasseUtilizador(int id, String novaPassword);

    Utilizador criarUtilizador(String password,int idFuncionario,  Cargo cargo);

    Utilizador obterDadosUtilizador(int id);

    boolean removerUtilizador(int id);

    boolean existeUtilizador(int id);

    boolean autenticar(int id, String password);

    Cargo obterCargoUtilizador(int id);

}
