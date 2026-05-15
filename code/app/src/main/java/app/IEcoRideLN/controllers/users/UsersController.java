package app.IEcoRideLN.controllers.users;

import java.util.Map;

import app.IEcoRideLN.auth.GestorSessoes;
import app.IEcoRideLN.auth.SessaoUtilizador;
import app.IEcoRideLN.controllers.users.dto.PasswordChangeRequest;
import app.IEcoRideLN.controllers.users.dto.UserRequest;
import app.ecoRideLN.IEcoRideController;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;

public class UsersController {

    private final IEcoRideController facade;

    public UsersController(IEcoRideController facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // Listar todos os utilizadores
        app.get("/api/utilizadores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            ctx.status(200).json(facade.obterUtilizadores());
        });

        // Registar novo utilizador
        app.post("/api/utilizadores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            UserRequest req = ctx.bodyAsClass(UserRequest.class);
            Utilizador criado = facade.registarUtilizador(req.password(), req.idFuncionario(), req.cargo(), req.identificador());
            ctx.status(201).json(criado);
        });

        // Remover utilizador
        app.delete("/api/utilizadores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.removerUtilizador(id);
            ctx.status(204);
        });

        // Atualizar cargo e identificador
        app.patch("/api/utilizadores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            UserRequest req = ctx.bodyAsClass(UserRequest.class);
            Utilizador atualizado = facade.atualizarUtilizador(id, req.idFuncionario(), req.cargo(), req.identificador());
            ctx.status(200).json(atualizado);
        });

        // Obter ID de utilizador a partir do ID de funcionário
        app.get("/api/utilizadores/por-funcionario/{idFuncionario}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria, Cargo.GestorStock);
            int idFuncionario = Integer.parseInt(ctx.pathParam("idFuncionario"));
            int idUtilizador = facade.obterIdUserPorIdFuncionario(idFuncionario);
            ctx.status(200).json(Map.of("idUtilizador", idUtilizador));
        });

        // Alterar palavra-passe — apenas o próprio utilizador ou o Gerente
        app.patch("/api/utilizadores/{id}/password", ctx -> {
            SessaoUtilizador sessao = GestorSessoes.sessao(ctx);
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (sessao.getCargo() != Cargo.Gerente && sessao.getIdUtilizador() != id)
                throw new ForbiddenResponse("Só pode alterar a sua própria palavra-passe");
            PasswordChangeRequest req = ctx.bodyAsClass(PasswordChangeRequest.class);
            facade.atualizarPalavraPasseUtilizador(id, req.currentPassword(), req.newPassword());
            ctx.status(204);
        });
    }
}
