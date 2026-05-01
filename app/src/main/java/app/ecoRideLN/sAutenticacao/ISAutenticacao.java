package app.ecoRideLN.sAutenticacao;

public interface ISAutenticacao {
    boolean autenticar(int id, String password);
    Cargo cargoDe(int id);
}
