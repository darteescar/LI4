package app.IecoRideCA.controllers.ordensservico;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.stock.dto.EncomendaRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import app.ecoRideLN.sStock.Encomenda;
import io.javalin.Javalin;

public class OrdemServicoController {

    private final IEcoRideLN facade;

    public OrdemServicoController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/ordensservico", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            ctx.json(facade.obterOSs());
        });

        app.get("/api/ordensservico/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrdemServico c = facade.obterOS(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/ordensservico", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            // Assuming there's a request class for creating OrdemServico
            // OrdemServicoRequest req = ctx.bodyAsClass(OrdemServicoRequest.class);
            // OrdemServico criado = facade.registarOS(req...);
            // ctx.status(201).json(criado);
        });

        app.delete("/api/ordensservico/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerOS(id) ? 204 : 404);
        });
    }
}
