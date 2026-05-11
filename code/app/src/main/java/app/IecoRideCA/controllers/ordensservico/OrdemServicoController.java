package app.IecoRideCA.controllers.ordensservico;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.ordensservico.dto.ConsertoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.DefeitoStockComGarantiaRequest;
import app.IecoRideCA.controllers.ordensservico.dto.DefeitoStockRequest;
import app.IecoRideCA.controllers.ordensservico.dto.DefeitoToDevolucaoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.DevolucaoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.DiagnosticoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.OrdemServicoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.PagamentoRequest;
import app.IecoRideCA.controllers.ordensservico.dto.PegarOSRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import io.javalin.Javalin;

public class OrdemServicoController {

    private final IEcoRideLN facade;

    public OrdemServicoController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // List<OrdemServico>
        app.get("/api/ordensservicos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            ctx.json(facade.obterOSs());
        });

        // List<OrdemServico> por cliente
        app.get("/api/ordensservicos/cliente/{id_cliente}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id_cliente = Integer.parseInt(ctx.pathParam("id_cliente"));
            ctx.json(facade.obterOSs_Cliente(id_cliente));
        });

        // List<OrdemServico> por trotinete
        app.get("/api/ordensservicos/trotinete/{id_trotinete}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id_trotinete = Integer.parseInt(ctx.pathParam("id_trotinete"));
            ctx.json(facade.obterOSs_Trotinete(id_trotinete));
        });

        // Obter OrdemServico por id
        app.get("/api/ordensservicos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrdemServico c = facade.obterOS(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        // Registar OrdemServico
        app.post("/api/ordensservicos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            OrdemServicoRequest req = ctx.bodyAsClass(OrdemServicoRequest.class);
            OrdemServico criado = facade.registarOS(req.id_cliente(), req.id_trotinete(), req.descricao(), req.acessorios(), req.fotografias(), req.codCriador());
            ctx.status(201).json(criado);
        });

        // Remover OrdemServico
        app.delete("/api/ordensservicos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerOS(id) ? 204 : 404);
        });

        // Atribuir OrdemServico a Funcionario
        app.patch("/api/ordensservicos/pegarNaOs/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PegarOSRequest req = ctx.bodyAsClass(PegarOSRequest.class);
            facade.atribuirOS(id, req.id_funcionario());
            ctx.status(204);
        });

        // Cancelar OrdemServico
        app.patch("/api/ordensservicos/cancelarOS/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.cancelarOS(id);
            ctx.status(204);
        });

        // Adicionar Diagnostico a OrdemServico
        app.patch("/api/ordensservicos/addDiagnostico/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            DiagnosticoRequest req = ctx.bodyAsClass(DiagnosticoRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.registarDiagnosticoOS(id, req.pecasQuantidades(), req.reparacoes(), req.descricao(), idFuncionario);
            ctx.status(204);
        });

        // Adicionar Conserto a OrdemServico
        app.patch("/api/ordensservicos/addConserto/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ConsertoRequest req = ctx.bodyAsClass(ConsertoRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.registarConsertoOS(id, req.pecasQuantidades(), req.reparacoes(), idFuncionario, req.checklist());
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/aprovarOrcamento/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.aprovarOrcamentoOS(id);
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/rejeitarOrcamento/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.rejeitarOrcamentoOS(id);
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/clienteNotificado/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.registarNotificacaoPagamentoOS(id);
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/pagarOS/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PagamentoRequest req = ctx.bodyAsClass(PagamentoRequest.class);
            facade.registarPagamentoOS(id, req.metodo_pagamento());
            ctx.status(204);
        });

        app.put("/api/ordensservicos/defeitoEmStock", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            DefeitoStockRequest req = ctx.bodyAsClass(DefeitoStockRequest.class);
            facade.reportarDefeitoFungivelConsertoOS(req.idOS(), req.codPeca(), req.motivo(), req.idFuncionario() );
            ctx.status(204);
        });

        app.put("/api/ordensservicos/defeitoEmStockComGarantia", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            DefeitoStockComGarantiaRequest req = ctx.bodyAsClass(DefeitoStockComGarantiaRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.reportarDefeitoSerializadoConsertoOS(req.idOS(), req.codStocks(), req.motivo(), idFuncionario);
            ctx.status(204);
        });

        // Defeitos

        app.get("/api/ordensservicos/defeitos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterDefeitos());
        });

        app.delete("/api/ordensservicos/defeitos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerDefeito(id) ? 204 : 404);
        });

        app.patch("/api/ordensservicos/defeitos/devolver", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            DefeitoToDevolucaoRequest req = ctx.bodyAsClass(DefeitoToDevolucaoRequest.class);
            facade.confirmarDefeitoComDevolucao(req.idDefeito(), req.motivo(), req.data());
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/defeitos/descartar", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            DefeitoToDevolucaoRequest req = ctx.bodyAsClass(DefeitoToDevolucaoRequest.class);
            facade.descartarDefeito(req.idDefeito());
            ctx.status(204);
        });

        // Devoluções

        app.get("/api/ordensservicos/devolucoes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            ctx.json(facade.obterDevolucoes());
        });

         app.delete("/api/ordensservicos/devolucoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerDevolucao(id) ? 204 : 404);
        });

        app.patch("/api/ordensservicos/devolucoes/marcarEnviada", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            DevolucaoRequest req = ctx.bodyAsClass(DevolucaoRequest.class);
            facade.marcarDevolucaoComoEnviada(req.idDevolucao());
            ctx.status(204);
        });

         app.patch("/api/ordensservicos/devolucoes/marcarDevolvida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            DevolucaoRequest req = ctx.bodyAsClass(DevolucaoRequest.class);
            facade.marcarDevolucaoComoDevolvida(req.idDevolucao());
            ctx.status(204);
        });

        app.patch("/api/ordensservicos/devolucoes/marcarInvalida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.GestorStock);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            DevolucaoRequest req = ctx.bodyAsClass(DevolucaoRequest.class);
            facade.marcarDevolucaoComoInvalida(req.idDevolucao());
            ctx.status(204);
        });
    }
}
