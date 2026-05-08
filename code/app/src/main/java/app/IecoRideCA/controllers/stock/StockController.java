package app.IecoRideCA.controllers.stock;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.stock.dto.EncomendaRequest;
import app.IecoRideCA.controllers.stock.dto.FornecedorRequest;
import app.IecoRideCA.controllers.stock.dto.PecaRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sStock.Encomenda;
import app.ecoRideLN.sStock.Fornecedor;
import app.ecoRideLN.sStock.Peca;
import app.ecoRideLN.sStock.Stock;
import io.javalin.Javalin;

public class StockController {

    private final IEcoRideLN facade;

    public StockController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // Fornecedor

        app.get("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterFornecedores());
        });

        app.get("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Fornecedor c = facade.obterFornecedor(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            Fornecedor criado = facade.registarFornecedor(req.nome(), req.telemovel(), req.email());
            ctx.status(201).json(criado);
        });

        app.delete("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFornecedor(id) ? 204 : 404);
        });

        // Peça

        app.get("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterPecas());
        });

        app.get("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Peca c = facade.obterPeca(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            PecaRequest req = ctx.bodyAsClass(PecaRequest.class);
            Peca criado = facade.registarPeca(req.referencia(), req.nome(), req.descricao(), req.stock_minimo(), req.preco_venda(), req.codFornecedor());
            ctx.status(201).json(criado);
        });

        app.delete("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerPeca(id) ? 204 : 404);
        });

        // Stock

        /* 
        app.get("/api/stock", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico);
            // TODO obterStocks()
            //ctx.json(facade.obterStocks());
        });*/

        app.get("/api/stock/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Stock c = facade.obterStock(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        // TODO diferenciar entre peças com garantia e peças normais
        /*app.post("/api/stock", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            StockRequest req = ctx.bodyAsClass(StockRequest.class);
            Stock criado = facade.registarStock(req.codPeca(), req.quantidade());
            ctx.status(201).json(criado);
        });*/

        app.delete("/api/stock/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerStock(id) ? 204 : 404);
        });

        // Encomenda

        app.get("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterEncomendas());
        });

        app.get("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda c = facade.obterEncomenda(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            EncomendaRequest req = ctx.bodyAsClass(EncomendaRequest.class);
            Encomenda criado = facade.registarEncomenda(req.itens(), req.cod_fornecedor());
            ctx.status(201).json(criado);
        });

        app.delete("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerEncomenda(id) ? 204 : 404);
        });

    }
}
