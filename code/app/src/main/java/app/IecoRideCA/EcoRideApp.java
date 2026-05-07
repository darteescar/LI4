package app.IecoRideCA;

import app.IecoRideCA.auth.GestorSessoes;
import app.IecoRideCA.auth.SessaoUtilizador;
import app.IecoRideCA.controllers.auth.AuthController;
import app.IecoRideCA.controllers.clientes.ClientesController;
import app.IecoRideCA.controllers.financeiro.FinanceiroController;
import app.IecoRideCA.controllers.funcionarios.FuncionariosController;
import app.IecoRideCA.controllers.notificacoes.NotificacoesController;
import app.IecoRideCA.controllers.ordensservico.OrdemServicoController;
import app.IecoRideCA.controllers.reparacoes.ReparacoesController;
import app.IecoRideCA.controllers.stock.StockController;
import app.common.EcoRideException;
import app.ecoRideLN.EcoRideLN;
import app.ecoRideLN.IEcoRideLN;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EcoRideApp {

    public static void main(String[] args) {
        IEcoRideLN    facade        = new EcoRideLN();
        GestorSessoes gestorSessoes = new GestorSessoes();

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(m ->
                m.registerModule(new JavaTimeModule())
            ));
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(it -> it.allowHost("http://localhost:5173"))
            );
        }).start(8080);

        // Middleware — todas as rotas /api/* exigem sessão válida
        app.before("/api/*", ctx -> {
            String token = ctx.header("Authorization");
            if (token == null) throw new UnauthorizedResponse("Token em falta");
            SessaoUtilizador sessao = gestorSessoes.validar(token);
            ctx.attribute("sessao", sessao);
        });

        // Tratamento global de erros de negócio
        app.exception(EcoRideException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(e.getMessage()))
        );
        app.exception(NumberFormatException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse("ID inválido"))
        );

        // Registo de controllers
        new AuthController(facade, gestorSessoes).register(app);
        new ClientesController(facade).register(app);
        new OrdemServicoController(facade).register(app);
        new StockController(facade).register(app);
        new FuncionariosController(facade).register(app);
        new ReparacoesController(facade).register(app);
        new NotificacoesController(facade).register(app);
        new FinanceiroController(facade).register(app);
    }

    record ErroResponse(String erro) {}
}
