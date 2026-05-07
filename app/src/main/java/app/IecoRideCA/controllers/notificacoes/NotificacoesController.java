package app.IecoRideCA.controllers.notificacoes;

import app.IecoRideCA.auth.GestorSessoes;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import io.javalin.Javalin;

public class NotificacoesController {

    private final IEcoRideLN facade;

    public NotificacoesController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/notificacoes/destinatario/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            ctx.json(facade.obterNotificacoesPorDestinatario(Integer.parseInt(ctx.pathParam("id"))));
        });

        app.patch("/api/notificacoes/{id}/lida", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            boolean ok = facade.sinalizarNotificacao_comoLida(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        app.patch("/api/notificacoes/{id}/tratada", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            boolean ok = facade.sinalizarNotificacao_comoTratada(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        app.delete("/api/notificacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            ctx.status(facade.removerNotificacao(Integer.parseInt(ctx.pathParam("id"))) ? 204 : 404);
        });
    }
}
