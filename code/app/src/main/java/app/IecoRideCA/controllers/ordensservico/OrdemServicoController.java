package app.IecoRideCA.controllers.ordensservico;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.ordensservico.dto.ConsertoRequest;
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
        

    }
}
