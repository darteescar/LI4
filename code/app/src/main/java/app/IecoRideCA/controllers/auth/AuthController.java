package app.IecoRideCA.controllers.auth;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.auth.SessaoUtilizador;
import app.IecoRideCA.controllers.auth.dto.LoginRequest;
import app.IecoRideCA.controllers.auth.dto.LoginResponse;
import app.IecoRideCA.controllers.auth.dto.RegistRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;
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

        app.post("/auth/register", ctx -> {
            RegistRequest req = ctx.bodyAsClass(RegistRequest.class);
            Utilizador criado = facade.registarUtilizador(req.password(), req.idFuncionario(), req.cargo(), req.identificador());
            ctx.status(201).json(criado);
        });

        app.post("/auth/login", ctx -> {
            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
            if (!facade.autenticar(req.id(), req.password()))
                throw new UnauthorizedResponse("Credenciais inválidas");
            Cargo cargo       = facade.obterCargoUtilizador(req.id());
            int idFuncionario = facade.obterIdFuncionario_Utilizador(req.id());
            String token      = gestorSessoes.criarSessao(new SessaoUtilizador(req.id(), idFuncionario, cargo));
            ctx.status(200).json(new LoginResponse(token, cargo.name()));
        });

        app.post("/api/auth/logout", ctx -> {
            String token = ctx.header("Authorization");
            if (token != null) gestorSessoes.terminarSessao(token);
            ctx.status(204);
        });
    }
}
