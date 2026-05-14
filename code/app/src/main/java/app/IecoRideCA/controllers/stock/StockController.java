package app.IecoRideCA.controllers.stock;

import java.util.List;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.ordensservico.dto.DefeitoToDevolucaoRequest;
import app.IecoRideCA.controllers.stock.dto.DefeitoRequest;
import app.IecoRideCA.controllers.stock.dto.EncomendaRequest;
import app.IecoRideCA.controllers.stock.dto.FornecedorRequest;
import app.IecoRideCA.controllers.stock.dto.PecaRequest;
import app.IecoRideCA.controllers.stock.dto.SplitDefeitoRequest;
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

        app.get("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico, Cargo.Secretaria);
            ctx.status(200).json(facade.obterFornecedores());
        });

        app.get("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Fornecedor c = facade.obterFornecedor(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        app.get("/api/fornecedores/{id}/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200).json(facade.obterPecasDoFornecedor(id));
        });

        app.post("/api/fornecedores", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            ctx.status(201).json(facade.registarFornecedor(req.nome(), req.telemovel(), req.email()));
        });

        app.delete("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFornecedor(id) ? 204 : 404);
        });

        app.patch("/api/fornecedores/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            FornecedorRequest req = ctx.bodyAsClass(FornecedorRequest.class);
            Fornecedor atualizado = facade.atualizarFornecedor(id, req.nome(), req.telemovel(), req.email());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Peça

        app.get("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico);
            ctx.status(200).json(facade.obterPecas());
        });

        app.get("/api/pecasAtivas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.status(200).json(facade.obterPecasAtivas());
        });

        app.get("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Peca c = facade.obterPeca(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        app.post("/api/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            PecaRequest req = ctx.bodyAsClass(PecaRequest.class);
            ctx.status(201).json(facade.registarPeca(req.referencia(), req.marca(), req.nome(), req.descricao(), req.stock_minimo(), req.preco_venda(), req.codFornecedor(), req.garantia()));
        });

        app.delete("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerPeca(id) ? 204 : 404);
        });

        app.patch("/api/pecas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PecaRequest req = ctx.bodyAsClass(PecaRequest.class);
            Peca atualizado = facade.atualizarPeca(id, req.referencia(), req.marca(), req.nome(), req.descricao(), req.stock_minimo(), req.preco_venda(), req.codFornecedor(), req.ativa(), req.garantia());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Stock

        app.get("/api/stocks", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico);
            ctx.status(200).json(facade.obterStocks());
        });

        app.get("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Stock c = facade.obterStock(id);
            if (c == null) ctx.status(404);
            else ctx.status(200).json(c);
        });

        app.post("/api/stocks", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            StockRequest req = ctx.bodyAsClass(StockRequest.class);
            ctx.status(201).json(facade.registarStock(req.codPeca(), req.preco(), req.dataChegada(), req.quantidade()));
        });

        app.delete("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerStock(id) ? 204 : 404);
        });

        app.patch("/api/stocks/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            StockRequest req = ctx.bodyAsClass(StockRequest.class);
            Stock atualizado = facade.atualizarStock(id, req.preco(), req.codPeca(), req.dataChegada(), req.quantidade());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Encomenda

        app.get("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterEncomendas());
        });

        app.get("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda c = facade.obterEncomendas().stream()
                .filter(e -> e.getId() == id).findFirst().orElse(null);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/encomendas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            EncomendaRequest req = ctx.bodyAsClass(EncomendaRequest.class);
            List<Integer> codPecas    = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::codPeca).collect(java.util.stream.Collectors.toList());
            List<Float>   precos      = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::preco_compra).collect(java.util.stream.Collectors.toList());
            List<Integer> quantidades = req.itens().stream().map(EncomendaRequest.ItemEncomendaRequest::quantidade).collect(java.util.stream.Collectors.toList());
            ctx.status(201).json(facade.registarEncomenda(codPecas, precos, quantidades, req.cod_fornecedor()));
        });

        app.delete("/api/encomendas/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerEncomenda(id) ? 204 : 404);
        });

        app.patch("/api/encomendas/{id}/enviada", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda atualizado = facade.marcarEncomendaComoEnviada(id);
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        app.post("/api/encomendas/automatica", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.status(201).json(facade.gerarListaAutomatica());
        });

        app.patch("/api/encomendas/{id}/recebida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Encomenda atualizado = facade.marcarEncomendaComoRecebida(id);
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Defeitos

        app.get("/api/defeitos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterDefeitos());
        });

        app.delete("/api/defeitos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerDefeito(id) ? 204 : 404);
        });

        app.post("/api/defeitos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock, Cargo.Mecanico);
            DefeitoRequest req = ctx.bodyAsClass(DefeitoRequest.class);
            ctx.status(201).json(facade.registarDefeito(req.codPeca(), req.motivo(), req.idFuncionario()));
        });

        app.patch("/api/defeitos/{id}/devolver", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            DefeitoToDevolucaoRequest req = ctx.bodyAsClass(DefeitoToDevolucaoRequest.class);
            facade.confirmarDefeitoComDevolucao(id, req.motivo(), req.data());
            ctx.status(204);
        });

        app.patch("/api/defeitos/{id}/descartar", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.descartarDefeito(id);
            ctx.status(204);
        });

        app.patch("/api/defeitos/{id}/split", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            SplitDefeitoRequest req = ctx.bodyAsClass(SplitDefeitoRequest.class);
            facade.resolverDefeitoComSplit(id, req.quantidade(), req.motivo(), req.data());
            ctx.status(204);
        });

        // Devoluções

        app.get("/api/devolucoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterDevolucoes());
        });

        app.delete("/api/devolucoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerDevolucao(id) ? 204 : 404);
        });

        app.patch("/api/devolucoes/{id}/enviada", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.marcarDevolucaoComoEnviada(id);
            ctx.status(204);
        });

        app.patch("/api/devolucoes/{id}/devolvida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.marcarDevolucaoComoDevolvida(id);
            ctx.status(204);
        });

        app.patch("/api/devolucoes/{id}/invalida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.marcarDevolucaoComoInvalida(id);
            ctx.status(204);
        });
    }
}
