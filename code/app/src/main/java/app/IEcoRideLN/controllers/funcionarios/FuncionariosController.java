package app.IEcoRideLN.controllers.funcionarios;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.IEcoRideLN.auth.GestorSessoes;
import app.IEcoRideLN.controllers.funcionarios.dto.FuncionarioRequest;
import app.ecoRideLN.IEcoRideController;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sFuncionarios.Funcionario;
import io.javalin.Javalin;

public class FuncionariosController {

    private final IEcoRideController facade;

    public FuncionariosController(IEcoRideController facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        // List<Funcionario> — Gerente recebe dados completos; outros apenas {id, nome}
        app.get("/api/funcionarios", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria, Cargo.GestorStock);
            List<Funcionario> todos = facade.obterFuncionarios();
            if (GestorSessoes.sessao(ctx).getCargo() == Cargo.Gerente) {
                ctx.json(todos);
            } else {
                ctx.json(todos.stream()
                    .map(f -> Map.of("id", f.getId(), "nome", f.getNome()))
                    .collect(Collectors.toList()));
            }
        });

        // Obter Funcionario por ID — Gerente recebe dados completos; outros apenas {id, nome}
        app.get("/api/funcionarios/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.Secretaria, Cargo.GestorStock);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Funcionario c = facade.obterFuncionario(id);
            if (c == null) {
                ctx.status(404);
            } else if (GestorSessoes.sessao(ctx).getCargo() == Cargo.Gerente) {
                ctx.json(c);
            } else {
                ctx.json(Map.of("id", c.getId(), "nome", c.getNome()));
            }
        });

        // Criar Funcionario
        app.post("/api/funcionarios", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            FuncionarioRequest req = ctx.bodyAsClass(FuncionarioRequest.class);
            Funcionario criado = facade.registarFuncionario(req.nome(), req.telemovel(), req.email(), req.data_nascimento(), req.NISS(), req.NIF(), req.NUS(), req.IBAN(), req.salario_hora(), req.salario_liquido(), req.salario_bruto(), req.horas_extra(), req.numero_porta(), req.rua(), req.localidade(), req.codigo_postal());
            ctx.status(201).json(criado);
        });

        // Remover Funcionario
        app.delete("/api/funcionarios/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFuncionario(id) ? 204 : 404);
        });

        // Atualizar Funcionario
        app.patch("/api/funcionarios/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            FuncionarioRequest req = ctx.bodyAsClass(FuncionarioRequest.class);
            Funcionario atualizado = facade.atualizarFuncionario(id, req.nome(), req.telemovel(), req.email(), req.data_nascimento(), req.NISS(), req.NIF(), req.NUS(), req.IBAN(), req.salario_hora(), req.salario_liquido(), req.salario_bruto(), req.horas_extra(), req.numero_porta(), req.rua(), req.localidade(), req.codigo_postal());
            if (atualizado == null) ctx.status(404);
            else ctx.status(200).json(atualizado);
        });

        // Pagar Funcionario
        app.patch("/api/funcionarios/pagar/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.registarPagamentoFuncionario(id) ? 204 : 404);
        });

        // Registar Horas Extra Funcionario
        app.patch("/api/funcionarios/horas_extra/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            FuncionarioRequest req = ctx.bodyAsClass(FuncionarioRequest.class);
            facade.registarHorasExtraFuncionario(id, req.horas_extra());
            ctx.status(204);
        });

    }
}
