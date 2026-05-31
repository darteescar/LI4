package app.IEcoRideLN.controllers.ordensservico;

import java.util.List;
import java.util.Map;

import app.IEcoRideLN.auth.GestorSessoes;
import app.IEcoRideLN.controllers.ordensservico.dto.ConsertoRequest;
import app.IEcoRideLN.controllers.ordensservico.dto.DiagnosticoRequest;
import app.IEcoRideLN.controllers.ordensservico.dto.OrdemServicoRequest;
import app.IEcoRideLN.controllers.ordensservico.dto.PagamentoRequest;
import app.IEcoRideLN.controllers.ordensservico.dto.PegarOSRequest;
import app.ecoRideLN.IEcoRideController;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sOrdensServico.OrdemServico;
import io.javalin.Javalin;

public class OrdemServicoController {

    private final IEcoRideController facade;

    public OrdemServicoController(IEcoRideController facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // List<OrdemServico> — por defeito devolve apenas estados não-terminais; ?historico=true inclui Paga e Eliminada
        app.get("/api/ordensservicos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            boolean historico = "true".equalsIgnoreCase(ctx.queryParam("historico"));
            ctx.json(historico ? facade.obterOSs() : facade.obterOSsAtivas());
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

        // List<OrdemServico> disponíveis para mecanicos
        app.get("/api/ordensservicos/disponiveis", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            List<OrdemServico> res = facade.obterOSsDisponiveis();
            if (res == null) ctx.status(404);
            else ctx.json(res);
        });

        // Obter OrdemServico por id
        app.get("/api/ordensservicos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrdemServico c = facade.obterOS(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.get("/api/ordensservicos/{id}/conserto/pecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<Integer, Integer> pecasUsadas = facade.obterPecasUsadasConsertoOS(id);
            ctx.json(pecasUsadas);
        });

        // Registar OrdemServico — codCriador vem da sessão
        app.post("/api/ordensservicos", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria);
            OrdemServicoRequest req = ctx.bodyAsClass(OrdemServicoRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            OrdemServico criado = facade.registarOS(req.id_cliente(), req.id_trotinete(), req.descricao(), req.acessorios(), idFuncionario);
            ctx.status(201).json(criado);
        });

        // Remover OrdemServico
        app.delete("/api/ordensservicos/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerOS(id) ? 204 : 404);
        });

        // Atribuir OrdemServico a Funcionario
        app.patch("/api/ordensservicos/{id}/atribuir", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PegarOSRequest req = ctx.bodyAsClass(PegarOSRequest.class);
            facade.atribuirOS(id, req.id_funcionario());
            ctx.status(204);
        });

        // Cancelar OrdemServico
        app.patch("/api/ordensservicos/{id}/cancelar", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            facade.cancelarOS(id);
            ctx.status(204);
        });

        // Adicionar Diagnostico a OrdemServico
        app.patch("/api/ordensservicos/{id}/diagnostico", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            DiagnosticoRequest req = ctx.bodyAsClass(DiagnosticoRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.registarDiagnosticoOS(id, req.pecasQuantidades(), req.reparacoes(), req.descricao(), idFuncionario);
            ctx.status(204);
        });

        // Adicionar Conserto a OrdemServico
        app.patch("/api/ordensservicos/{id}/conserto", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ConsertoRequest req = ctx.bodyAsClass(ConsertoRequest.class);
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.registarConsertoOS(id, req.pecasQuantidades(), req.reparacoes(), idFuncionario, req.checklist());
            ctx.status(204);
        });

        // Aprovar orçamento
        app.patch("/api/ordensservicos/{id}/aprovarOrcamento", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.aprovarOrcamentoOS(id, idFuncionario);
            ctx.status(204);
        });

        // Rejeitar orçamento
        app.patch("/api/ordensservicos/{id}/rejeitarOrcamento", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.rejeitarOrcamentoOS(id, idFuncionario);
            ctx.status(204);
        });

        // Registar notificação de pagamento ao cliente
        app.patch("/api/ordensservicos/{id}/notificarCliente", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            boolean ok = facade.registarNotificacaoPagamentoOS(id, idFuncionario);
            ctx.status(ok ? 204 : 409);
        });

        // Registar pagamento da OS
        app.patch("/api/ordensservicos/{id}/pagar", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            PagamentoRequest req = ctx.bodyAsClass(PagamentoRequest.class);
            boolean ok = facade.registarPagamentoOS(id, req.metodo_pagamento());
            ctx.status(ok ? 204 : 409);
        });

        app.patch("/api/ordensservicos/{id}/aguardarPecas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico);
            int id = Integer.parseInt(ctx.pathParam("id"));
            int idFuncionario = GestorSessoes.sessao(ctx).getIdFuncionario();
            facade.aguardarPecas(id, idFuncionario);
            ctx.status(204);
        });
    }
}
