package app.IecoRideCA.controllers.users;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.auth.SessaoUtilizador;
import app.IecoRideCA.controllers.users.dto.PasswordChangeRequest;
import app.IecoRideCA.controllers.users.dto.UserRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sAutenticacao.Utilizador;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;

public class UsersController {

    private final IEcoRideLN facade;

    public UsersController(IEcoRideLN facade) {
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
