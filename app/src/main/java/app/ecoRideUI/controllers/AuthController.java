package app.ecoRideUI.controllers;

import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideUI.auth.GestorSessoes;
import app.ecoRideUI.auth.SessaoUtilizador;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;

public class AuthController {

    private final IEcoRideLN facade;
    private final GestorSessoes gestorSessoes;

    public AuthController(IEcoRideLN facade, GestorSessoes gestorSessoes) {
        this.facade = facade;
        this.gestorSessoes = gestorSessoes;
    }

    public void register(Javalin app) {

        app.post("/auth/login", ctx -> {
            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
            if (!facade.autenticar(req.id(), req.password())) {
                throw new UnauthorizedResponse("Credenciais inválidas");
            }
            Cargo cargo = facade.obterCargoUtilizador(req.id());
            int idFuncionario = facade.obterIdFuncionario_Utilizador(req.id());
            String token = gestorSessoes.criarSessao(new SessaoUtilizador(req.id(), idFuncionario, cargo));
            ctx.status(200).json(new LoginResponse(token, cargo.name()));
        });

        app.post("/api/auth/logout", ctx -> {
            String token = ctx.header("Authorization");
            if (token != null) {
                gestorSessoes.terminarSessao(token);
            }
            ctx.status(204);
        });
    }

    record LoginRequest(int id, String password) {

    }

    record LoginResponse(String token, String cargo) {

    }
}
