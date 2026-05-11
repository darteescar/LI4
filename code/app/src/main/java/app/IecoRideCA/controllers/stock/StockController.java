package app.IecoRideCA.controllers.stock;

import java.util.List;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.stock.dto.EncomendaRequest;
import app.IecoRideCA.controllers.stock.dto.FornecedorRequest;
import app.IecoRideCA.controllers.stock.dto.PecaRequest;
import app.IecoRideCA.controllers.stock.dto.StockComGarantiaRequest;
import app.IecoRideCA.controllers.stock.dto.StockRequest;
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

        // List<Fornecedor> 
        app.get("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.status(200).json(facade.obterFornecedores());
        });

        // Obter Fornecedor por id
        app.get("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Fornecedor c = facade.obterFornecedor(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        // Registar Fornecedor
        app.post("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            Fornecedor criado = facade.registarFornecedor(req.nome(), req.telemovel(), req.email());
            ctx.status(201).json(criado);
        });

        // Remover Fornecedor
        app.delete("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFornecedor(id) ? 204 : 404);
        });

        // Atualizar Fornecedor
        app.patch("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            Fornecedor atualizado = facade.atualizarFornecedor(id, req.nome(), req.telemovel(), req.email());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Peça

        // List<Peca>
        app.get("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.status(200).json(facade.obterPecas());
        });

        // Obter Peça por id
        app.get("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Peca c = facade.obterPeca(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        // Registar Peça
        app.post("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            PecaRequest req = ctx.bodyAsClass(PecaRequest.class);
            Peca criado = facade.registarPeca(req.referencia(), req.nome(), req.descricao(), req.stock_minimo(), req.preco_venda(), req.codFornecedor());
            ctx.status(201).json(criado);
        });

        // Remover Peça
        app.delete("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerPeca(id) ? 204 : 404);
        });

        // Atualizar Peça
        app.patch("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PecaRequest req = ctx.bodyAsClass(PecaRequest.class);
            Peca atualizado = facade.atualizarPeca(id, req.referencia(), req.nome(), req.descricao(), req.stock_minimo(), req.preco_venda(), req.codFornecedor(), req.ativa());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Stock

        // List<Stock>
        app.get("/api/stocks", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico);
            ctx.status(200).json(facade.obterStocks());
        });

        // Obter Stock por id
        app.get("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Stock c = facade.obterStock(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        // Criar Stock (Peça Normal)
        app.post("/api/stocks", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            StockRequest req = ctx.bodyAsClass(StockRequest.class);
            Stock criado = facade.registarStock_PecaNormal(req.codPeca(), req.preco(),  req.dataChegada(), req.quantidade());
            ctx.status(201).json(criado);
        });

        // Criar Stock (Peça com Garantia)
        app.post("/api/stockGarantia", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            StockComGarantiaRequest req = ctx.bodyAsClass(StockComGarantiaRequest.class);
            Stock atualizado = facade.registarStockComGarantia(req.codPeca(), req.preco(), req.dataChegada(), req.garantia(), req.nr_serie());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Remover Stock
        app.delete("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerStock(id) ? 204 : 404);
        });

        // Atualizar Stock Normal
        app.patch("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            StockRequest req = ctx.bodyAsClass(StockRequest.class);
            Stock atualizado = facade.atualizarStock(id, req.preco(), req.codPeca(), req.dataChegada(), req.quantidade());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Atualizar Stock com Garantia
        app.patch("/api/stockGarantia/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            StockComGarantiaRequest req = ctx.bodyAsClass(StockComGarantiaRequest.class);
            Stock atualizado = facade.atualizarStockComGarantia(id, req.preco(), req.codPeca(), req.dataChegada(), req.quantidade(), req.garantia(), req.nr_serie());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Encomenda

        // List<Encomenda>
        app.get("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterEncomendas());
        });

        // Obter Encomenda por id
        app.get("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda c = facade.obterEncomendas().stream()
                .filter(e -> e.getId() == id).findFirst().orElse(null);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        // Registar Encomenda
        app.post("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            EncomendaRequest req = ctx.bodyAsClass(EncomendaRequest.class);
            List<Integer> codPecas    = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::codPeca).collect(java.util.stream.Collectors.toList());
            List<Float>   precos      = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::preco_compra).collect(java.util.stream.Collectors.toList());
            List<Integer> quantidades = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::quantidade).collect(java.util.stream.Collectors.toList());
            Encomenda criado = facade.registarEncomenda(codPecas, precos, quantidades, req.cod_fornecedor());
            ctx.status(201).json(criado);
        });

        // Remover Encomenda
        app.delete("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = facade.removerEncomenda(id);
            ctx.status(removed ? 204 : 404);
        });

        // Marcar Encomenda como enviada
        app.patch("/api/encomendasEnviada/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda atualizado = facade.marcarEncomendaComoEnviada(id);
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Marcar Encomenda como recebida
        app.patch("/api/encomendasRecebida/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda atualizado = facade.marcarEncomendaComoRecebida(id, List.of(), List.of());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

    }
}
