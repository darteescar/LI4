package app.IecoRideCA.controllers.clientes;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.controllers.clientes.dto.ClienteRequest;
import app.IecoRideCA.controllers.clientes.dto.TrotineteRequest;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sClientes.Cliente;
import app.ecoRideLN.sClientes.Trotinete;
import io.javalin.Javalin;

public class ClientesController {

    private final IEcoRideLN facade;

    public ClientesController(IEcoRideLN facade) {
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
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/clientes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            ClienteRequest req = ctx.bodyAsClass(ClienteRequest.class);
            Cliente criado = facade.registarCliente(req.nome(), req.email(), req.telemovel(), req.nif());
            ctx.status(201).json(criado);
        });

        app.delete("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerCliente(id) ? 204 : 404);
        });

        app.get("/api/trotinetes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            ctx.json(facade.obterTrotinetes());
        });

        app.get("/api/trotinetes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Trotinete c = facade.obterTrotinete(id);
            if (c == null) ctx.status(404);
            else ctx.json(c);
        });

        app.post("/api/trotinetes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            TrotineteRequest req = ctx.bodyAsClass(TrotineteRequest.class);
            Trotinete criado = facade.registarTrotinete(req.cod_cliente(), req.modelo(), req.marca(), req.num_serie(), req.motor());
            ctx.status(201).json(criado);
        });

        app.delete("/api/trotinetes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(facade.removerTrotinete(id) ? 204 : 404);
        });
    }
}
