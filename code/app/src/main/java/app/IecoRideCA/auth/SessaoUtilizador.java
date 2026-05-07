package app.IecoRideCA.auth;

import app.ecoRideLN.sAutenticacao.Cargo;
import java.time.LocalDateTime;

public class SessaoUtilizador {

    private final int idUtilizador;
    private final int idFuncionario;
    private final Cargo cargo;
    private final LocalDateTime expiracao;

    public SessaoUtilizador(int idUtilizador, int idFuncionario, Cargo cargo) {
        this.idUtilizador = idUtilizador;
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
        this.expiracao = LocalDateTime.now().plusHours(8);
    }

    public boolean expirou() {
        return LocalDateTime.now().isAfter(expiracao);
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
