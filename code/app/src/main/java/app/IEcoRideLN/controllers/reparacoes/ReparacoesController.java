package app.IEcoRideLN.controllers.reparacoes;

import app.IEcoRideLN.auth.GestorSessoes;
import app.IEcoRideLN.controllers.reparacoes.dto.ReparacaoRequest;
import app.ecoRideLN.IEcoRideController;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sReparacoes.Reparacao;
import io.javalin.Javalin;

public class ReparacoesController {

    private final IEcoRideController facade;

    public ReparacoesController(IEcoRideController facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // List<Reparacao> — ?disponiveis=true para filtrar apenas as disponíveis
        app.get("/api/reparacoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            boolean apenasDisponiveis = "true".equalsIgnoreCase(ctx.queryParam("disponiveis"));
            ctx.status(200).json(apenasDisponiveis ? facade.obterReparacoesDisponiveis() : facade.obterReparacoes());
        });

        // Obter Reparacao por id
        app.get("/api/reparacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Reparacao c = facade.obterReparacao(id);
            if (c == null) {
                ctx.status(404);
            } else {
                ctx.status(200).json(c);
            }
        });

        // Criar Reparacao
        app.post("/api/reparacoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            ReparacaoRequest req = ctx.bodyAsClass(ReparacaoRequest.class);
            ctx.status(201).json(facade.registarReparacao(req.nomenclatura(), req.descricao(), req.preco(), req.disponivel()));
        });

        // Eliminar Reparacao
        app.delete("/api/reparacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = facade.removerReparacao(id);
            if (removed) {
                ctx.status(204);
            }
            else {
                ctx.status(404);
            }
        });

        // Editar Reparacao
        app.patch("/api/reparacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ReparacaoRequest req = ctx.bodyAsClass(ReparacaoRequest.class);
            Reparacao editado = facade.atualizarReparacao(id, req.nomenclatura(), req.descricao(), req.preco(), req.disponivel());
            if (editado == null) {
                ctx.status(404);
            }
            else {
                ctx.status(200).json(editado);
            }
        });
    }
}
