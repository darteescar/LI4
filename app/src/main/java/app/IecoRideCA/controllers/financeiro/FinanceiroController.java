package app.IecoRideCA.controllers.financeiro;

import app.IecoRideCA.auth.GestorSessoes;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sStock.Fornecedor;
import app.IecoRideCA.controllers.financeiro.dto.FornecedorRequest;
import io.javalin.Javalin;

public class FinanceiroController {

    private final IEcoRideLN facade;

    public FinanceiroController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            ctx.json(facade.obterFornecedores());
        });

        app.get("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Fornecedor f = facade.obterFornecedor(id);
            if (f == null) ctx.status(404);
            else ctx.json(f);
        });

        app.post("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            Fornecedor criado = facade.registarFornecedor(req.getNome(), req.getEmail(), req.getTelemovel());
            ctx.status(201).json(criado);
        });

        app.delete("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFornecedor(id) ? 204 : 404);
        });

    }
}
