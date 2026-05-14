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

        // Clientes

        // List<Cliente>
        app.get("/api/clientes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria, Cargo.Mecanico); // Mecanicos podem ver clientes para diagnostico
            ctx.status(200).json(facade.obterClientes());
        });

        // Obter Cliente por id
        app.get("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Cliente c = facade.obterCliente(id);
            if (c == null) {
                ctx.status(404);
            } else {
                ctx.status(200).json(c);
            }
        });

        // Criar Cliente
        app.post("/api/clientes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            ClienteRequest req = ctx.bodyAsClass(ClienteRequest.class);
            ctx.status(201).json(facade.registarCliente(req.nome(), req.email(), req.telemovel(), req.nif()));
        });

        // Eliminar Cliente
        app.delete("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = facade.removerCliente(id);
            if (removed) {
                ctx.status(204);
            } else {
                ctx.status(404);
            }
        });

        // Editar Cliente
        app.patch("/api/clientes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            ClienteRequest req = ctx.bodyAsClass(ClienteRequest.class);
            Cliente editado = facade.atualizarCliente(id, req.nome(), req.email(), req.telemovel(), req.nif());
            if (editado == null) {
                ctx.status(404);
            } else {
                ctx.status(200).json(editado);
            }
        });

        // Trotinetes

        // List<Trotinete>
        app.get("/api/trotinetes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria, Cargo.Mecanico); // Mecanicos podem ver trotinetes para diagnostico
            ctx.status(200).json(facade.obterTrotinetes());
        });

        // Obter Trotinete por id
        app.get("/api/trotinetes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            Trotinete c = facade.obterTrotinete(id);
            if (c == null) {
                ctx.status(404);
            } else {
                ctx.status(200).json(c);
            }
        });

        // Criar Trotinete
        app.post("/api/clientes/{id}/trotinetes", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            TrotineteRequest req = ctx.bodyAsClass(TrotineteRequest.class);
            ctx.status(201).json(facade.registarTrotinete(id, req.modelo(), req.marca(), req.num_serie(), req.motor()));
        });

        // Apagar Trotinete
        app.delete("/api/trotinetes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = facade.removerTrotinete(id);
            if (removed) {
                ctx.status(204);
            } else {
                ctx.status(404);
            }
        });

        // Atualizar Trotinete
        app.patch("/api/trotinetes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Secretaria);
            int id = Integer.parseInt(ctx.pathParam("id"));
            TrotineteRequest req = ctx.bodyAsClass(TrotineteRequest.class);
            Trotinete editado = facade.atualizarTrotinete(id, req.cod_cliente(), req.modelo(), req.marca(), req.num_serie(), req.motor());
            if (editado == null) {
                ctx.status(404);
            } else {
                ctx.status(200).json(editado);
            }
        });
    }
}
