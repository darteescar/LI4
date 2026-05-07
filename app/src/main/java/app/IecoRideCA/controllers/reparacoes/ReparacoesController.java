package app.IecoRideCA.controllers.reparacoes;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.reparacoes.dto.ReparacaoRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sReparacoes.Reparacao;
import io.javalin.Javalin;

public class ReparacoesController {

    private final IEcoRideLN facade;

    public ReparacoesController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/reparacoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            ctx.json(facade.obterReparacoes());
        });

        app.get("/api/reparacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Reparacao c = facade.obterReparacao(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/reparacoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            ReparacaoRequest req = ctx.bodyAsClass(ReparacaoRequest.class);
            Reparacao criado = facade.registarReparacao(req.nomenclatura(), req.descricao(), req.preco(), req.disponivel());
            ctx.status(201).json(criado);
        });

        app.delete("/api/reparacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerReparacao(id) ? 204 : 404);
        });
    }
}
