package app.IecoRideCA.controllers.auth;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.auth.SessaoUtilizador;
import app.IecoRideCA.controllers.auth.dto.LoginRequest;
import app.IecoRideCA.controllers.auth.dto.LoginResponse;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;

public class AuthController {

    private final IEcoRideLN    facade;
    private final GestorSessoes gestorSessoes;

    public AuthController(IEcoRideLN facade, GestorSessoes gestorSessoes) {
        this.facade        = facade;
        this.gestorSessoes = gestorSessoes;
    }

    public void register(Javalin app) {

        app.post("/auth/login", ctx -> {
            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
            var utilizador = facade.obterUtilizadorPorIdentificador(req.identificador());
            if (utilizador == null || !facade.autenticar(utilizador.getId(), req.password()))
                throw new UnauthorizedResponse("Credenciais inválidas");
            Cargo cargo       = utilizador.getCargo();
            int idFuncionario = utilizador.getIdFuncionario();
            String token      = gestorSessoes.criarSessao(new SessaoUtilizador(utilizador.getId(), idFuncionario, cargo));
            ctx.status(200).json(new LoginResponse(token, cargo.name(), idFuncionario, utilizador.getId()));
        });

        app.post("/api/auth/logout", ctx -> {
            String token = ctx.header("Authorization");
            if (token != null) gestorSessoes.terminarSessao(token);
            ctx.status(204);
        });
    }
}
