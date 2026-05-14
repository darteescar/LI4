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

        // List<Notificacao> por destinatário
        app.get("/api/notificacoes/minhas", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            int id = GestorSessoes.sessao(ctx).getIdUtilizador();
            ctx.json(facade.obterNotificacoesPorDestinatario(id));
        });

        // Colocar como lida
        app.patch("/api/notificacoes/lida/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            boolean ok = facade.sinalizarNotificacao_comoLida(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // Colocar como tratada
        app.patch("/api/notificacoes/tratada/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            boolean ok = facade.sinalizarNotificacao_comoTratada(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // Eliminar notificação
        app.delete("/api/notificacoes/{id}", ctx -> {
            GestorSessoes.verifica_cargo(ctx, Cargo.Gerente, Cargo.Mecanico, Cargo.GestorStock, Cargo.Secretaria);
            ctx.status(facade.removerNotificacao(Integer.parseInt(ctx.pathParam("id"))) ? 204 : 404);
        });
    }
}
