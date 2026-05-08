package app.IecoRideCA.controllers.funcionarios;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.funcionarios.dto.FuncionarioRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sFuncionarios.Funcionario;
import io.javalin.Javalin;

public class FuncionariosController {

    private final IEcoRideLN facade;

    public FuncionariosController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/funcionarios", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            ctx.json(facade.obterFuncionarios());
        });

        app.get("/api/funcionarios/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Funcionario c = facade.obterFuncionario(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/funcionarios", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            FuncionarioRequest req = ctx.bodyAsClass(FuncionarioRequest.class);
            Funcionario criado = facade.registarFuncionario(req.nome(), req.email(), req.telemovel(), req.data_nascimento(), req.NISS(), req.NIF(), req.NUS(), req.IBAN(), req.salario_hora(), req.salario_liquido(), req.salario_bruto(), req.horas_extra(), req.numero_porta(), req.rua(), req.localidade(), req.codigo_postal());
            ctx.status(201).json(criado);
        });

        app.delete("/api/funcionarios/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerFuncionario(id) ? 204 : 404);
        });

        app.patch("/api/funcionarios/pagar/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.registarPagamentoFuncionario(id) ? 204 : 404);
        });

        app.patch("/api/funcionarios/horas_extra/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            int horas_extra = Integer.parseInt(ctx.queryParam("horas_extra"));
            facade.registarHorasExtraFuncionario(id, horas_extra);
            ctx.status(204);
        });

    }
}
