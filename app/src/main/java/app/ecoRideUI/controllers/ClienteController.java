package app.ecoRideUI.controllers;

import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideUI.auth.GestorSessoes;
import app.ecoRideUI.auth.SessaoUtilizador;
import io.javalin.Javalin;

public class ClienteController {

    private final IEcoRideLN facade;

    public ClienteController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/clientes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            ctx.json(facade.obterClientes());
        });

        app.get("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Cliente c = facade.obterCliente(id);
            if (c == null) {
                ctx.status(404); 
            }else {
                ctx.json(c);
            }
        });

        app.post("/api/clientes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            CriarClienteRequest req = ctx.bodyAsClass(CriarClienteRequest.class);
            Cliente criado = facade.registarCliente(req.nome(), req.email(), req.telemovel(), req.nif());
            ctx.status(201).json(criado);
        });

        app.delete("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerCliente(id) ? 204 : 404);
        });
    }

    record CriarClienteRequest(String nome, String email, String telemovel, String nif) {

    }
}
